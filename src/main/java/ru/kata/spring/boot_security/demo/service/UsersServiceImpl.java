package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer id) {
        Optional<User> oUser = userRepository.findById(id);

        return oUser.orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        if (!user.getPassword().isEmpty()) {
            if (!getUserById(user.getId()).getPassword().equals(user.getPassword())) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
        } else {
            user.setPassword(getUserById(user.getId()).getPassword());
        }
        userRepository.save(user);
    }
    @Transactional
    public void removeUserById(Integer id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(userRepository.getUserByUsername(username) == null) {
            throw new UsernameNotFoundException("Username not found!");
        }
        Hibernate.initialize(userRepository.getUserByUsername(username).getRoles());
        return userRepository.getUserByUsername(username);
    }

}
