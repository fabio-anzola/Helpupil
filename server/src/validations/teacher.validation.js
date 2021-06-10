const Joi = require('joi');
const { objectId } = require('./custom.validation');

const createTeacher = {
	body: Joi.object().keys({
		name: Joi.string().required(),
		shortname: Joi.string().required(),
		description: Joi.string().allow(null, ''),
	}),
};

const getTeachers = {
	query: Joi.object().keys({
		name: Joi.string(),
		shortname: Joi.string(),
		sortBy: Joi.string(),
		limit: Joi.number().integer(),
		page: Joi.number().integer(),
	}),
};

const getTeacher = {
	params: Joi.object().keys({
		teacherId: Joi.string().custom(objectId),
	}),
};

const updateTeacher = {
	params: Joi.object().keys({
		teacherId: Joi.required().custom(objectId),
	}),
	body: Joi.object()
		.keys({
			name: Joi.string(),
			shortname: Joi.string(),
			description: Joi.string(),
		})
		.min(1),
};

const deleteTeacher = {
	params: Joi.object().keys({
		teacherId: Joi.string().custom(objectId),
	}),
};

module.exports = {
	createTeacher,
	getTeachers,
	getTeacher,
	updateTeacher,
	deleteTeacher,
};
