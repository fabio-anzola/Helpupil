const Joi = require('joi');
const { documentTypes, documentMimes } = require('../config/documents');
const { objectId } = require('./custom.validation');

const createFile = {
	body: Joi.object().keys({
		name: Joi.string().required(),
		type: Joi.string().valid(...Object.values(documentTypes)).required(),
		subject: Joi.string().custom(objectId).required(),
		teacher: Joi.string().custom(objectId).required(),
	}),
	file: Joi.object({
		fieldname: Joi.string().required(),
		originalname: Joi.string().required(),
		encoding: Joi.string().required(),
		mimetype: Joi.string().valid(...documentMimes),
		destination: Joi.string().required(),
		filename: Joi.string().required(),
		path: Joi.string().required(),
		size: Joi.number().required(),
	}).required(),
};

const createBase64 = {
	body: Joi.object().keys({
		name: Joi.string().required(),
		type: Joi.string().valid(...Object.values(documentTypes)).required(),
		subject: Joi.string().custom(objectId).required(),
		teacher: Joi.string().custom(objectId).required(),
	}),
	file: Joi.object({
		fieldname: Joi.string().required(),
		originalname: Joi.string().required(),
		encoding: Joi.string().required(),
		mimetype: Joi.string().required(),
		buffer: Joi.object().required(),
		size: Joi.number().required(),
	}).required(),
};

const get = {
	query: Joi.object().keys({
		name: Joi.string(),
		type: Joi.string().valid(...Object.values(documentTypes)),
		user: Joi.string().custom(objectId),
		teacher: Joi.string().custom(objectId),
		subject: Joi.string().custom(objectId),
		sortBy: Joi.string(),
		limit: Joi.number().integer(),
		page: Joi.number().integer(),
	  }),
};

module.exports = {
  createFile,
	createBase64,
  get,
};