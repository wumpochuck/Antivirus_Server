package ru.mtuci.antivirus.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.services.UserService;
import static ru.mtuci.antivirus.entities.ENUMS.ROLE.*;

@RestController
@RequestMapping("/users")
public class MyController {

    private final UserService userservice;

    @Autowired
    public MyController(UserService userservice) {
        this.userservice = userservice;
    }

    @GetMapping("/tester")
    public String tester(){
        return "test successful";
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user, BindingResult bindingresult){
        if(bindingresult.hasErrors()){
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }
        user.setRole(USER);
        userservice.save(user);
        return ResponseEntity.ok("Пользователь  добавлен.");
    }

}
