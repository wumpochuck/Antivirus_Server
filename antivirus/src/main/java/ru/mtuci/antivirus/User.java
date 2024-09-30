package ru.mtuci.antivirus;

// @Entity
// @Table(name = "USERS")
public class User {
    private int id;
    private String login;
    private String password;
    private String role;
    private String licence;

    // Constructors

    public User() {
    }

    public User(int id, String login, String password, String role, String licence) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.licence = licence;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getLicence() {
        return licence;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    // Methods

}
