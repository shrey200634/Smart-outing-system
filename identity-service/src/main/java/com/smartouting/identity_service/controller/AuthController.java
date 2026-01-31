package com.smartouting.identity_service.controller;
import com.smartouting.identity_service.entity.UserCredential;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import com.smartouting.identity_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@AllArgsConstructor
@NoArgsConstructor

@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // register end points
    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredential userCredential){
        return authService.saveUser(userCredential);
    }
    // LOGIN endpoints
    @PostMapping("/token")
    public String getToken (@RequestBody UserCredential authRequest){
        Authentication authenticate =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(),authRequest.getPassword())
        );
        if (authenticate.isAuthenticated()) {
            return authService.generateToken(authRequest.getEmail());
        } else {
            throw new RuntimeException("invalid access");
        }

    }



    // 3. VALIDATE Endpoint: Verify if a token is real
    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return "Token is valid";
    }
}
