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

/**
 * @swagger
 * tags:
 *   name: Content
 *   description: Document download
 */

/**
 * @swagger
 * /content/{filename}:
 *   get:
 *     summary: Download document
 *     description: Logged in users can download a document.
 *     produces:
 *       - application/pdf
 *     tags: [Content]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: filename
 *         required: true
 *         schema:
 *           type: string
 *         description: Document filename
 *     responses:
 *       "200":
 *         description: A pdf file
 *         schema:
 *           type: file
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 *       "404":
 *         $ref: '#/components/responses/NotFound'
 *       "404":
 *         $ref: '#/components/responses/Credits'
 */