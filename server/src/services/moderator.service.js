const httpStatus = require('http-status');
const emailService = require('./email.service');
const ApiError = require('../utils/ApiError');
const { Doc } = require('../models');

/**
 * Get document by id
 * @param {ObjectId} id
 * @returns {Promise<Document>}
 */
const getDocumentById = async (id) => {
  return Doc.findById(id);
};

/**
 * Update document by id
 * @param {ObjectId} documentId
 * @param {Object} updateBody
 * @returns {Promise<Document>}
 */
const updateDocumentById = async (documentId, updateBody) => {
	const document = await getDocumentById(documentId);
	if (!document) {
		throw new ApiError(httpStatus.NOT_FOUND, 'Document not found');
	}
	Object.assign(document, updateBody);
	await document.save();
	return document;
};

module.exports = {
	getDocumentById,
	updateDocumentById,
};