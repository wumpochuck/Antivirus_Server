package ru.mtuci.antivirus.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userrepository;

    @Autowired
    public UserService(UserRepository userrepository) {
        this.userrepository = userrepository;
    }

    public void save(User user){
        userrepository.save(user);
    }
}
