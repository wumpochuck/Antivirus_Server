package ru.mtuci.antivirus.entities.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {

    @NotBlank(message = "Логин не может быть пустым")
    @Column(name = "login")
    private String login;

    @NotBlank(message = "Пароль не может быть пустым")
    @Column(name = "password")
    private String password;

    public UserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserDTO() {
    }

    public @NotBlank(message = "Логин не может быть пустым") String getLogin() {
        return login;
    }

    public void setLogin(@NotBlank(message = "Логин не может быть пустым") String login) {
        this.login = login;
    }

    public @NotBlank(message = "Пароль не может быть пустым") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Пароль не может быть пустым") String password) {
        this.password = password;
    }
}
