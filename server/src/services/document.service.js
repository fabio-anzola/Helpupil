const httpStatus = require('http-status');
const { User } = require('../models');
const ApiError = require('../utils/ApiError');
const { Doc } = require('../models');

/**
 * Create a document in db
 * @param {Object} docuemntBody
 * @returns {Promise<User>}
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
 * Get user by id
 * @param {ObjectId} id
 * @returns {Promise<User>}
 */
const getDocumentById = async (id) => {
  return Doc.findById(id);
};

module.exports = {
    createDoc,
    queryDocuments,
		getDocumentById,
};