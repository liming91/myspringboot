package com.ming.controller;

import com.ming.bean.User;
import com.ming.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserJPAController {

    @Autowired
     UserRepository userRepository;

    @GetMapping("/userjpa/{id}")
    public User getUser(@PathVariable(value = "id") Integer id){
        User user = userRepository.findOne(id);
        return  user;
    }

    @GetMapping("/userjpa")
    public User insertUser(User user){
        User user1 = userRepository.save(user);
        return user1;
    }
}
