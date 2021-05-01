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

/**
 * @swagger
 * tags:
 *   name: Teacher
 *   description: Teacher management and retrieval
 */

/**
 * @swagger
 * /teacher:
 *   post:
 *     summary: Create a teacher
 *     description: Only logged in users can create a teacher.
 *     tags: [Teacher]
 *     security:
 *       - bearerAuth: []
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required:
 *               - name
 *               - shortname
 *               - description
 *             properties:
 *               name:
 *                 type: string
 *                 description: must be unique
 *               shortname:
 *                 type: string
 *                 description: must be unique
 *               description:
 *                 type: string
 *             example:
 *               name: Max Mustermann
 *               shortname: MUS
 *               description: Teacher MUS @ HTL3R
 *     responses:
 *       "201":
 *         description: Created
 *         content:
 *           application/json:
 *             schema:
 *                $ref: '#/components/schemas/Teacher'
 *       "400":
 *         $ref: '#/components/responses/DuplicateTeacher'
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 *
 *   get:
 *     summary: Get all teachers
 *     description: Only logged in users can retrieve all teachers.
 *     tags: [Teacher]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: query
 *         name: name
 *         schema:
 *           type: string
 *         description: Name
 *       - in: query
 *         name: shortname
 *         schema:
 *           type: string
 *         description: Shortname
 *       - in: query
 *         name: sortBy
 *         schema:
 *           type: string
 *         description: sort by query in the form of field:desc/asc (ex. name:asc)
 *       - in: query
 *         name: limit
 *         schema:
 *           type: integer
 *           minimum: 1
 *         default: 10
 *         description: Maximum number of teachers
 *       - in: query
 *         name: page
 *         schema:
 *           type: integer
 *           minimum: 1
 *           default: 1
 *         description: Page number
 *     responses:
 *       "200":
 *         description: OK
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 results:
 *                   type: array
 *                   items:
 *                     $ref: '#/components/schemas/Teacher'
 *                 page:
 *                   type: integer
 *                   example: 1
 *                 limit:
 *                   type: integer
 *                   example: 10
 *                 totalPages:
 *                   type: integer
 *                   example: 1
 *                 totalResults:
 *                   type: integer
 *                   example: 1
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 */

/**
 * @swagger
 * /teacher/{id}:
 *   get:
 *     summary: Get a teacher
 *     description: Logged in users can fetch all teachers
 *     tags: [Teacher]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: Teacher id
 *     responses:
 *       "200":
 *         description: OK
 *         content:
 *           application/json:
 *             schema:
 *                $ref: '#/components/schemas/Teacher'
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 *       "404":
 *         $ref: '#/components/responses/NotFound'
 *
 *   patch:
 *     summary: Update a teacher
 *     description: Moderators can use this route
 *     tags: [Teacher]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: Teacher id
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               name:
 *                 type: string
 *               shortname:
 *                 type: string
 *               description:
 *                 type: string
 *             example:
 *               name: Max Mustermann
 *               shortname: MUS
 *               description: Teacher MUS @ HTL3R
 *     responses:
 *       "200":
 *         description: OK
 *         content:
 *           application/json:
 *             schema:
 *                $ref: '#/components/schemas/Teacher'
 *       "400":
 *         $ref: '#/components/responses/DuplicateTeacher'
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 *       "404":
 *         $ref: '#/components/responses/NotFound'
 *
 *   delete:
 *     summary: Delete a teacher
 *     description: Only moderators can delete a teacher
 *     tags: [Teacher]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: Teacher id
 *     responses:
 *       "200":
 *         description: No content
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 *       "404":
 *         $ref: '#/components/responses/NotFound'
 */