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

/**
 * Get subject by id
 * @param {ObjectId} id
 * @returns {Promise<Subject>}
 */
const getSubjectById = async (id) => {
	return Subject.findById(id);
};

/**
 * Update user by id
 * @param {ObjectId} subjectId
 * @param {Object} updateBody
 * @returns {Promise<Subject>}
 */
const updateSubjectById = async (subjectId, updateBody) => {
	console.log(updateBody);
	const subject = await getSubjectById(subjectId);
	if (!subject) {
		throw new ApiError(httpStatus.NOT_FOUND, 'Subject not found');
	}
	if (updateBody.name && (await Subject.isNameTaken(updateBody.name, subjectId))) {
		throw new ApiError(httpStatus.BAD_REQUEST, 'Name already taken');
	}
	if (updateBody.shortname && (await Subject.isShortnameTaken(updateBody.shortname, subjectId))) {
		throw new ApiError(httpStatus.BAD_REQUEST, 'Shortname already taken');
	}
	Object.assign(subject, updateBody);
	await subject.save();
	return subject;
};

/**
 * Delete subject by id
 * @param {ObjectId} subjectId
 * @returns {Promise<Subject>}
 */
const deleteSubjectById = async (subjectId) => {
	const subject = await getSubjectById(subjectId);
	if (!subject) {
		throw new ApiError(httpStatus.NOT_FOUND, 'Subject not found');
	}
	await subject.remove();
	return subject;
};

module.exports = {
	createSubject,
	querySubjects,
	getSubjectById,
	updateSubjectById,
	deleteSubjectById,
};
