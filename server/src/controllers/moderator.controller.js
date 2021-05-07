const httpStatus = require('http-status');
const pick = require('../utils/pick');
const ApiError = require('../utils/ApiError');
const catchAsync = require('../utils/catchAsync');
const { moderatorService, userService } = require('../services');
const { statusTypes, priceTypes, documentTypes } = require('../config/documents');

const getDocuments = catchAsync(async (req, res) => {
	const filter = pick(req.query, ['name', 'type']);
	filter.status = statusTypes.PENDING;
	const options = pick(req.query, ['sortBy', 'limit', 'page']);
	const result = await moderatorService.queryDocuments(filter, options);
	res.send(result);
});

const approve = catchAsync(async (req, res) => {
	req.body.status = statusTypes.APPROVED;
	const document = await moderatorService.updateDocumentById(req.params.documentId, req.body);
	const user = await userService.addCoins(document.user, priceTypes[document.type.toUpperCase()]);
	res.send(document);
});

const decline = catchAsync(async (req, res) => {
	req.body.status = statusTypes.DECLINED;
	const document = await moderatorService.updateDocumentById(req.params.documentId, req.body);
	res.send(document);
});


module.exports = {
	getDocuments,
	approve,
	decline,
};