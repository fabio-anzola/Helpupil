const Joi = require('joi');
const { documentTypes, documentMimes } = require('../config/documents');

const create = {
	body: Joi.object().keys({
		name: Joi.string().required(),
		type: Joi.string().valid(...Object.values(documentTypes)).required(),
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

module.exports = {
  create,
};