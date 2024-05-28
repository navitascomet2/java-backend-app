package com.navitas.gsademo.controller;

import com.navitas.gsademo.model.User;
import com.navitas.gsademo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class GSAController {

    private final UserRepository userRepository;

    GSAController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @GetMapping("/user/register_user")
    User addUser(@RequestBody User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setEmail(user.getEmail());
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole());

        return userRepository.save(user);
    }

    @GetMapping("/user/gettravllers")
    List<User> getTraveller(){
        return userRepository.findAll();
    }

    @PostMapping("/admin/posttravllers")
    List<User> traveller(){
        return userRepository.findAll();
    }
}
