const express = require('express');
const auth = require('../../middlewares/auth');
const validate = require('../../middlewares/validate');
const contentValidation = require('../../validations/content.validation');
const contentController = require('../../controllers/content.controller');

const router = express.Router();

router
	.route('/:documentName')
	.get(auth(), validate(contentValidation.get), contentController.downloadDocument);

module.exports = router;