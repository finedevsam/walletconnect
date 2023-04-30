package com.walletconnect.service.impl;

import com.walletconnect.entity.Merchant;
import com.walletconnect.entity.Team;
import com.walletconnect.entity.User;
import com.walletconnect.entity.impl.AddTeamMember;
import com.walletconnect.entity.impl.UpdateMerchantAccount;
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

import java.util.*;

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
        Optional<Team> team = teamRepository.findTeamByUserId(user.get().getId());
        Optional<Merchant> merchant = merchantRepository.findMerchantById(team.get().getMerchant().getId());
        List<Team> teams = teamRepository.findAllByMerchantId(merchant.get().getId());
        return new ResponseEntity<Object>(teams, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> addTeamMember(AddTeamMember teamMember) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        Merchant merchant = merchantRepository.findMerchantByUserId(user.get().getId());
        Optional<Team> teams = teamRepository.findTeamByUserId(user.get().getId());
        if(! teams.get().getIsAdmin()){
            return response.failResponse("Permission denied", user.get().getId(), HttpStatus.BAD_REQUEST);
        }
        Optional<User> checkUser = userRepository.findByEmail(teamMember.getEmail());

        if(checkUser.isPresent()){
            return response.failResponse("Email has been Used", user.get().getId(), HttpStatus.CONFLICT);
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

    @Override
    public ResponseEntity<Object> removeTeamMember(String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> loggedInUser = userRepository.findByEmail(authentication.getName());
        Merchant merchant = merchantRepository.findMerchantByUserId(loggedInUser.get().getId());
        Optional<Team> teams = teamRepository.findTeamByUserIdAndMerchantId(userId, merchant.getId());
//        if(teams.get().getIsAdmin()){
//            System.out.println("Here 1");
//            System.out.println(teams.get().getFirstName());
//            return response.failResponse("Permission denied", userId, HttpStatus.BAD_REQUEST);
//        }
        if(Objects.equals(loggedInUser.get().getId(), userId) && teams.get().getIsAdmin()){
            return response.failResponse("Permission denied", "You can't delete yourself as owner", HttpStatus.BAD_REQUEST);
        }
        if(Objects.equals(loggedInUser.get().getId(), userId)){
            return response.failResponse("Permission denied", "You can't delete yourself", HttpStatus.BAD_REQUEST);
        }
        if(Objects.equals(merchant.getId(), teams.get().getMerchant().getId())) {
            userRepository.deleteById(userId);
            return response.successResponse("Team member removed", userId, HttpStatus.OK);
        }
        System.out.println("Here 2");
        return response.failResponse("Permission denied", userId, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> updateBusiness(UpdateMerchantAccount merchantAccount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> loggedInUser = userRepository.findByEmail(authentication.getName());
        if(Objects.equals(loggedInUser.get().getIsMerchant(), true)){
            if(Objects.equals(loggedInUser.get().getIsMerchantOwner(), true)){
                Merchant merchant = merchantRepository.findMerchantByUserId(loggedInUser.get().getId());
                merchant.setAddress(merchantAccount.getBusinessAddress());
                merchant.setBusinessNo(merchantAccount.getBusinessPhoneNo());
                merchant.setBusinessRegCert(merchantAccount.getBusinessRegNo());
                merchant.setBusinessRegNo(merchantAccount.getBusinessRegNo());
                merchant.setContactPerson(merchantAccount.getContactPerson());
                merchant.setContactPersonNo(merchantAccount.getContactPersonMobileNo());
                merchant.setMerchantName(merchantAccount.getBusinessName());
                merchant.setIsActivate(true);
                merchantRepository.save(merchant);
                return response.successResponse("Mechant information updated", merchant.getId(), HttpStatus.OK);
            }else {
                return response.failResponse("Permission Denied", "", HttpStatus.BAD_REQUEST);
            }
        }
        return response.failResponse("Permission Denied", "", HttpStatus.BAD_REQUEST);
    }

}
