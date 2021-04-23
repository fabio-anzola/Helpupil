const Joi = require('joi');
const { objectId } = require('./custom.validation');

const edit = {
  params: Joi.object().keys({
    documentId: Joi.string().custom(objectId),
   }),
	query: Joi.object().keys({
		rating: Joi.number().integer().min(1).max(5).required(),
	}),
};


module.exports = {
  edit,
};