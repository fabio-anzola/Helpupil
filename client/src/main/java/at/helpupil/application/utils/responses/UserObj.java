package at.helpupil.application.utils.responses;

import java.util.ArrayList;

class UserObj {
    private String role;
    private boolean isEmailVerified;
    private float wallet;
    ArrayList<Object> purchasedDocuments = new ArrayList<Object>();
    private String name;
    private String email;
    private String id;

    public UserObj(String role, boolean isEmailVerified, float wallet, ArrayList<Object> purchasedDocuments, String name, String email, String id) {
        this.role = role;
        this.isEmailVerified = isEmailVerified;
        this.wallet = wallet;
        this.purchasedDocuments = purchasedDocuments;
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public boolean getIsEmailVerified() {
        return isEmailVerified;
    }

    public float getWallet() {
        return wallet;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    // Setter Methods

    public void setRole(String role) {
        this.role = role;
    }

    public void setIsEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }
}
