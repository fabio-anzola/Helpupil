const Joi = require('joi');

const get = {
  params: Joi.object().keys({
    documentName: Joi.string().required(),
  }),
};

module.exports = {
  get,
};