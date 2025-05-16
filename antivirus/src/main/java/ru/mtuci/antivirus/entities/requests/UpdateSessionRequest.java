package ru.mtuci.antivirus.entities.requests;

import lombok.Data;
import ru.mtuci.antivirus.entities.ENUMS.session.STATUS;

import java.time.LocalDateTime;

@Data
public class UpdateSessionRequest {
    private String accessToken;

    private String refreshToken;

    private LocalDateTime accessTokenExpires;

    private LocalDateTime refreshTokenExpires;

    private LocalDateTime sessionCreationTime;

    private LocalDateTime lastActivityTime;

    private STATUS status;

    private Long version;
}