const httpStatus = require('http-status');
const pick = require('../utils/pick');
const ApiError = require('../utils/ApiError');
const catchAsync = require('../utils/catchAsync');
const { emailService, documentService, moderatorService, userService } = require('../services');
const { statusTypes, priceTypes, documentTypes } = require('../config/documents');

const getDocuments = catchAsync(async (req, res) => {
	const filter = pick(req.query, ['name', 'type']);
	filter.status = statusTypes.PENDING;
	const options = pick(req.query, ['sortBy', 'limit', 'page']);
	const result = await documentService.queryDocuments(filter, options);
	res.send(result);
});

const approve = catchAsync(async (req, res) => {
	const doc = await documentService.getDocumentById(req.params.documentId);
	if (doc.status == statusTypes.APPROVED) {
		throw new ApiError(httpStatus.FORBIDDEN, 'Document already approved');
	}
	req.body.status = statusTypes.APPROVED;
	const document = await moderatorService.updateDocumentById(req.params.documentId, req.body);
	await userService.addCoins(document.user, priceTypes[document.type.toUpperCase()]);
	const user = await userService.getUserById(document.user);
	if (!user.purchasedDocuments.includes(document._id)) {
		user.purchasedDocuments.push(document._id);
		await userService.updateUserById(document.user, user);
	}
	emailService.sendApproveEmail(user.email, user.name, doc.name)
	res.send(document);
});

const decline = catchAsync(async (req, res) => {
	req.body.status = statusTypes.DECLINED;
	const document = await moderatorService.updateDocumentById(req.params.documentId, req.body);
	const user = await userService.getUserById(document.user);
	emailService.sendDeclineEmail(user.email, user.name, document.name, req.query.message)
	await documentService.deleteDocumentById(req.params.documentId);

	res.send(document);
});


module.exports = {
	getDocuments,
	approve,
	decline,
};