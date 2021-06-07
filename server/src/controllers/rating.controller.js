const httpStatus = require('http-status');
const ApiError = require('../utils/ApiError');
const catchAsync = require('../utils/catchAsync');
const { moderatorService, userService } = require('../services');


const updateRating = catchAsync(async (req, res) => {
	let document = await moderatorService.getDocumentById(req.params.documentId, req.body);
    if (!document) {
        throw new ApiError(httpStatus.NOT_FOUND, 'Document not found');
    }
    if (document.reviewer.includes(req.user._id)) {
        throw new ApiError(httpStatus.FORBIDDEN, 'You have already rated this document!');
    }
    const purchased = ( await userService.getUserById(req.user._id)).purchasedDocuments;
    if (!purchased.includes(req.params.documentId)) {
        throw new ApiError(httpStatus.FORBIDDEN, 'Please purchase this document first!');
    }

    req.body.rating = (document.rating * document.reviewer.length + req.query.rating) / (document.reviewer.length + 1);
    document.reviewer = document.reviewer.push(req.user._id);
    req.body.reviewer = document.reviewer;

    document = await moderatorService.updateDocumentById(req.params.documentId, req.body);
	res.send(document);
});

module.exports = {
	updateRating,
};