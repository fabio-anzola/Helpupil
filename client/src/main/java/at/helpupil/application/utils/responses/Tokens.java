package at.helpupil.application.utils.responses;

/**
 * Response objects of tokens
 */
public class Tokens {
    /**
     * access token to get access to helpupil
     */
    private Access access;
    /**
     * token to refresh access token
     */
    private Refresh refresh;

    /**
     * @param accessObject to access helpupil
     * @param refreshObject to refresh accessObject
     */
    public Tokens(Access accessObject, Refresh refreshObject) {
        access = accessObject;
        refresh = refreshObject;
    }

    /**
     * @return accessObject
     */
    public Access getAccess() {
        return access;
    }

    /**
     * @return refreshObject
     */
    public Refresh getRefresh() {
        return refresh;
    }

    /**
     * @param accessObject to access helpupil
     */
    public void setAccess(Access accessObject) {
        this.access = accessObject;
    }

    /**
     * @param refreshObject to refresh accessObject
     */
    public void setRefresh(Refresh refreshObject) {
        this.refresh = refreshObject;
    }
}
