package at.helpupil.application.utils;

import at.helpupil.application.utils.responses.*;
import at.helpupil.application.utils.responses.Error;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import static at.helpupil.application.Application.BASE_URL;

/**
 * static methods to resolve objects by specific keys
 */
public class Resolve {
    /**
     * @param shortname of subject
     * @return subject resolved by shortname
     */
    public static String resolveSubjectByShortname(String shortname) {
        HttpResponse<Subjects> subject = Unirest.get(BASE_URL + "/subject")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .queryString("shortname", shortname)
                .asObject(Subjects.class);

        Error error = subject.mapError(Error.class);

        if (null == error) {
            return subject.getBody().getResults()[0].getId();
        } else {
            return shortname;
        }
    }

    /**
     * @param shortname of teacher
     * @return teacher resolved by shortname
     */
    public static String resolveTeacherByShortname(String shortname) {
        HttpResponse<Teachers> teacher = Unirest.get(BASE_URL + "/teacher")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .queryString("shortname", shortname)
                .asObject(Teachers.class);

        Error error = teacher.mapError(Error.class);

        if (null == error) {
            return teacher.getBody().getResults()[0].getId();
        } else {
            return shortname;
        }
    }

    /**
     * @param id of subject
     * @return subject resolved by id
     */
    public static Subject resolveSubjectById(String id) {
        HttpResponse<Subject> subject = Unirest.get(BASE_URL + "/subject/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Subject.class);

        Error error = subject.mapError(Error.class);

        if (null == error) {
            return subject.getBody();
        } else {
            return null;
        }
    }

    /**
     * @param id of teacher
     * @return teacher resolved by id
     */
    public static Teacher resolveTeacherById(String id) {
        HttpResponse<Teacher> teacher = Unirest.get(BASE_URL + "/teacher/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Teacher.class);

        Error error = teacher.mapError(Error.class);

        if (null == error) {
            return teacher.getBody();
        } else {
            return null;
        }
    }

    /**
     * @param id of document
     * @return document resolved by id
     */
    public static Document resolveDocumentById(String id) {
        HttpResponse<Document> document = Unirest.get(BASE_URL + "/documents/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Document.class);

        Error error = document.mapError(Error.class);

        if (null == error) {
            return document.getBody();
        } else {
            return null;
        }
    }
}
