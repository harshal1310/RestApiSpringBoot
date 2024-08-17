package com.sjprogramming.restapi.Service;

import com.sjprogramming.restapi.entity.User;
import com.sjprogramming.restapi.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Validator validator;
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9]{4,15}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$";
    private static final String MOBILE_REGEX = "^\\d{10}$";


    //@Autowired
    //  private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        try {
            System.out.println("in");
            user.setPassword(user.getPassword());
            return userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());
            throw e;
        }

    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByUsername(email);
    }

    public User updateUser(Long id, User userUpdateRequest) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (StringUtils.isBlank(userUpdateRequest.getFirstName())) {
                throw new IllegalArgumentException("First name cannot be empty");
            }
            if (StringUtils.isBlank(userUpdateRequest.getLastName())) {
                throw new IllegalArgumentException("Last name cannot be empty");
            }
            if (StringUtils.isBlank(userUpdateRequest.getMobile())) {
                throw new IllegalArgumentException("Mobile number cannot be empty");
            }
            user.setFirstName(userUpdateRequest.getFirstName());
            user.setLastName(userUpdateRequest.getLastName());
            user.setMobile(userUpdateRequest.getMobile());
            if(!StringUtils.isBlank(userUpdateRequest.getPassword()))
            user.setPassword(userUpdateRequest.getPassword());
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            return userRepository.save(user);
        }
        return null;
    }
    private void validateUsername(String username) {
        if (!Pattern.matches(USERNAME_REGEX, username)) {
            throw new ConstraintViolationException("Username must be 4-15 characters long and contain only letters and digits.", null);
        }
    }

    private void validateMobile(String mobile) {
        if (!Pattern.matches(MOBILE_REGEX, mobile)) {
            throw new ConstraintViolationException("Mobile number must be 10 digits long.", null);
        }
    }

    private void validatePassword(String password) {
        if (!Pattern.matches(PASSWORD_REGEX, password)) {
            throw new ConstraintViolationException("Password must be 8-15 characters long with at least 1 uppercase, 1 lowercase, 1 digit, and 1 special character.", null);
        }
    }
}
