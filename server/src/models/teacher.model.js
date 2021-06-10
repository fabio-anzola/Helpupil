const mongoose = require('mongoose');
const { toJSON, paginate } = require('./plugins');

const teacherSchema = mongoose.Schema(
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
teacherSchema.plugin(toJSON);
teacherSchema.plugin(paginate);

/**
 * Check if name is taken
 * @param {string} name - The teachers name
 * @param {ObjectId} [excludeTeacherId] - The id of the teacher to be excluded
 * @returns {Promise<boolean>}
 */
teacherSchema.statics.isNameTaken = async function (name, excludeTeacherId) {
	const teacher = await this.findOne({ name, _id: { $ne: excludeTeacherId } });
	return !!teacher;
};

/**
 * Check if shortname is taken
 * @param {string} name - The teachers shortname
 * @param {ObjectId} [excludeTeacherId] - The id of the teacher to be excluded
 * @returns {Promise<boolean>}
 */
teacherSchema.statics.isShortnameTaken = async function (shortname, excludeTeacherId) {
	const teacher = await this.findOne({ shortname, _id: { $ne: excludeTeacherId } });
	return !!teacher;
};

/**
 * @typedef Teacher
 */
const Teacher = mongoose.model('Teacher', teacherSchema);

module.exports = Teacher;
