const httpStatus = require('http-status');
const ApiError = require('../utils/ApiError');
const catchAsync = require('../utils/catchAsync');
const { documentService, userService } = require('../services');
const { priceTypes } = require('../config/documents');

const downloadDocument = catchAsync(async (req, res) => {
  const file = "content/" + req.params.documentName;
  const document = await documentService.getDocumentByName(req.params.documentName);
  if (document.status != "approved") {
    if (req.user.role != "admin" && req.user.role != "moderator") {
      throw new ApiError(httpStatus.UNAUTHORIZED, 'File not approved');
    }
  }
	const user = await userService.getUserById(req.user._id);
	if (!user.purchasedDocuments.includes(document._id)) {
    if (req.user.role != "admin" && req.user.role != "moderator") {
      await userService.removeCoins(user._id, priceTypes[document.type.toUpperCase()]);
      await userService.addCoins(document.user, 1);
    }
    user.purchasedDocuments.push(document._id);
    await userService.updateUserById(user._id, user);
	}
  originalname = document.file.originalname;
  res.download(file, originalname);
});

module.exports = {
  downloadDocument,
};
