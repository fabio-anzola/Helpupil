const httpStatus = require('http-status');
const { Subject } = require('../models');
const ApiError = require('../utils/ApiError');

/**
 * Create a subject
 * @param {Object}  subjectBody
 * @returns {Promise<Subject>}
 */
const createSubject = async (subjectBody, userBody) => {
  if (await Subject.isNameTaken(subjectBody.name) || await Subject.isShortnameTaken(subjectBody.shortname)) {
    throw new ApiError(httpStatus.BAD_REQUEST, 'Name or Shortname already taken');
  }
	subjectBody.user = userBody._id;
  const subject = await Subject.create(subjectBody);
  return subject;
};

/**
 * Query for subjects
 * @param {Object} filter - Mongo filter
 * @param {Object} options - Query options
 * @param {string} [options.sortBy] - Sort option in the format: sortField:(desc|asc)
 * @param {number} [options.limit] - Maximum number of results per page (default = 10)
 * @param {number} [options.page] - Current page (default = 1)
 * @returns {Promise<QueryResult>}
 */
 const querySubjects = async (filter, options) => {
  const subjects = await Subject.paginate(filter, options);
  return subjects;
};

module.exports = {
	createSubject,
	querySubjects,
};
