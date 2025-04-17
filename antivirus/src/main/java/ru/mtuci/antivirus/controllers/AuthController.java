package ru.mtuci.antivirus.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.DTO.UserLoginDTO;
import ru.mtuci.antivirus.entities.DTO.UserRegisterDTO;
import ru.mtuci.antivirus.entities.ENUMS.ROLE;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.services.UserService;
import ru.mtuci.antivirus.utils.JwtUtil;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint is working");
    }

    @PostMapping("/register")
    public ResponseEntity<?> userRegistration(@Valid @RequestBody UserRegisterDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(400).body("Validation error: " + msg);
        }

        // Check if user with this login already exists
        if (userService.existsByLogin(userDTO.getLogin())) {
            return ResponseEntity.status(400).body("Validation error: User with this login already exists");
        }

        // Check if user with this email already exists
        if (userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.status(400).body("Validation error: User with this email already exists");
        }

        User user = new User(userDTO.getLogin(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getEmail(), ROLE.ROLE_USER, null);
        userService.saveUser(user);

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok("Registration completed, JWT{Bearer " + token + "}");
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody UserLoginDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.status(400).body("Validation error: " + msg);
        }

        User user = userService.findUserByLogin(userDTO.getLogin());

        // If login not exist
        if (user == null) {
            return ResponseEntity.status(400).body("Validation error: User with this login not found");
        }

        // If password didnt match
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(400).body("Validation error: Password is incorrect");
        }

        String token = jwtUtil.generateToken(userService.loadUserByUsername(user.getUsername()));

        return ResponseEntity.status(200).body("Login completed, JWT{Bearer " + token + "}");
    }
}
