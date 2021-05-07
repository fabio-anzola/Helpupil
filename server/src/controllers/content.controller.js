const httpStatus = require('http-status');
const ApiError = require('../utils/ApiError');
const catchAsync = require('../utils/catchAsync');
const { documentService, userService } = require('../services');
const { priceTypes } = require('../config/documents');

const downloadDocument = catchAsync(async (req, res) => {
  const file = "content/" + req.params.documentName;
  if (!file) {
    throw new ApiError(httpStatus.NOT_FOUND, 'File not found');
  }
  const document = await documentService.getDocumentByName(req.params.documentName);
  await userService.removeCoins(req.user._id, priceTypes[document.type.toUpperCase()]);
  originalname = document.file.originalname;
  res.download(file, originalname);
});

module.exports = {
  downloadDocument,
};