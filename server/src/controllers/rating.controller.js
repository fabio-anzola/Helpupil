const catchAsync = require('../utils/catchAsync');
const { moderatorService } = require('../services');


const updateRating = catchAsync(async (req, res) => {
	let document = await moderatorService.getDocumentById(req.params.documentId, req.body);

    document.reviewer = document.reviewer.push(req.user._id);
    req.body.rating = (document.rating + req.query.rating) / (document.reviewer.length);
    req.body.reviewer = document.reviewer;

    document = await moderatorService.updateDocumentById(req.params.documentId, req.body);
	res.send(document);
});

module.exports = {
	updateRating,
};