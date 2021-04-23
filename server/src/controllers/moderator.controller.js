const httpStatus = require('http-status');
const pick = require('../utils/pick');
const ApiError = require('../utils/ApiError');
const catchAsync = require('../utils/catchAsync');
const { moderatorService } = require('../services');
const { statusTypes } = require('../config/documents');

const getDocuments = catchAsync(async (req, res) => {
	const filter = pick(req.query, ['name', 'type']);
	filter.status = statusTypes.PENDING;
	const options = pick(req.query, ['sortBy', 'limit', 'page']);
	const result = await moderatorService.queryDocuments(filter, options);
	res.send(result);
});

const approve = catchAsync(async (req, res) => {
	const document = await moderatorService.getDocumentById(req.params.documentId);
	if (!document) {
		throw new ApiError(httpStatus.NOT_FOUND, 'Document not found');
	}
	res.send(document);
});

const decline = catchAsync(async (req, res) => {
	const document = await moderatorService.getDocumentById(req.params.documentId);
	if (!document) {
		throw new ApiError(httpStatus.NOT_FOUND, 'Document not found');
	}
	res.send(document);
});

module.exports = {
	getDocuments,
	approve,
	decline,
};