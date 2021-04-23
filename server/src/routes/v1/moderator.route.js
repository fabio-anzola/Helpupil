const express = require('express');
const httpStatus = require('http-status');
const auth = require('../../middlewares/auth');
const validate = require('../../middlewares/validate');
const moderatorValidation = require('../../validations/moderator.validation');
const moderatorController = require('../../controllers/moderator.controller');

const router = express.Router();

router.route('/pending').get(auth('getPendingDoc'), validate(moderatorValidation.get), moderatorController.getDocuments);
router.route('/approve/:documentId').patch(auth('manageDocs'), validate(moderatorValidation.edit), moderatorController.approve);
router.route('/decline/:documentId').patch(auth('manageDocs'), validate(moderatorValidation.edit), moderatorController.decline);

module.exports = router;