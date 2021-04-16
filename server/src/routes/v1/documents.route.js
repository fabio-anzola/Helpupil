const express = require('express');
const httpStatus = require('http-status');
const auth = require('../../middlewares/auth');
const validate = require('../../middlewares/validate');
const { Doc } = require('../../models');
const catchAsync = require('../../utils/catchAsync');

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
  if (file.mimetype === 'image/jpg' || file.mimetype === 'image/png' || file.mimetype === 'application/pdf') {
    cb(null, true);
  } else {
    cb(null, false);
  }
};

const upload = multer({
  storage: storage,
  limits: {
    fileSize: 1024 * 1024 * 200
  },
  fileFilter: fileFilter
});

router
  .route('/')
  .post(auth(), upload.single('document'), (req, res) => {

    Doc.create({name: 'test', type: 'homework', rating: 0, file: {}});
    //const resu = documentf.create(req.file);

    res.status(httpStatus.CREATED).send( req.file );
  });

module.exports = router;
