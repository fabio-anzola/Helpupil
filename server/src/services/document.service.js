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

module.exports = {
    createDoc,
};