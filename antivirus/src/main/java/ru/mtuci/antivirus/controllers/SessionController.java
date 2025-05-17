package ru.mtuci.antivirus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.requests.UpdateSessionRequest;
import ru.mtuci.antivirus.services.UserSessionService;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    private final UserSessionService sessionService;

    @Autowired
    public SessionController(UserSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSession(@PathVariable Long id, @RequestBody UpdateSessionRequest sessionRequest) {
        try{
            return ResponseEntity.status(200).body(sessionService.updateSession(id, sessionRequest));
        } catch (RuntimeException e){
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }


    //
    @PostMapping("/refresh-update")
    public ResponseEntity<?> refreshUpdate(@RequestHeader("Authorization") String accessToken) {
        try {
            // Извлечение токена из заголовка Authorization
            if (accessToken != null && accessToken.startsWith("Bearer ")) {
                accessToken = accessToken.substring(7);
            } else {
                throw new RuntimeException("Некорректный заголовок Authorization");
            }

            // Обновление refresh токена на основе access токена
            return ResponseEntity.status(200).body(sessionService.refreshRefreshTokenByAccessToken(accessToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

}