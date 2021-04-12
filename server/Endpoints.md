#  Helpupil API Endpoints

## Auth Routes

[ ] POST /v1/auth/register  
[ ] POST /v1/auth/login - login  
[ ] POST /v1/auth/refresh-tokens - refresh auth tokens  
[ ] POST /v1/auth/forgot-password - send reset password email  
[ ] POST /v1/auth/reset-password - reset password  
[ ] POST /v1/auth/send-verification-email - send verification email  
[ ] POST /v1/auth/verify-email  

## User Routes

[ ] POST /v1/users - create a user  
[ ] GET /v1/users - get all users  
[ ] GET /v1/users/:userId - get user  
[ ] PATCH /v1/users/:userId - update user  
[ ] DELETE /v1/users/:userId - delete user  

## Subject Routes

[ ] POST /v1/subjects - create a subject  
[ ] GET /v1/subjects - get all subjects  
[ ] GET /v1/subjects/:subjectId - get subject  
[ ] PATCH /v1/subjects/:subjectId - update subject  
[ ] DELETE /v1/subjects/:subjectId - delete subject  

## Teacher Routes

[ ] POST /v1/teacher - create a teacher  
[ ] GET /v1/teacher - get all teachers  
[ ] GET /v1/teacher/:teacherId - get teacher  
[ ] PATCH /v1/teacher/:teacherId - update teacher  
[ ] DELETE /v1/teacher/:teacherId - delete teacher  

## Document Routes

[ ] POST /v1/documents/:documentId - create a document  
[ ] GET /v1/documents - get all documents  
[ ] GET /v1/documents/:documentId - get document  
[ ] DELETE /v1/documents/:documentId - delete a document  

## Rating Routes

[ ] GET /v1/rating/:documentId - get document rating  
[ ] PATCH /v1/rating/:documentId - update document rating  

## Moderator Routes

[ ] GET /v1/mod/pending - get all pending documents  
[ ] PATCH /v1/mod/approve/:documentId - approve document  
[ ] PATCH /v1/mod/decline/:documentId - approve document  