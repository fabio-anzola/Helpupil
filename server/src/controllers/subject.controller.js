const httpStatus = require('http-status');
const pick = require('../utils/pick');
const ApiError = require('../utils/ApiError');
const catchAsync = require('../utils/catchAsync');
const { subjectService } = require('../services');

const createSubject = catchAsync(async (req, res) => {
	const subject = await subjectService.createSubject(req.body, req.user);
	res.status(httpStatus.CREATED).send(subject);
});

const getSubjects = catchAsync(async (req, res) => {
	const filter = pick(req.query, ['name', 'role']);
	const options = pick(req.query, ['sortBy', 'limit', 'page']);
	const result = await subjectService.querySubjects(filter, options);
	res.send(result);
});

const getSubject = catchAsync(async (req, res) => {
	const subject = await subjectService.getSubjectById(req.params.subjectId);
	if (!subject) {
		throw new ApiError(httpStatus.NOT_FOUND, 'Subject not found');
	}
	res.send(subject);
});

const updateSubject = catchAsync(async (req, res) => {
	const subject = await subjectService.updateSubjectById(req.params.subjectId, req.body);
	res.send(subject);
});

const deleteSubject = catchAsync(async (req, res) => {
	await subjectService.deleteSubjectById(req.params.subjectId);
	res.status(httpStatus.NO_CONTENT).send();
});

module.exports = {
	createSubject,
	getSubjects,
	getSubject,
	updateSubject,
	deleteSubject,
};
