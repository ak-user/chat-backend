package com.chat.backend.service;

import com.chat.backend.controllers.NotFoundException;
import com.chat.backend.models.User;
import com.chat.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User patient) {
        userRepository.save(patient);
    }

    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NotFoundException("User by this ID is not found");
        }
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
