package at.helpupil.application.utils.responses;

import java.util.ArrayList;

/**
 * Response object of user without tokens
 */
public class UserObj {
    /**
     * role of user
     */
    private String role;
    /**
     * true if user has verified his email
     */
    private boolean isEmailVerified;
    /**
     * wallet of user
     */
    private int wallet;
    /**
     * list of purchased documents
     */
    private ArrayList<Object> purchasedDocuments;
    /**
     * name of user
     */
    private String name;
    /**
     * email of user
     */
    private String email;
    /**
     * id of user
     */
    private String id;

    /**
     * @param role of user
     * @param isEmailVerified true if user has verified his email
     * @param wallet of user
     * @param purchasedDocuments list of purchased documents
     * @param name of user
     * @param email of user
     * @param id of user
     */
    public UserObj(String role, boolean isEmailVerified, int wallet, ArrayList<Object> purchasedDocuments, String name, String email, String id) {
        this.role = role;
        this.isEmailVerified = isEmailVerified;
        this.wallet = wallet;
        this.purchasedDocuments = purchasedDocuments;
        this.name = name;
        this.email = email;
        this.id = id;
    }

    /**
     * @return role of user
     */
    public String getRole() {
        return role;
    }

    /**
     * @return true if user has verified his email
     */
    public boolean getIsEmailVerified() {
        return isEmailVerified;
    }

    /**
     * @return wallet of user
     */
    public int getWallet() {
        return wallet;
    }

    /**
     * @return name of user
     */
    public String getName() {
        return name;
    }

    /**
     * @return email of user
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return id of user
     */
    public String getId() {
        return id;
    }

    /**
     * @return list of purchased documents
     */
    public ArrayList<Object> getPurchasedDocuments() {
        return purchasedDocuments;
    }

    // Setter Methods

    /**
     * @param role of user
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @param isEmailVerified true if user has verified his email
     */
    public void setIsEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    /**
     * @param wallet of user
     */
    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    /**
     * @param name of user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param email of user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param id of user
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param purchasedDocuments list of purchased documents
     */
    public void setPurchasedDocuments(ArrayList<Object> purchasedDocuments) {
        this.purchasedDocuments = purchasedDocuments;
    }
}
