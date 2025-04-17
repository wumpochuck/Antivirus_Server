package ru.mtuci.antivirus.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.requests.UserRequest;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/info/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long id){
        User user = userService.getUserById(id);
        return ResponseEntity.status(200).body("User: " + user.toString());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/info")
    public ResponseEntity<List<User>> getUserInfo(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(200).body(users);
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserRequest user, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String findUsername = userDetails.getUsername();
            User currentUser = userService.findUserByLogin(findUsername);

            System.out.println("User request: " + user.toString());

            if (user.getLogin() != null && !user.getLogin().isEmpty() && !user.getLogin().equals(currentUser.getLogin())) {
                if (userService.existsByLogin(user.getLogin())) {
                    return ResponseEntity.status(400).body("Validation error: login already exists");
                }

                currentUser.setLogin(user.getLogin());
            }

            if (user.getEmail() != null && !user.getEmail().isEmpty() &&!user.getEmail().equals(currentUser.getEmail())) {
                if (userService.existsByEmail(user.getEmail())) {
                    return ResponseEntity.status(400).body("Validation error: email already exists");
                }
                currentUser.setEmail(user.getEmail());
            }

            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                currentUser.setPassword(passwordEncoder.encode(user.getPasswordHash()));
            }

            userService.saveUser(currentUser);

            return ResponseEntity.status(200).body("User updated");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try{
            User user = userService.getUserById(id);
            userService.deleteUser(id);
            return ResponseEntity.status(200).body("User with id: " + id + " deleted");
        } catch (Exception e){
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
}
