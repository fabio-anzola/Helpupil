const httpStatus = require('http-status');
const {
  User
} = require('../models');
const ApiError = require('../utils/ApiError');
const {
  Doc
} = require('../models');

/**
 * Create a document in db
 * @param {Object} docuemntBody
 * @returns {Promise<Document>}
 */
const createDoc = async (documentBody) => {
  if (!documentBody) {
    throw new ApiError(httpStatus.BAD_REQUEST, 'Something is wrong');
  }
  const document = await Doc.create(documentBody);
  return document;
};

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
 * Get document by name
 * @param {ObjectId} name
 * @returns {Promise<Document>}
 */
const getDocumentByName = async (name) => {
  return Doc.findOne({
    "file.filename": name
  });
};


/**
 * Delete document by id
 * @param {ObjectId} documentId
 * @returns {Promise<Document>}
 */
const deleteDocumentById = async (documentId) => {
  const document = await getDocumentById(documentId);
  if (!document) {
    throw new ApiError(httpStatus.NOT_FOUND, 'Document not found');
  }
  await document.remove();
  return document;
};

module.exports = {
  createDoc,
  queryDocuments,
  getDocumentById,
  deleteDocumentById,
  getDocumentByName,
};
