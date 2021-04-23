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

module.exports = {
	createSubject,
};
