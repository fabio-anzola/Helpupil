const express = require('express');
const authRoute = require('./auth.route');
const userRoute = require('./user.route');
const documentRoute = require('./documents.route');
const moderatorRoute = require('./moderator.route');
const ratingRoute = require('./rating.route');
const subjectRoute = require('./subject.route');
const teacherRoute = require('./teacher.route');
const docsRoute = require('./docs.route');
const config = require('../../config/config');

const router = express.Router();

const defaultRoutes = [
  {
    path: '/auth',
    route: authRoute,
  },
  {
    path: '/users',
    route: userRoute,
  },
  {
    path: '/documents',
    route: documentRoute,
  },
  {
    path: '/mod',
    route: moderatorRoute,
  },
  {
    path: '/rating',
    route: ratingRoute,
  },
  {
    path: '/subject',
    route: subjectRoute,
  },
  {
    path: '/teacher',
    route: teacherRoute,
  }
];

const devRoutes = [
  // routes available only in development mode
  {
    path: '/docs',
    route: docsRoute,
  },
];

defaultRoutes.forEach((route) => {
  router.use(route.path, route.route);
});

/* istanbul ignore next */
if (config.env === 'development') {
  devRoutes.forEach((route) => {
    router.use(route.path, route.route);
  });
}

module.exports = router;
