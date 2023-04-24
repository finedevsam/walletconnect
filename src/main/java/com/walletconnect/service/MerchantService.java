package com.walletconnect.service;


import com.walletconnect.entity.impl.AddTeamMember;
import org.springframework.http.ResponseEntity;

public interface MerchantService {

    ResponseEntity<Object> getAllTeamMember();

    ResponseEntity<Object> addTeamMember(AddTeamMember teamMember);

    ResponseEntity<Object> removeTeamMember(String userId);
}
