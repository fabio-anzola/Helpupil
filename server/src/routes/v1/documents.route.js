const express = require('express');
const httpStatus = require('http-status');
const auth = require('../../middlewares/auth');
const validate = require('../../middlewares/validate');
const documentValidation = require('../../validations/document.validation');
const { documentMimes } = require('../../config/documents');
const documentController = require('../../controllers/document.controller');

const router = express.Router();
const multer = require('multer');

const storage = multer.diskStorage({
  destination: function(req, file, cb) {
    cb(null, 'uploads');
  },
  filename: function(req, file, cb) {
    cb(null, file.originalname);
  }
});

const fileFilter = (req, file, cb) => {
  if (documentMimes.includes(file.mimetype)) {
  // if (file.mimetype === 'image/jpg' || file.mimetype === 'image/png' || file.mimetype === 'application/pdf') {
    cb(null, true);
  } else {
    cb(null, false);
  }
};

const upload = multer({
  fileFilter: fileFilter,
  storage: storage,
  limits: {
    fileSize: 1024 * 1024 * 200
  },
});

router
  .route('/')
  .post(auth(), upload.single('file'), validate(documentValidation.create), documentController.createDocument)
  .get(auth(), validate(documentValidation.get), documentController.getDocuments);

router
  .route('/:documentId')
  .get(auth(), validate(documentValidation.get), documentController.getDocument)
  .delete(auth(), documentController.deleteDocument);

module.exports = router;

/**
 * @swagger
 * tags:
 *   name: Documents
 *   description: Document management and retrieval
 */

/**
 * @swagger
 * /documents:
 *   post:
 *     summary: Create a document
 *     description: Only logged in users can create documents.
 *     tags: [Documents]
 *     security:
 *       - bearerAuth: []
 *     requestBody:
 *       required: true
 *       content:
 *         multipart/form-data:
 *           schema:
 *             type: object
 *             required:
 *               - file
 *               - name
 *               - type
 *               - subject
 *               - teacher
 *             properties:
 *               file:
 *                 type: string
 *                 format: binary
 *                 description: document
 *               name:
 *                 type: string
 *                 description: Name of document
 *               type:
 *                 type: string
 *                 enum: [homework, exam, test, revision, script]
 *                 description: Type of document
 *               subject:
 *                 type: string
 *                 description: ObjectId of subject
 *               teacher:
 *                 type: string
 *                 description: ObjectId of teacher
 *             example:
 *               file: <example.pdf>
 *               name: example_homework
 *               type: homework
 *               subject: 60833a0fdefaa30582041ea7
 *               teacher: 608c81ec640bb32f5ce13552
 *     responses:
 *       "201":
 *         description: Created
 *         content:
 *           application/json:
 *             schema:
 *                $ref: '#/components/schemas/Document'
 *       "400":
 *         $ref: '#/components/responses/FieldNotFilled'
 *       "401":
 *         $ref: '#/components/responses/Unauthorized'
 *
 *   get:
 *     summary: Get all documents
 *     description: Only logged in users can retrieve all documents.
 *     tags: [Documents]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: query
 *         name: name
 *         schema:
 *           type: string
 *         description: Document name
 *       - in: query
 *         name: type
 *         schema:
 *           type: string
 *           enum: [homework, exam, test, revision, script]
 *         description: Document type
 *       - in: query
 *         name: user
 *         schema:
 *           type: string
 *         description: user id of creator
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
 *         description: Maximum number of users
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
 *                     $ref: '#/components/schemas/Document'
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
 * /documents/{id}:
 *   get:
 *     summary: Get a document
 *     description: Logged in users can get a document.
 *     tags: [Documents]
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
 *         description: OK
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
 *
 *   delete:
 *     summary: Delete a document
 *     description: Only moderators and owners can delete a document.
 *     tags: [Documents]
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: User id
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
