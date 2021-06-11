const express = require('express');
const httpStatus = require('http-status');
const auth = require('../../middlewares/auth');
const emailService = require('./email.service');
const validate = require('../../middlewares/validate');
const moderatorValidation = require('../../validations/moderator.validation');
const moderatorController = require('../../controllers/moderator.controller');

const router = express.Router();

router.route('/pending').get(auth('getPendingDoc'), validate(moderatorValidation.get), moderatorController.getDocuments);
router.route('/approve/:documentId').patch(auth('manageDocs'), validate(moderatorValidation.edit), moderatorController.approve);
router.route('/decline/:documentId').patch(auth('manageDocs'), validate(moderatorValidation.edit), moderatorController.decline);

module.exports = router;


/**
 * @swagger
 * tags:
 *   name: Moderator
 *   description: Moderator document management
 */

/**
 * @swagger
 * /mod/pending:
 *   patch:
 *     summary: List all pending documents
 *     description: Only moderators can list pending documents.
 *     tags: [Moderator]
 *     security:
 *       - bearerAuth: []
 *     responses:
 *       "200":
 *         description: OK
 *         content:
 *           application/json:
 *             schema:
 *                $ref: '#/components/schemas/Document'
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 */

/**
 * @swagger
 * /mod/approve/{id}:
 *   get:
 *     summary: Approve a document
 *     description: Only moderators can approve a document.
 *     tags: [Moderator]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: Document id
 *     responses:
 *       "200":
 *         description: Approved
 *         content:
 *           application/json:
 *             schema:
 *                $ref: '#/components/schemas/Document'
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 *       "404":
 *         $ref: '#/components/responses/NotFound'
 */

/**
 * @swagger
 * /mod/decline/{id}:
 *   get:
 *     summary: Decline a document
 *     description: Only moderators can decline a document.
 *     tags: [Moderator]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: Document id
 *     responses:
 *       "200":
 *         description: Declined
 *         content:
 *           application/json:
 *             schema:
 *                $ref: '#/components/schemas/Document'
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 *       "404":
 *         $ref: '#/components/responses/NotFound'
 */
