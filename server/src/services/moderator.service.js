const httpStatus = require('http-status');
const ApiError = require('../utils/ApiError');
const { Doc } = require('../models');

/**
 * Query for documents
 * @param {Object} filter - Mongo filter
 * @param {Object} options - Query options
 * @param {string} [options.sortBy] - Sort option in the format: sortField:(desc|asc)
 * @param {number} [options.limit] - Maximum number of results per page (default = 10)
 * @param {number} [options.page] - Current page (default = 1)
 * @returns {Promise<QueryResult>}
 */
const queryDocuments = async (filter, options) => {
    const documents = await Doc.paginate(filter, options);
    return documents;
};

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
	queryDocuments,
	getDocumentById,
	updateDocumentById,
};