const roles = ['user', 'moderator', 'admin'];

const roleRights = new Map();
roleRights.set(roles[0], []);
roleRights.set(roles[1], ['getPendingDoc', 'manageDocs', 'manageSubjects', 'manageTeacher']);
roleRights.set(roles[2], roleRights.get(roles[1]).concat(['getUsers', 'manageUsers']));

module.exports = {
  roles,
  roleRights,
};
