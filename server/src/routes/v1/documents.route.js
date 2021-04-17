const express = require('express');
const httpStatus = require('http-status');
const auth = require('../../middlewares/auth');
const validate = require('../../middlewares/validate');
const documentValidation = require('../../validations/document.validation');
const { Doc } = require('../../models');
const { documentMimes } = require('../../config/documents');

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
  .post(auth(), upload.single('file'), validate(documentValidation.create),  (req, res) => {

    const obj = {name: req.body.name, type: req.body.type, user: req.user._id, rating: 0, file: req.file};

    Doc.create(obj);

    res.status(httpStatus.CREATED).send(obj);
  });

module.exports = router;
