package ru.mtuci.antivirus.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.DTO.TokenResponse;
import ru.mtuci.antivirus.entities.DTO.UserLoginDTO;
import ru.mtuci.antivirus.entities.DTO.UserRegisterDTO;
import ru.mtuci.antivirus.entities.ENUMS.ROLE;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.entities.UserSession;
import ru.mtuci.antivirus.services.UserService;
import ru.mtuci.antivirus.utils.JwtUtil;

import java.util.Objects;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> userRegistration(@Valid @RequestBody UserRegisterDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body("Ошибка валидации: " + errMsg);
        }

        try{
            // Регистрация и создание сессии
            UserSession session = userService.registerUser(userDTO);

            return ResponseEntity.status(200).body(new TokenResponse(session.getAccessToken(), session.getRefreshToken()));
        } catch (Exception e){
            System.out.println("Ошибка при регистрации пользователя: " + e.getMessage());
            return ResponseEntity.status(400).body(
                    new TokenResponse(null, null, "Ошибка при регистрации: " + e.getMessage())
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody UserLoginDTO userDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            String errMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(200).body("Ошибка валидации: " + errMsg);
        }

        try{
            UserSession session = userService.loginUser(userDTO);
            return ResponseEntity.status(200).body(new TokenResponse(session.getAccessToken(), session.getRefreshToken()));
        } catch (Exception e){
            System.out.println("Ошибка при логине пользователя: " + e.getMessage());
            return ResponseEntity.status(400).body(
                    new TokenResponse(null, null, "Ошибка при логине: " + e.getMessage())
            );
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization") String authBearer){
        try{
            userService.logoutUser(authBearer);

            return ResponseEntity.status(200).body("Успешный выход из системы");
        } catch (Exception e){
            System.out.println("Ошибка при выходе из системы: " + e.getMessage());
            return ResponseEntity.status(400).body("Ошибка при выходе: " + e.getMessage());
        }
    }
}