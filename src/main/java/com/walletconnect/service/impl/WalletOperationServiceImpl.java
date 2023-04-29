package com.walletconnect.service.impl;

import com.walletconnect.entity.Merchant;
import com.walletconnect.entity.User;
import com.walletconnect.entity.impl.WalletTransfer;
import com.walletconnect.repository.MerchantRepository;
import com.walletconnect.repository.UserRepository;
import com.walletconnect.service.WalletOperationService;
import com.walletconnect.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletOperationServiceImpl implements WalletOperationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Response response;

    @Autowired
    private MerchantRepository merchantRepository;


    @Override
    public ResponseEntity<Object> walletTransfer(WalletTransfer transfer) {

        // Logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> sender = userRepository.findByEmail(authentication.getName());

        // Receiver

        Optional<User> receiver = userRepository.findByEmail(transfer.getReceiverEmail());
        if(!receiver.isPresent()){
            return response.failResponse("Receiver not found", "", HttpStatus.BAD_REQUEST);
        }
        if(sender.get().getIsMerchant()){
            return response.failResponse("Permission denied", "Mechant not allowed to send fund out", HttpStatus.BAD_REQUEST);
        }

        if (sender.get().getIsUser() && receiver.get().getIsUser()){
            Optional<User> rec = userRepository.findByEmail(transfer.getReceiverEmail());
            return null;
        }else if(sender.get().getIsUser() && receiver.get().getIsMerchant()){
            // Merchant logic
            return null;
        }else {
            return response.failResponse("Permission denied", "", HttpStatus.BAD_REQUEST);
        }
    }
}
