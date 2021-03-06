const nodemailer = require('nodemailer');
const config = require('../config/config');
const logger = require('../config/logger');

const transport = nodemailer.createTransport(config.email.smtp);
/* istanbul ignore next */
if (config.env !== 'test') {
  transport
    .verify()
    .then(() => logger.info('Connected to email server'))
    .catch(() => logger.warn('Unable to connect to email server. Make sure you have configured the SMTP options in .env'));
}

/**
 * Send an email
 * @param {string} to
 * @param {string} subject
 * @param {string} text
 * @returns {Promise}
 */
const sendEmail = async (to, subject, text) => {
  const msg = { from: {
      name: config.email.name,
      address: config.email.from,
    }, to, subject, text };
  await transport.sendMail(msg);
};

/**
 * Send reset password email
 * @param {string} to
 * @param {string} token
 * @returns {Promise}
 */
const sendResetPasswordEmail = async (to, name, token) => {
  const subject = 'Reset password';
  // replace this url with the link to the reset password page of your front-end app
  const resetPasswordUrl = `https://mail.helpupil.at/?type=reset&token=${token}`;
  const text = `Hi ${name},
To reset your password, click on this link: 
${resetPasswordUrl}

If you did not request a password reset, then ignore this email.
Your Helpupil Team`;
  await sendEmail(to, subject, text);
};

/**
 * Send verification email
 * @param {string} to
 * @param {string} token
 * @returns {Promise}
 */
const sendVerificationEmail = async (to, name, token) => {
  const subject = 'Email Verification';
  // replace this url with the link to the email verification page of your front-end app
  const verificationEmailUrl = `https://mail.helpupil.at/?type=verify&token=${token}`;
  const text = `Hi ${name},
To verify your email, click on this link: 
${verificationEmailUrl}

If you did not create an account, then ignore this email.
Your Helpupil Team`;
  await sendEmail(to, subject, text);
};

/**
 * Send approve email
 * @param {string} to
 * @param {string} name
 * @param {string} docName
 * @returns {Promise}
 */
 const sendApproveEmail = async (to, name, docName) => {
  const subject = 'Your Document has been approved!';
  const text = `Hi ${name}!
Congrats! ???? Your document with the name "${docName}" has been approved!

Thanks for contributing to the community!
Your Helpupil Team`;
  await sendEmail(to, subject, text);
};

/**
 * Send decline email
 * @param {string} to
 * @param {string} name
 * @param {string} docName
 * @param {string} message
 * @returns {Promise}
 */
 const sendDeclineEmail = async (to, name, docName, message) => {
  const subject = 'Your Document has been declined!';
  const text = `Hi ${name}!
Unfortunately we had to decline your document with the name "${docName}".
Here your message from the moderator:
"${message}"

Thanks for contributing to the community!
Your Helpupil Team`;
  await sendEmail(to, subject, text);
};

module.exports = {
  transport,
  sendEmail,
  sendResetPasswordEmail,
  sendVerificationEmail,
  sendApproveEmail,
  sendDeclineEmail,
};
