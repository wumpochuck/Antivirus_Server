package ru.mtuci.antivirus.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public User findUserByLogin(String login){
        return userRepository.findByLogin(login);
    }

    public User findUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }
}
