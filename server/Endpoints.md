#  Helpupil API Endpoints

## Auth Routes

- [x] POST /v1/auth/register
- [x] POST /v1/auth/login - login
- [x] POST /v1/auth/refresh-tokens - refresh auth tokens
- [x] POST /v1/auth/forgot-password - send reset password email
- [x] POST /v1/auth/reset-password - reset password
- [x] POST /v1/auth/send-verification-email - send verification email
- [x] GET /v1/auth/verify-email

## User Routes

- [x] POST /v1/users - create a user
- [x] GET /v1/users - get all users
- [x] GET /v1/users/:userId - get user
- [x] PATCH /v1/users/:userId - update user
- [x] DELETE /v1/users/:userId - delete user
- [x] GET /v1/users/public/:userId - get name of user

## Subject Routes

- [x] POST /v1/subject - create a subject
- [x] GET /v1/subject - get all subjects
- [x] GET /v1/subject/:subjectId - get subject
- [x] PATCH /v1/subject/:subjectId - update subject
- [x] DELETE /v1/subject/:subjectId - delete subject

## Teacher Routes

- [x] POST /v1/teacher - create a teacher
- [x] GET /v1/teacher - get all teachers
- [x] GET /v1/teacher/:teacherId - get teacher
- [x] PATCH /v1/teacher/:teacherId - update teacher
- [x] DELETE /v1/teacher/:teacherId - delete teacher

## Document Routes

- [x] POST /v1/documents - create a document
- [x] GET /v1/documents - get all documents
- [x] GET /v1/documents/:documentId - get document
- [x] DELETE /v1/documents/:documentId - delete a document

## Content Routes

- [x] GET /v1/content/:documentName - download a document

## Rating Routes

- [x] PATCH /v1/rating/:documentId - update document rating

## Moderator Routes

- [x] GET /v1/mod/pending - get all pending documents
- [x] PATCH /v1/mod/approve/:documentId - approve document
- [x] PATCH /v1/mod/decline/:documentId - approve document
