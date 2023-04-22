package com.walletconnect.controller;

import com.walletconnect.entity.User;
import com.walletconnect.entity.impl.AuthModel;
import com.walletconnect.repository.UserRepository;
import com.walletconnect.security.CustomUserDetailsService;
import com.walletconnect.service.UserService;
import com.walletconnect.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<Map> login(@RequestBody AuthModel authModel) throws Exception {
        Map user_data = new HashMap<>();
        Map data = new HashMap<>();
        authenticate(authModel.getEmail(), authModel.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authModel.getEmail());
        User user = userRepository.findUserByEmail(userDetails.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
        user_data.put("id", user.getId());
        user_data.put("createdAt", user.getCreatedAt());

        data.put("access", token);
        data.put("user", user_data);

        return new ResponseEntity<Map>(data, HttpStatus.OK);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    email, password));

        } catch (DisabledException e){
            throw new Exception("User disabled");
        } catch (BadCredentialsException e){
            throw new Exception("Bad Credentials");
        }
    }
}

