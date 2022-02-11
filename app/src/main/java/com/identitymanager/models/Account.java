package com.identitymanager.models;

public class Account {

    String id;
    String fkIdUser;
    String accountUsername;
    String accountEmail;
    String accountPassword;
    String accountLastUpdateDate;
    String fkIdCategory;

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

    public String getAccountUsername() {
        return accountUsername;
    }

    public void setAccountUsername(String accountUsername) {
        this.accountUsername = accountUsername;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getAccountLastUpdateDate() {
        return accountLastUpdateDate;
    }

    public void setAccountLastUpdateDate(String accountLastUpdateDate) {
        this.accountLastUpdateDate = accountLastUpdateDate;
    }

    public String getFkIdCategory() {
        return fkIdCategory;
    }

    public void setFkIdCategory(String fkIdCategory) {
        this.fkIdCategory = fkIdCategory;
    }
}
