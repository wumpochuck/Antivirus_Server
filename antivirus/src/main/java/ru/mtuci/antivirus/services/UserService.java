package ru.mtuci.antivirus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.DTO.TokenResponse;
import ru.mtuci.antivirus.entities.DTO.UserLoginDTO;
import ru.mtuci.antivirus.entities.DTO.UserRegisterDTO;
import ru.mtuci.antivirus.entities.ENUMS.ROLE;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.entities.UserSession;
import ru.mtuci.antivirus.repositories.UserRepository;
import ru.mtuci.antivirus.utils.JwtUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSessionService userSessionService;
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByLogin(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public User findUserByLogin(String login){
        return userRepository.findUserByLogin(login)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    public User getUserById(Long id){
        return userRepository.getUserById(id);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /// Регистрация пользователя
    public UserSession registerUser(UserRegisterDTO userDTO) {
        // Существует ли пользователь с такой почтой или именем
        if(userRepository.findUserByEmail(userDTO.getEmail()).isPresent()){
            throw new RuntimeException("User with this email already exists");
        }
        if (userRepository.findUserByLogin(userDTO.getLogin()).isPresent()){
            throw new RuntimeException("User with this login already exists");
        }

        User user = User.builder()
                .login(userDTO.getLogin())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(ROLE.ROLE_USER)
                .isBlocked(false)
                .build();

        // Сохраняем и создаем сессию
        userRepository.save(user);

        return userSessionService.createUserSession(user);
    }

    /// Логин
    public UserSession loginUser(UserLoginDTO userDTO){
        User user = userRepository.findUserByLogin(userDTO.getLogin())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Проверка пароля
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        // Если пользователь найден и пароль совпал
        return userSessionService.createUserSession(user);
    }

    /// Логаут
    public void logoutUser(String authBearer){
        try {
            userSessionService.deactivateSessionByAccessToken(jwtUtil.resolveToken(authBearer));
            System.out.println("Пользователь вышел из системы");
        } catch (Exception e) {
            throw new RuntimeException("Logout error: " + e.getMessage());
        }
    }



}
