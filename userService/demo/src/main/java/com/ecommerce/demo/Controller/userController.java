package com.ecommerce.demo.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.demo.Model.User;
import com.ecommerce.demo.Service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")
public class userController {

    private final UserService userService;

    public userController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User postUser(@RequestBody User user) {
        // TODO: process POST request

        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getMethodName(@PathVariable Long id) {
        return userService.findUserById(id);
    }

}
