package com.easy.easystays.service;

import com.easy.easystays.exception.UserNotExistException;
import com.easy.easystays.model.Token;
import com.easy.easystays.model.User;
import com.easy.easystays.model.UserRole;
import com.easy.easystays.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public Token authenticate(User user, UserRole userRole) throws UserNotExistException {
        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        } catch (AuthenticationException ex) {
            throw new UserNotExistException("User not exist");
        }

        if (auth == null || !auth.isAuthenticated() || !auth.getAuthorities().contains(new SimpleGrantedAuthority(userRole.name()))) {
            // fail to authenticate or does not have the role to access
            throw new UserNotExistException("User not exist");
        }
        return new Token(jwtUtil.generateToken(user.getUsername()));
    }

}
