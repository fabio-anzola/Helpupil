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
	.get(auth(), validate(subjectValidation.getSubject), subjectController.getSubject)
	.patch(auth('manageSubjects'), validate(subjectValidation.updateSubject), subjectController.updateSubject)
	.delete(auth('manageSubjects'), validate(subjectValidation.deleteSubject), subjectController.deleteSubject);

module.exports = router;

/**
 * @swagger
 * tags:
 *   name: Subject
 *   description: Subject management and retrieval
 */

/**
 * @swagger
 * /subject:
 *   post:
 *     summary: Create a subject
 *     description: Only logged in users can create a subject.
 *     tags: [Subject]
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
 *               name: Math
 *               shortname: M
 *               description: Subject of Math
 *     responses:
 *       "201":
 *         description: Created
 *         content:
 *           application/json:
 *             schema:
 *                $ref: '#/components/schemas/Subject'
 *       "400":
 *         $ref: '#/components/responses/DuplicateSubject'
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 *
 *   get:
 *     summary: Get all subjects
 *     description: Only logged in users can retrieve all subjects.
 *     tags: [Subject]
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
 *         description: Maximum number of subjects
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
 *                     $ref: '#/components/schemas/Subject'
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
 * /subject/{id}:
 *   get:
 *     summary: Get a subject
 *     description: Logged in users can fetch all subjects
 *     tags: [Subject]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: Subject id
 *     responses:
 *       "200":
 *         description: OK
 *         content:
 *           application/json:
 *             schema:
 *                $ref: '#/components/schemas/Subject'
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 *       "404":
 *         $ref: '#/components/responses/NotFound'
 *
 *   patch:
 *     summary: Update a subject
 *     description: Moderators can use this route
 *     tags: [Subject]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: Subject id
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
 *               name: Applied Math
 *               shortname: AM
 *               description: Subject for applied math
 *     responses:
 *       "200":
 *         description: OK
 *         content:
 *           application/json:
 *             schema:
 *                $ref: '#/components/schemas/Subject'
 *       "400":
 *         $ref: '#/components/responses/DuplicateSubject'
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 *       "404":
 *         $ref: '#/components/responses/NotFound'
 *
 *   delete:
 *     summary: Delete a subject
 *     description: Only moderators can delete a subject
 *     tags: [Subject]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: Subject id
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