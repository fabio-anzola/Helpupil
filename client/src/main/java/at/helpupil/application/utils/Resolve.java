package at.helpupil.application.utils;

import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.Subject;
import at.helpupil.application.utils.responses.Teacher;
import at.helpupil.application.utils.responses.UserPublicObj;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import static at.helpupil.application.Application.BASE_URL;

public class Resolve {
    public static String resolveSubjectByShortname(String shortname) {
        HttpResponse<Subject> subject = Unirest.get(BASE_URL + "/subject")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .queryString("shortname", shortname)
                .asObject(Subject.class);

        Error error = subject.mapError(Error.class);

        if (null == error) {
            return subject.getBody().getId();
        } else {
            return shortname;
        }
    }

    public static String resolveTeacherByShortname(String shortname) {
        HttpResponse<Teacher> teacher = Unirest.get(BASE_URL + "/teacher")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .queryString("shortname", shortname)
                .asObject(Teacher.class);

        Error error = teacher.mapError(Error.class);

        if (null == error) {
            return teacher.getBody().getId();
        } else {
            return shortname;
        }
    }

    public static String resolveSubjectById(String id) {
        HttpResponse<Subject> subject = Unirest.get(BASE_URL + "/subject/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Subject.class);

        Error error = subject.mapError(Error.class);

        if (null == error) {
            return subject.getBody().getShortname();
        } else {
            return id;
        }
    }

    public static String resolveTeacherById(String id) {
        HttpResponse<Teacher> teacher = Unirest.get(BASE_URL + "/teacher/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Teacher.class);

        Error error = teacher.mapError(Error.class);

        if (null == error) {
            return teacher.getBody().getShortname();
        } else {
            return id;
        }
    }

    public static String resolveUserById(String id) {
        HttpResponse<UserPublicObj> user = Unirest.get(BASE_URL + "/users/public/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(UserPublicObj.class);

        Error error = user.mapError(Error.class);

        if (null == error) {
            return user.getBody().getName();
        } else {
            return id;
        }
    }
}
