const express = require('express');
const auth = require('../../middlewares/auth');
const validate = require('../../middlewares/validate');
const subjectValidation = require('../../validations/subject.validation');
const subjectController = require('../../controllers/subject.controller');

const router = express.Router();

router
  .route('/')
  .post(auth(), validate(subjectValidation.createSubject), subjectController.createSubject)
  .get(auth(), validate(subjectValidation.getSubjects), subjectController.getSubjects);

router
  .route('/:subjectId')
  .get(auth(), validate(subjectValidation.getSubject), userController.getSubject)
  .patch(auth('manageSubjects'), validate(subjectValidation.updateSubject), subjectController.updateSubject)
  .delete(auth('manageSubjects'), validate(subjectValidation.deleteSubject), subjectController.deleteSubject);

module.exports = router;