const express = require('express');
const httpStatus = require('http-status');
const auth = require('../../middlewares/auth');
const validate = require('../../middlewares/validate');
const ratingValidation = require('../../validations/rating.validation');
const ratingController = require('../../controllers/rating.controller');

const router = express.Router();

router.route('/:documentId').patch(auth(), validate(ratingValidation.edit), ratingController.updateRating);

module.exports = router;


/**
 * @swagger
 * tags:
 *   name: Rating
 *   description: Rating management and retrieval
 */

/**
 * @swagger
 * /rating/{id}:
 *   patch:
 *     summary: Update document rating
 *     description: Only logged in users can rate (once).
 *     tags: [Rating]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: query
 *         name: rating
 *         required: true
 *         schema:
 *           type: integer
 *           minimum: 1
 *           maximum: 5
 *         description: Document rating (1-5)
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: Document id
 *     responses:
 *       "200":
 *         description: Rated
 *         content:
 *           application/json:
 *             schema:
 *                $ref: '#/components/schemas/Document'
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *       "403":
 *         $ref: '#/components/responses/Forbidden'
 */
