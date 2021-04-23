const documentTypes = {
  HOMEWORK: 'homework',
  EXAM: 'exam',
  TEST: 'test',
  REVISION: 'revision',
  SCRIPT: 'script',
};

const documentMimes = ['image/jpg', 'image/png', 'application/pdf'];

const statusTypes = {
  APPROVED: 'approved',
  WAITING: 'waiting',
  DECLINED: 'declined',
};

module.exports = {
  documentTypes,
  documentMimes,
  statusTypes,
};
