package com.identitymanager.models.data;


import java.util.Date;

public class User {

    private String id;
    private String username;
    private String name;
    private String surname;
    private String birthDate;
    private String email;
    private String phone;
    private String country;

    public String getId() { return  id; }

    public void setId (String id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getBirthDate() { return birthDate; }

    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }
}
