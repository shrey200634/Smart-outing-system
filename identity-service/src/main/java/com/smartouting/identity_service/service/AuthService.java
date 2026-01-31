package com.smartouting.identity_service.service;

import com.smartouting.identity_service.entity.UserCredential;
import com.smartouting.identity_service.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserCredentialRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

//register
    public String saveUser(UserCredential userCredential){
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        repository.save(userCredential);
        return "User added to the System";
    }
    // login ;generate a token for varified user
    public String generateToken(String username ){
        return jwtService.generateToken(username);
    }

    // check if the token is valid
    public  void validateToken(String token){
        jwtService.validateToken(token);
    }
}
