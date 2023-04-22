package com.walletconnect.service.impl;

import com.walletconnect.entity.Merchant;
import com.walletconnect.entity.Team;
import com.walletconnect.entity.User;
import com.walletconnect.entity.Wallet;
import com.walletconnect.entity.impl.CreateUserModel;
import com.walletconnect.repository.MerchantRepository;
import com.walletconnect.repository.TeamRepository;
import com.walletconnect.repository.UserRepository;
import com.walletconnect.repository.WalletRepository;
import com.walletconnect.service.UserService;
import com.walletconnect.util.GenerateData;
import com.walletconnect.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public ResponseEntity<Object> createMerchant(CreateUserModel user) {
        if(userRepository.existsByEmail(user.getEmail())){
            return response.failResponse("email already used", "", HttpStatus.CONFLICT);
        }
        User newUser = new User();
        Merchant merchant = new Merchant();
        Team team = new Team();
        Wallet wallet = new Wallet();

        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setIsMerchant(true);
        userRepository.save(newUser);

        merchant.setUser(newUser);
        merchant.setSecretKey(String.format("sk_%s",generateData.apikey(32)));
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
}
