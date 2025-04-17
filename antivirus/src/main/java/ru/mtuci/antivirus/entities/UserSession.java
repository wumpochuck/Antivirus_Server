package ru.mtuci.antivirus.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.mtuci.antivirus.entities.ENUMS.STATUS;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder // Чтобы было удобнее создавать экземляр
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "access_token", columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    private LocalDateTime accessTokenExpires;

    private LocalDateTime refreshTokenExpires;

    private LocalDateTime sessionCreationTime;

    private LocalDateTime lastActivityTime;

    @Enumerated(EnumType.STRING)
    private STATUS status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Version
    @Column(name = "version")
    private Long version;
}
