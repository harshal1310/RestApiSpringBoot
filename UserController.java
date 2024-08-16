package com.sjprogramming.restapi.controller;

import com.sjprogramming.restapi.Service.UserService;
import com.sjprogramming.restapi.entity.User;
import com.sjprogramming.restapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (DataIntegrityViolationException e) {
            // Handle duplicate key error, e.g., username or email already exists
            Map<String, String> errors = new HashMap<>();
            errors.put("duplicateKey", "Username or email already exists");
            return ResponseEntity.status(409).body(errors);  // 409 Conflict
        } catch (Exception e) {
            // Handle any other exceptions
            Map<String, String> errors = new HashMap<>();
            errors.put("error", "An unexpected error occurred");
            return ResponseEntity.status(500).body(errors);  // 500 Internal Server Error
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> optionalUser = userService.getUserByUsername(user.getUsername());
        if (optionalUser.isPresent() && user.getPassword().equals(optionalUser.get().getPassword())) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userUpdateRequest) {
        try {
            User updatedUser = userService.updateUser(id, userUpdateRequest);
            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (DataIntegrityViolationException e) {
            // Handle duplicate key error on update, if applicable
            Map<String, String> errors = new HashMap<>();
            errors.put("duplicateKey", "Username or email already exists");
            return ResponseEntity.status(409).body(errors);  // 409 Conflict
        } catch (Exception e) {
            // Handle any other exceptions
            Map<String, String> errors = new HashMap<>();
            errors.put("error", "An unexpected error occurred");
            return ResponseEntity.status(500).body(errors);  // 500 Internal Server Error
        }
    }
}
