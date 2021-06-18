const httpStatus = require('http-status');
const { User } = require('../models');
const ApiError = require('../utils/ApiError');
const { Doc } = require('../models');
const { priceTypes } = require('../config/documents');
const teacherService = require('./teacher.service.js');
const subjectService = require('./subject.service.js');
const userService  = require('./user.service.js');

/**
 * Create a document in db
 * @param {Object} docuemntBody
 * @returns {Promise<Document>}
 */
const createDoc = async (documentBody) => {
  if (!documentBody) {
    throw new ApiError(httpStatus.BAD_REQUEST, 'Something is wrong');
  }
  const document = await Doc.create(documentBody);
  return document;
};

/**
 * Query for documents
 * @param {Object} filter - Mongo filter
 * @param {Object} options - Query options
 * @param {string} [options.sortBy] - Sort option in the format: sortField:(desc|asc)
 * @param {number} [options.limit] - Maximum number of results per page (default = 10)
 * @param {number} [options.page] - Current page (default = 1)
 * @returns {Promise<QueryResult>}
 */
const queryDocuments = async (filter, options) => {
  var documents = (await Doc.paginate(filter, options));
  for (let i = 0; i < documents.results.length; i++) {
    const element = (await documents.results[i]).toObject();
    element.price = priceTypes[element.type.toUpperCase()];
    element.teacher_sn = (await teacherService.getTeacherById(element.teacher)).shortname;
    element.subject_sn = (await subjectService.getSubjectById(element.subject)).shortname;
    element.uname = (await userService.getUserById(element.user)).name;
    documents.results[i] = element;
  }
  return documents;
};

/**
 * Get document by id
 * @param {ObjectId} id
 * @returns {Promise<Document>}
 */
const getDocumentById = async (id) => {
  const document = (await Doc.findById(id)).toObject();
  document.price = priceTypes[document.type.toUpperCase()];
  document.teacher_sn = (await teacherService.getTeacherById(document.teacher)).shortname;
  document.subject_sn = (await subjectService.getSubjectById(document.subject)).shortname;
  document.uname = (await userService.getUserById(document.user)).name;
  return document;
};

/**
 * Get document by name
 * @param {ObjectId} name
 * @returns {Promise<Document>}
 */
const getDocumentByName = async (name) => {
  let document = (await Doc.findOne({
    "file.filename": name
  }));
  if (!document) {
    throw new ApiError(httpStatus.NOT_FOUND, 'File not found');
  }
  document = document.toObject();
  document.price = priceTypes[document.type.toUpperCase()];
  document.teacher_sn = (await teacherService.getTeacherById(document.teacher)).shortname;
  document.subject_sn = (await subjectService.getSubjectById(document.subject)).shortname;
  document.uname = (await userService.getUserById(document.user)).name;
  return document;
};


/**
 * Delete document by id
 * @param {ObjectId} documentId
 * @returns {Promise<Document>}
 */
const deleteDocumentById = async (documentId) => {
  const document = await getDocumentById(documentId);
  if (!document) {
    throw new ApiError(httpStatus.NOT_FOUND, 'Document not found');
  }
  await Doc.deleteOne({ "_id": documentId });
  return document;
};

module.exports = {
  createDoc,
  queryDocuments,
  getDocumentById,
  deleteDocumentById,
  getDocumentByName,
};
