const catchAsync = require('../utils/catchAsync');
const { moderatorService } = require('../services');


const updateRating = catchAsync(async (req, res) => {
	let document = await moderatorService.getDocumentById(req.params.documentId, req.body);

    req.body.rating = (document.rating * document.reviewer.length + req.query.rating) / (document.reviewer.length + 1);
    document.reviewer = document.reviewer.push(req.user._id);
    req.body.reviewer = document.reviewer;

    document = await moderatorService.updateDocumentById(req.params.documentId, req.body);
	res.send(document);
});

module.exports = {
	updateRating,
};