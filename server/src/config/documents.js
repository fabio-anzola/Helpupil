const documentTypes = {
  HOMEWORK: 'homework',
  EXAM: 'exam',
  TEST: 'test',
  REVISION: 'revision',
  SCRIPT: 'script',
};

const priceTypes = {
  HOMEWORK: 5,
  EXAM: 6,
  TEST: 7,
  REVISION: 8,
  SCRIPT: 9,
};

const documentMimes = ['image/jpg', 'image/png', 'application/pdf'];

const statusTypes = {
  APPROVED: 'approved',
  PENDING: 'pending',
  DECLINED: 'declined',
};

module.exports = {
  documentTypes,
  priceTypes,
  documentMimes,
  statusTypes,
};
