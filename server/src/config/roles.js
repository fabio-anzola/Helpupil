const roles = ['user', 'admin', 'moderator'];

const roleRights = new Map();
roleRights.set(roles[0], []);
roleRights.set(roles[1], ['getUsers', 'manageUsers']);
roleRights.set(roles[2], ['getPendingDoc', 'manageDocs']);

module.exports = {
  roles,
  roleRights,
};
