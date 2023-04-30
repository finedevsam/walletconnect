package com.walletconnect.service.impl;

import com.walletconnect.entity.*;
import com.walletconnect.entity.impl.ChangePassword;
import com.walletconnect.entity.impl.CreateUserModel;
import com.walletconnect.entity.impl.ResetPassword;
import com.walletconnect.entity.impl.ResetPasswordConfirm;
import com.walletconnect.exception.ResourceNotFoundException;
import com.walletconnect.repository.*;
import com.walletconnect.service.UserService;
import com.walletconnect.util.GenerateData;
import com.walletconnect.util.Response;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Response response;

    @Autowired
    private GenerateData generateData;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private UserWalletRepository userWalletRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public ResponseEntity<Object> userRegistration(CreateUserModel user) {
        if(userRepository.existsByEmail(user.getEmail())){
            return response.failResponse("email already used", "", HttpStatus.CONFLICT);
        }
        System.out.println(user.getAccountType());

        if(Objects.equals(user.getAccountType(), "personal")){
            UserProfile newUserProfile = new UserProfile();
            UserWallet newUserWallet = new UserWallet();
            User newUser = new User();

            newUser.setEmail(user.getEmail());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            newUser.setIsUser(true);
            userRepository.save(newUser);


            newUserProfile.setFirstName(user.getFirstName());
            newUserProfile.setLastName(user.getLastName());
            newUserProfile.setUser(newUser);
            userProfileRepository.save(newUserProfile);

            newUserWallet.setUser(newUser);
            userWalletRepository.save(newUserWallet);
            return response.successResponse("registration successful", newUser.getId(), HttpStatus.OK);

        }else if(Objects.equals(user.getAccountType(), "merchant")) {
            Merchant merchant = new Merchant();
            Team team = new Team();
            Wallet wallet = new Wallet();
            User newUser = new User();

            newUser.setEmail(user.getEmail());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            newUser.setIsMerchant(true);
            newUser.setIsMerchantOwner(true);
            userRepository.save(newUser);

            merchant.setUser(newUser);
            merchant.setBusinessEmail(user.getEmail());
            merchant.setSecretKey(String.format("sk_%s", generateData.apikey(32)));
            merchantRepository.save(merchant);

            team.setFirstName(user.getFirstName());
            team.setLastName(user.getLastName());
            team.setIsAdmin(true);
            team.setMerchant(merchant);
            team.setUser(newUser);
            teamRepository.save(team);

            wallet.setMerchant(merchant);
            walletRepository.save(wallet);
            return response.successResponse("registration successful", newUser.getId(), HttpStatus.OK);
        }
        else {
            return response.failResponse("please select correct account type from `personal` or  `merchant`", "", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public User getLoggedInUser() {
        return null;
    }

    @Override
    public Page<User> getAllUser(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<Object> changePassword(ChangePassword changePassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(authentication.getName());
        if (!passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword())){
            return response.failResponse("Wrong password", user.getId(), HttpStatus.BAD_REQUEST);
        }else if (!Objects.equals(changePassword.getNewPassword(), changePassword.getConfirmNewPassword())){
            return response.failResponse("Password mismatch", user.getId(), HttpStatus.BAD_REQUEST);
        }else {
            user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
            userRepository.save(user);
            return response.successResponse("Password change successfully", user.getId(), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Object> resetPassword(ResetPassword resetPassword) {
        User user = userRepository.findByEmail(resetPassword.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User does not exist on our system"));

        PasswordReset passwordReset = new PasswordReset();
        String token = UUID.randomUUID().toString();
        passwordReset.setToken(token);
        passwordReset.setUser(user);
        passwordResetRepository.save(passwordReset);
        String resetUrl = "http://localhost:8080/user/reset-password/confirm?token=" + token;
        String message = "Please click on the following link to reset your password: " + resetUrl;
        System.out.println(message);
        System.out.println(resetUrl);
        return response.successResponse("Password reset link sent to your registered email", resetPassword.getEmail(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> resetPasswordConfirm(String token, ResetPasswordConfirm passwordConfirm) {
        PasswordReset resetToken = passwordResetRepository.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException("Invalid reset token"));

        if(! Objects.equals(passwordConfirm.getNewPassword(), passwordConfirm.getConfirmNewPassword())){
            return response.failResponse("Password mismatch", resetToken.getUser().getId(), HttpStatus.BAD_REQUEST);
        }else {
            User user = resetToken.getUser();
            user.setPassword(passwordEncoder.encode(passwordConfirm.getNewPassword()));
            userRepository.save(user);
            passwordResetRepository.delete(resetToken);
            return response.successResponse("Password reset successful", user.getId(), HttpStatus.OK);
        }
    }
}
