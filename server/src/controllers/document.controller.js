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

const getDocuments = catchAsync(async (req, res) => {
	const filter = pick(req.query, ['name', 'role']);
	const options = pick(req.query, ['sortBy', 'limit', 'page']);
	const result = await documentService.queryDocuments(filter, options);
	res.send(result);
});

const getDocument = catchAsync(async (req, res) => {
	const document = await documentService.getDocumentById(req.params.userId);
	if (!document) {
		throw new ApiError(httpStatus.NOT_FOUND, 'Document not found');
	}
	res.send(document);
});

module.exports = {
	createDocument,
	getDocuments,
	getDocument,
};