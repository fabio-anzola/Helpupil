const fs = require('fs');
const httpStatus = require('http-status');
const pick = require('../utils/pick');
const ApiError = require('../utils/ApiError');
const catchAsync = require('../utils/catchAsync');
const { documentService } = require('../services');
const {  documentTypes, priceTypes, statusTypes } = require('../config/documents');

const createDocument = catchAsync(async (req, res) => {
	fs.writeFile("xyz.pdf", req.file.buffer, 'base64', () => {console.log("Hurray")});
	const obj = {
		name: req.body.name, 
		type: req.body.type, 
		user: req.user._id, 
		rating: 0, file: 
		req.file, status: statusTypes.PENDING, 
		reviewer: [], 
		subject: req.body.subject,
		teacher: req.body.teacher,
	};
	const document = await documentService.createDoc(obj);
	res.status(httpStatus.CREATED).send(obj);
});

const getDocuments = catchAsync(async (req, res) => {
	const filter = pick(req.query, ['name', 'type', 'subject', 'teacher']);
	filter.status = statusTypes.APPROVED;
	const options = pick(req.query, ['sortBy', 'limit', 'page']);
	const result = await documentService.queryDocuments(filter, options);
	res.send(result);
});

const getDocument = catchAsync(async (req, res) => {
	const document = await documentService.getDocumentById(req.params.documentId);
	if (!document) {
		throw new ApiError(httpStatus.NOT_FOUND, 'Document not found');
	}
	if (document.status != statusTypes.APPROVED && req.user.role != 'moderator') {
		throw new ApiError(httpStatus.NOT_FOUND, 'Document not approved');
	}
	res.send(document);
});

const deleteDocument = catchAsync(async (req, res) => {
	const document = await documentService.getDocumentById(req.params.documentId);
	if (!document) {
		throw new ApiError(httpStatus.NOT_FOUND, 'Document not found');
	}
	if (req.user.role == 'moderator' || document.user.toString() == req.user._id.toString()) {
		await documentService.deleteDocumentById(req.params.documentId);
	} else {
		throw new ApiError(httpStatus.UNAUTHORIZED, 'You dont have the required rights');
	}
  res.status(httpStatus.NO_CONTENT).send();
});

/**
 * Get document-types
 * @returns {Promise<Object>}
 */
 const getDocumentTypes = (req, res) => {
	let obj = {
	  keys: Object.keys(documentTypes),
	  values: Object.keys(documentTypes).map(k => documentTypes[k]),
		friendly_values: Object.keys(documentTypes).map(k => documentTypes[k].charAt(0).toUpperCase() + documentTypes[k].slice(1)),
	  prices: Object.keys(priceTypes).map(k => priceTypes[k]),
	};
	res.send(obj);
}

module.exports = {
	createDocument,
	getDocuments,
	getDocument,
	deleteDocument,
	getDocumentTypes,
};