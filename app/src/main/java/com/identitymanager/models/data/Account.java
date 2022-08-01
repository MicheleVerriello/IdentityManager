package com.identitymanager.models.data;

public class Account {

    private String id;
    private String fkIdUser;
    private String accountName;
    private String accountUsername;
    private String accountEmail;
    private String accountPassword;
    private boolean twoFactorAuthentication;
    private String fkIdCategory;
    private String accountLastUpdateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFkIdUser() {
        return fkIdUser;
    }

    public void setFkIdUser(String fkIdUser) {
        this.fkIdUser = fkIdUser;
    }

    public String getAccountName() { return accountName; }

    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getAccountUsername() {
        return accountUsername;
    }

    public void setAccountUsername(String accountUsername) { this.accountUsername = accountUsername; }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) { this.accountPassword = accountPassword; }

    public boolean getFkId2FA() { return twoFactorAuthentication; }

    public void setFkId2FA(boolean fkId2FA) { this.twoFactorAuthentication = fkId2FA; }

    public String getFkIdCategory() {
        return fkIdCategory;
    }

    public void setFkIdCategory(String fkIdCategory) {
        this.fkIdCategory = fkIdCategory;
    }

    public String getAccountLastUpdateDate() {
        return accountLastUpdateDate;
    }

    public void setAccountLastUpdateDate(String accountLastUpdateDate) { this.accountLastUpdateDate = accountLastUpdateDate; }
}
