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

import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;;

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());


    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/test")
    public String test(){
        return "Hello";
    }

    @PostMapping("/register")
    public ResponseEntity<?> userRegistration(@Valid @RequestBody UserRegisterDTO userDTO/*, BindingResult bindingResult*/) {
        // if (bindingResult.hasErrors()) {
        //    return ResponseEntity.badRequest().body("Validation error: " + bindingResult.getAllErrors());
        // }

        logger.info("Received request body: " + userDTO.toString());

        // Check if user with this login already exists
        if(userService.existsByLogin(userDTO.getLogin())){
            return ResponseEntity.badRequest().body("Validation error:  User with this login already exists");
        }

        // Check if user with this email already exists
        if(userService.existsByEmail(userDTO.getEmail())){
            return ResponseEntity.badRequest().body("Validation error:  User with this email already exists");
        }

        User user = new User(userDTO.getLogin(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getEmail(), ROLE.ROLE_USER, null);
        userService.saveUser(user);

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok("Registration completed, JWT: Bearer " + token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody UserLoginDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: " + bindingResult.getAllErrors());
        }

        User user = userService.findUserByLogin(userDTO.getLogin());

        // If login not exist
        if(user == null){
            return ResponseEntity.badRequest().body("Validation error: User not found");
        }

        // If password didnt match
        if(!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())){
            return ResponseEntity.status(401).build();
        }

        String token = jwtUtil.generateToken(userService.loadUserByUsername(user.getUsername()));
        return ResponseEntity.ok("Login completed, JWT: Bearer " + token);
    }
}
