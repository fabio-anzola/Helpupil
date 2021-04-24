const mongoose = require('mongoose');
const { toJSON, paginate } = require('./plugins');
const { documentTypes, statusTypes } = require('../config/documents');

const documentSchema = mongoose.Schema(
  {
    name: {
      type: String,
      required: true,
      trim: true,
    },
    type: {
      type: String,
      enum: [documentTypes.HOMEWORK, documentTypes.EXAM, documentTypes.TEST, documentTypes.REVISION, documentTypes.SCRIPT],
      required: true,
    },
    user: {
      type: mongoose.SchemaTypes.ObjectId,
      ref: 'User',
      required: true,
    },
    rating: {
      type: Number,
      required: true,
    },
    file: {
      type: Object,
      required: true,
    },
    status: {
      type: String,
      enum: [statusTypes.APPROVED, statusTypes.PENDING, statusTypes.DECLINED],
      required: true,
    },
    reviewer: {
      type: [
        mongoose.SchemaTypes.ObjectId
      ],
      ref: 'User',
      require: true,
    },
    subject: {
      type: mongoose.SchemaTypes.ObjectId,
      ref: 'Subject',
      required: true,
    }
  },
  {
    timestamps: true,
  }
);

// add plugin that converts mongoose to json
documentSchema.plugin(toJSON);
documentSchema.plugin(paginate);

/**
 * @typedef document
 */
const document = mongoose.model('Document', documentSchema);

module.exports = document;
