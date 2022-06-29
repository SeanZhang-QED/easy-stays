package com.easy.easystays.controller;

import com.easy.easystays.model.User;
import com.easy.easystays.model.UserRole;
import com.easy.easystays.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {

    private SignupService signupService;

    @Autowired
    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping("/signup/guest")
    public void addGuest(@RequestBody User user) {
        signupService.add(user, UserRole.ROLE_GUEST);
    }

    @PostMapping("/signup/host")
    public void addHost(@RequestBody User user) {
        signupService.add(user, UserRole.ROLE_HOST);
    }
}
