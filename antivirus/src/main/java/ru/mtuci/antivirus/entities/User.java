package ru.mtuci.antivirus.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import ru.mtuci.antivirus.entities.ENUMS.ROLE;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "Логин не может быть пустым")
    @Column(name = "login")
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ROLE role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<License> licenses;

    public User(int id, String login, String password, ROLE role, List<License> licenses) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.licenses = licenses;
    }

    public User(String login, String password, ROLE role, List<License> licenses) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.licenses = licenses;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public ROLE getRole() {
        return role;
    }

    public List<License> getLicenses() {
        return licenses;
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

    public void setRole(ROLE role) {
        this.role = role;
    }

    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }
}
