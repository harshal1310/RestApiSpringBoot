package com.sjprogramming.restapi.Service;

import com.sjprogramming.restapi.entity.User;
import com.sjprogramming.restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    //@Autowired
  //  private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user) {


        try {
            System.out.println("in");
            user.setPassword(user.getPassword());
            return userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());
            throw e; // Rethrow or handle as appropriate
        }


    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
/*
    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(updatedUser.getEmail());
            // Update other fields as necessary
            return userRepository.save(user);
        }
        return null;
    }
*/


    public User updateUser(Long id, User userUpdateRequest) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirstName(userUpdateRequest.getFirstName());
            user.setLastName(userUpdateRequest.getLastName());
            user.setMobile(userUpdateRequest.getMobile());
            user.setPassword(userUpdateRequest.getPassword()); // You may want to hash the password here
            return userRepository.save(user);
        }
        return null;
    }
}
