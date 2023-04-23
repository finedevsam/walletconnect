package com.walletconnect.service.impl;

import com.walletconnect.entity.Merchant;
import com.walletconnect.entity.Team;
import com.walletconnect.entity.User;
import com.walletconnect.entity.impl.AddTeamMember;
import com.walletconnect.repository.MerchantRepository;
import com.walletconnect.repository.TeamRepository;
import com.walletconnect.repository.UserRepository;
import com.walletconnect.service.MerchantService;
import com.walletconnect.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private Response response;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Object> getAllTeamMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        Merchant merchant = merchantRepository.findMerchantByUserId(user.get().getId());
        List<Team> teams = teamRepository.findAllByMerchantId(merchant.getId());
        return new ResponseEntity<Object>(teams, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> addTeamMember(AddTeamMember teamMember) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        Merchant merchant = merchantRepository.findMerchantByUserId(user.get().getId());
        Optional<Team> teams = teamRepository.findTeamByUserId(user.get().getId());
        if(! teams.get().getIsAdmin()){
            return response.failResponse("Pernission denied", user.get().getId(), HttpStatus.BAD_REQUEST);
        }
        User newUser = new User();
        Team newTeam = new Team();

        newUser.setEmail(teamMember.getEmail());
        newUser.setPassword(passwordEncoder.encode("123456"));
        newUser.setIsMerchant(true);
        userRepository.save(newUser);

        newTeam.setMerchant(merchant);
        newTeam.setUser(newUser);
        newTeam.setIsMember(true);
        newTeam.setFirstName(teamMember.getFirstName());
        newTeam.setLastName(teamMember.getLastName());
        teamRepository.save(newTeam);
        return response.successResponse("team added successfully", newUser.getId(), HttpStatus.OK);
    }

}
