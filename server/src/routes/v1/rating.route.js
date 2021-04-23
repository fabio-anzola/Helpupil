const express = require('express');
const httpStatus = require('http-status');
const auth = require('../../middlewares/auth');
const validate = require('../../middlewares/validate');
const ratingValidation = require('../../validations/rating.validation');
const ratingController = require('../../controllers/rating.controller');

const router = express.Router();

router.route('/:documentId').patch(auth(), validate(ratingValidation.edit), ratingController.updateRating);

module.exports = router;