package at.helpupil.application.utils.responses;

public class Tokens {
    Access AccessObject;
    Refresh RefreshObject;


    // Getter Methods

    public Access getAccess() {
        return AccessObject;
    }

    public Refresh getRefresh() {
        return RefreshObject;
    }

    // Setter Methods

    public void setAccess(Access accessObject) {
        this.AccessObject = accessObject;
    }

    public void setRefresh(Refresh refreshObject) {
        this.RefreshObject = refreshObject;
    }
}
