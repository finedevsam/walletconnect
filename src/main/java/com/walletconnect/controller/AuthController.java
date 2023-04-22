package com.walletconnect.controller;

import com.walletconnect.entity.Merchant;
import com.walletconnect.entity.Team;
import com.walletconnect.entity.User;
import com.walletconnect.entity.impl.AuthModel;
import com.walletconnect.entity.impl.CreateUserModel;
import com.walletconnect.repository.MerchantRepository;
import com.walletconnect.repository.TeamRepository;
import com.walletconnect.repository.UserRepository;
import com.walletconnect.security.CustomUserDetailsService;
import com.walletconnect.service.impl.UserServiceImpl;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class AuthController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/signin")
    public ResponseEntity<Object> login(@RequestBody AuthModel authModel) throws Exception {
        Map<Object, Object> merchant_data = new HashMap<>();
        Map<Object, Object> data = new HashMap<>();

        Map<Object, Object> userData = new HashMap<>();
        authenticate(authModel.getEmail(), authModel.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authModel.getEmail());
        User user = userRepository.findUserByEmail(userDetails.getUsername());
        if(user.getIsMerchant()){
            Merchant merchant = merchantRepository.findMerchantByUserId(user.getId());

            Team team = teamRepository.findTeamByUserIdAndMerchantId(user.getId(), merchant.getId());
            merchant_data.put("merchantName", merchant.getMerchantName());
            merchant_data.put("address", merchant.getAddress());
            merchant_data.put("businessNo", merchant.getBusinessNo());
            merchant_data.put("businessEmail", merchant.getBusinessEmail());
            merchant_data.put("businessRegNo", merchant.getBusinessRegNo());
            merchant_data.put("businessRegCert", merchant.getBusinessRegCert());
            merchant_data.put("secretKey", merchant.getSecretKey());
            merchant_data.put("isActive", merchant.getIsActive());

            data.put("merchantData", merchant_data);
            userData.put("isMerchant", user.getIsMerchant());
            userData.put("firstName", team.getFirstName());
            userData.put("lastName", team.getLastName());
        }

        final String token = jwtTokenUtil.generateToken(userDetails);
        data.put("access", token);
        userData.put("id", user.getId());
        userData.put("email", user.getEmail());
        userData.put("CreateAt", user.getCreatedAt());
        data.put("user", userData);

        return new ResponseEntity<Object>(data, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> createMerchant(@RequestBody CreateUserModel userModel){
        return userService.createMerchant(userModel);
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

