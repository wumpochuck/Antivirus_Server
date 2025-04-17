package ru.mtuci.antivirus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mtuci.antivirus.entities.ENUMS.STATUS;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.entities.UserSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByRefreshToken(String refreshToken);

    List<UserSession> findByUserAndStatus(User user, STATUS status);

    List<UserSession> findByStatusAndSessionCreationTimeBefore(STATUS status, LocalDateTime threshold);

    Optional<UserSession> findByAccessToken(String accessToken);

    List<UserSession> findByStatusAndLastActivityTimeBefore(STATUS status, LocalDateTime threshold);

    List<UserSession> findByStatus(STATUS status);
}
