package com.walletconnect.service;


import com.walletconnect.entity.impl.AddTeamMember;
import com.walletconnect.entity.impl.UpdateMerchantAccount;
import org.springframework.http.ResponseEntity;

public interface MerchantService {

    ResponseEntity<Object> getAllTeamMember();

    ResponseEntity<Object> addTeamMember(AddTeamMember teamMember);

    ResponseEntity<Object> removeTeamMember(String userId);

    ResponseEntity<?> updateBusiness(UpdateMerchantAccount merchantAccount);
}
