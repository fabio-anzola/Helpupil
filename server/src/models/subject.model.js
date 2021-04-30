const mongoose = require('mongoose');
const { toJSON, paginate } = require('./plugins');

const subjectSchema = mongoose.Schema(
	{
		name: {
			type: String,
			required: true,
			trim: true,
		},
		shortname: {
			type: String,
			required: true,
			trim: true,
		},
		description: {
			type: String,
			required: true,
			trim: true,
		},
		user: {
			type: mongoose.SchemaTypes.ObjectId,
			ref: 'User',
			required: true,
		},
	},
	{
		timestamps: true,
	}
);

// add plugin that converts mongoose to json
subjectSchema.plugin(toJSON);
subjectSchema.plugin(paginate);

/**
 * Check if name is taken
 * @param {string} name - The subjects's name
 * @param {ObjectId} [excludeSubjectId] - The id of the subject to be excluded
 * @returns {Promise<boolean>}
 */
subjectSchema.statics.isNameTaken = async function (name, excludeSubjectId) {
	const subject = await this.findOne({ name, _id: { $ne: excludeSubjectId } });
	return !!subject;
};

/**
 * Check if shortname is taken
 * @param {string} name - The subjects's shortname
 * @param {ObjectId} [excludeSubjectId] - The id of the subject to be excluded
 * @returns {Promise<boolean>}
 */
subjectSchema.statics.isShortnameTaken = async function (shortname, excludeSubjectId) {
	const subject = await this.findOne({ shortname, _id: { $ne: excludeSubjectId } });
	return !!subject;
};

/**
 * @typedef Subject
 */
const Subject = mongoose.model('Subject', subjectSchema);

module.exports = Subject;
