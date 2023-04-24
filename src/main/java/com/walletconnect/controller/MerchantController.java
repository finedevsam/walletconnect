package com.walletconnect.controller;

import com.walletconnect.entity.impl.AddTeamMember;
import com.walletconnect.service.impl.MerchantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchant/")
public class MerchantController {

    @Autowired
    private MerchantServiceImpl merchantService;

    @GetMapping("/team")
    public ResponseEntity<Object> allTeamMember(){
        return merchantService.getAllTeamMember();
    }

    @PostMapping("/team")
    public ResponseEntity<Object> addTeamMember(@RequestBody AddTeamMember addTeamMember){
        return merchantService.addTeamMember(addTeamMember);
    }

    @DeleteMapping("/team/{userId}")
    public ResponseEntity<Object> removeTeamMember(@PathVariable(name = "userId") String userId){
        return merchantService.removeTeamMember(userId);
    }
}
