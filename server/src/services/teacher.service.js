const httpStatus = require('http-status');
const { Teacher } = require('../models');
const ApiError = require('../utils/ApiError');

/**
 * Create a teacher
 * @param {Object}  teacherBody
 * @returns {Promise<Teacher>}
 */
const createTeacher = async (teacherBody, userBody) => {
	if (await Teacher.isNameTaken(teacherBody.name) || await Teacher.isShortnameTaken(teacherBody.shortname)) {
		throw new ApiError(httpStatus.BAD_REQUEST, 'Name or Shortname already taken');
	}
	teacherBody.user = userBody._id;
	const teacher = await Teacher.create(teacherBody);
	return teacher;
};

/**
 * Query for teachers
 * @param {Object} filter - Mongo filter
 * @param {Object} options - Query options
 * @param {string} [options.sortBy] - Sort option in the format: sortField:(desc|asc)
 * @param {number} [options.limit] - Maximum number of results per page (default = 10)
 * @param {number} [options.page] - Current page (default = 1)
 * @returns {Promise<QueryResult>}
 */
const queryTeachers = async (filter, options) => {
	const teachers = await Teacher.paginate(filter, options);
	return teachers;
};

/**
 * Get teacher by id
 * @param {ObjectId} id
 * @returns {Promise<Teacher>}
 */
const getTeacherById = async (id) => {
	return Teacher.findById(id);
};

/**
 * Update teacher by id
 * @param {ObjectId} teacherId
 * @param {Object} updateBody
 * @returns {Promise<Teacher>}
 */
const updateTeacherById = async (teacherId, updateBody) => {
	console.log(updateBody);
	const teacher = await getTeacherById(teacherId);
	if (!teacher) {
		throw new ApiError(httpStatus.NOT_FOUND, 'Teacher not found');
	}
	if (updateBody.name && (await Teacher.isNameTaken(updateBody.name, teacherId))) {
		throw new ApiError(httpStatus.BAD_REQUEST, 'Name already taken');
	}
	if (updateBody.shortname && (await Teacher.isShortnameTaken(updateBody.shortname, teacherId))) {
		throw new ApiError(httpStatus.BAD_REQUEST, 'Shortname already taken');
	}
	Object.assign(teacher, updateBody);
	await teacher.save();
	return teacher;
};

/**
 * Delete teacher by id
 * @param {ObjectId} teacherId
 * @returns {Promise<Teacher>}
 */
const deleteTeacherById = async (teacherId) => {
	const teacher = await getTeacherById(teacherId);
	if (!teacher) {
		throw new ApiError(httpStatus.NOT_FOUND, 'Teacher not found');
	}
	await teacher.remove();
	return teacher;
};

module.exports = {
	createTeacher,
	queryTeachers,
	getTeacherById,
	updateTeacherById,
	deleteTeacherById,
};
