const Joi = require('joi');
const { documentTypes } = require('../config/documents');
const { objectId } = require('./custom.validation');

const edit = {
    params: Joi.object().keys({
      documentId: Joi.string().custom(objectId),
    }),
		query: Joi.object().keys({
			message: Joi.string().allow(null, ''),
		})
};

const get = {
	query: Joi.object().keys({
		name: Joi.string(),
		type: Joi.string().valid(...Object.values(documentTypes)),
		document: Joi.string().custom(objectId),
		sortBy: Joi.string(),
		limit: Joi.number().integer(),
		page: Joi.number().integer(),
	  }),
};

module.exports = {
  edit,
  get,
};