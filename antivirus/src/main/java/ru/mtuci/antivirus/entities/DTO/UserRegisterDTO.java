package ru.mtuci.antivirus.entities.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {

    @NotBlank(message = "login cannot be empty")
    private String login;

    @NotBlank(message = "password cannot be empty")
    private String password;

    @NotBlank(message = "email cannot be empty")
    @Email(message = "email should be valid")
    private String email;

    @Override
    public String toString() {
        return "UserRegisterDTO{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
