package ru.mtuci.antivirus.entities.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "Login cannot be empty")
    private String login;

    @NotBlank(message = "passwordHash cannot be empty")
    private String passwordHash;

    @NotBlank(message = "Email cannot be empty")
    private String email;

    @Override
    public String toString() {
        return "UserRequest{" +
                "login='" + login + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}