const { version } = require('../../package.json');
const config = require('../config/config');

const swaggerDef = {
  openapi: '3.0.0',
  info: {
    title: 'Helpupil API documentation',
    version,
  },
  servers: [
    {
      url: `${config.base}v1`,
    },
  ],
};

module.exports = swaggerDef;
