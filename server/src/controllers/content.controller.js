const httpStatus = require('http-status');
const ApiError = require('../utils/ApiError');
const catchAsync = require('../utils/catchAsync');
const { documentService } = require('../services');

const downloadDocument = catchAsync(async (req, res) => {
  const file = "content/" + req.params.documentName;
  if (!file) {
    throw new ApiError(httpStatus.NOT_FOUND, 'File not found');
  }
  originalname = (await documentService.getDocumentByName(req.params.documentName)).file.originalname;
  res.download(file, originalname);
});

module.exports = {
  downloadDocument,
};