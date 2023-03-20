package com.snoodify.user.controller;

import com.snoodify.user.dto.UserDto;
import com.snoodify.user.model.User;
import com.snoodify.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        User user = mapUserDtoToUser(userDto);
        userService.saveUser(user);
        logger.info("user " + user.getUsername()+ " saved successfully");
        return new ResponseEntity<>("User saved successfully", HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password) {
        Optional<User> user = userService.getUser(username, password);
        if(user.isPresent()) {
            logger.info("User " + username + " found in database");
            if (userService.validatePassword(user.get(), password)) {
                logger.info("Password validated");
                return new ResponseEntity<String>("Welcome " + user.get().getUsername(), HttpStatus.OK);
            } else {
                logger.info("Password incorrect");
                return new ResponseEntity<>("Incorrect password",  HttpStatus.BAD_REQUEST);
            }
        } else {
            logger.info("User " + username + " not found in database");
            return new ResponseEntity<>("User with username " + username+ " does not exist",  HttpStatus.BAD_REQUEST);
        }
    }


    public User mapUserDtoToUser(UserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .age(userDto.getAge())
                .gender(userDto.getGender())
                .build();
    }
}
