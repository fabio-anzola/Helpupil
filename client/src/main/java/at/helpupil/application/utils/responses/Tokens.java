package at.helpupil.application.utils.responses;

public class Tokens {
    private Access access;
    private Refresh refresh;

    public Tokens(Access accessObject, Refresh refreshObject) {
        access = accessObject;
        refresh = refreshObject;
    }

    public Access getAccess() {
        return access;
    }

    public Refresh getRefresh() {
        return refresh;
    }

    public void setAccess(Access accessObject) {
        this.access = accessObject;
    }

    public void setRefresh(Refresh refreshObject) {
        this.refresh = refreshObject;
    }
}
