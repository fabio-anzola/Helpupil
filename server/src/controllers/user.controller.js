const httpStatus = require('http-status');
const pick = require('../utils/pick');
const ApiError = require('../utils/ApiError');
const catchAsync = require('../utils/catchAsync');
const { userService, emailService, tokenService, authService } = require('../services');

const createUser = catchAsync(async (req, res) => {
  const user = await userService.createUser(req.body);
  res.status(httpStatus.CREATED).send(user);
});

const getUsers = catchAsync(async (req, res) => {
  const filter = pick(req.query, ['name', 'role']);
  const options = pick(req.query, ['sortBy', 'limit', 'page']);
  const result = await userService.queryUsers(filter, options);
  res.send(result);
});

const getUser = catchAsync(async (req, res) => {
  const user = await userService.getUserById(req.params.userId);
  if (!user) {
    throw new ApiError(httpStatus.NOT_FOUND, 'User not found');
  }
  res.send(user);
});

const getPublicUser = catchAsync(async (req, res) => {
  const user = await userService.getUserById(req.params.userId);
  if (!user) {
    throw new ApiError(httpStatus.NOT_FOUND, 'User not found');
  }
  res.send({
    "id": user.id,
    "name": user.name,
  });
});

const updateUser = catchAsync(async (req, res) => {
  if (req.body.email != null || req.body.password != null) {
    if (req.body.currentPassword == null) {
      throw new ApiError(httpStatus.FORBIDDEN, 'Please enter your current password');
    }
    await authService.loginUserWithEmailAndPassword(req.user.email, req.body.currentPassword);
  }
  if (req.body.email != null) {
    req.body.isEmailVerified = false;
  }
  const user = await userService.updateUserById(req.params.userId, req.body);
  if (req.body.email != null) {
    const verifyEmailToken = await tokenService.generateVerifyEmailToken({ id: user.id});
    await emailService.sendVerificationEmail(user.email, user.name, verifyEmailToken);
  }
  res.send(user);
});

const deleteUser = catchAsync(async (req, res) => {
  await userService.deleteUserById(req.params.userId);
  res.status(httpStatus.NO_CONTENT).send();
});

const topUsers = catchAsync(async (req, res) => {
  const filter = { isEmailVerified: true };
  const options = { sortBy: 'wallet:desc' };
  const resu = pick( await userService.queryUsers(filter, options), ['results']);
  let result = {results: []};
  for (let i = 0; i < resu.results.length; i++) {
    const element = { name: resu.results[i].name, wallet: resu.results[i].wallet };
    (result.results).push(element);
  }
  res.send(result);
});

module.exports = {
  createUser,
  getUsers,
  getUser,
  updateUser,
  deleteUser,
  getPublicUser,
  topUsers,
};
