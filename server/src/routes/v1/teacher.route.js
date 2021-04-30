const express = require('express');
const auth = require('../../middlewares/auth');
const validate = require('../../middlewares/validate');
const teacherValidation = require('../../validations/teacher.validation');
const teacherController = require('../../controllers/teacher.controller');

const router = express.Router();

router
  .route('/')
  .post(auth(), validate(teacherValidation.createTeacher), teacherController.createTeacher)
  .get(auth(), validate(teacherValidation.getTeachers), teacherController.getTeachers);

router
  .route('/:teacherId')
  .get(auth(), validate(teacherValidation.getTeacher), teacherController.getTeacher)
  .patch(auth('manageTeacher'), validate(teacherValidation.updateTeacher), teacherController.updateTeacher)
  .delete(auth('manageTeacher'), validate(teacherValidation.deleteTeacher), teacherController.deleteTeacher);

module.exports = router;
