const httpStatus = require('http-status');
const pick = require('../utils/pick');
const ApiError = require('../utils/ApiError');
const catchAsync = require('../utils/catchAsync');
const { documentService } = require('../services');

const createDocument = catchAsync(async (req, res) => {
    const obj = {name: req.body.name, type: req.body.type, user: req.user._id, rating: 0, file: req.file};
    const documet = await documentService.createDoc(obj);
    res.status(httpStatus.CREATED).send(obj);
});

module.exports = {
    createDocument,
};