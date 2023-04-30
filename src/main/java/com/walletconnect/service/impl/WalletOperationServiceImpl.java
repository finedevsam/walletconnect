package com.walletconnect.service.impl;

import com.walletconnect.entity.*;
import com.walletconnect.entity.impl.WalletTransfer;
import com.walletconnect.repository.*;
import com.walletconnect.service.WalletOperationService;
import com.walletconnect.util.GenerateData;
import com.walletconnect.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WalletOperationServiceImpl implements WalletOperationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Response response;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private UserWalletRepository walletRepository;

    @Autowired
    private GenerateData generateData;

    @Autowired
    private TransactionLogsRepository logsRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private WalletRepository merchantwalletRepository;


    @Override
    public ResponseEntity<Object> walletTransfer(WalletTransfer transfer) {

        // Logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> sender = userRepository.findByEmail(authentication.getName());

        // Receiver
        Optional<User> receiver = userRepository.findByEmail(transfer.getReceiverEmail());

        if(receiver.isEmpty()){
            return response.failResponse("Receiver not found", "", HttpStatus.BAD_REQUEST);
        }

        if(Objects.equals(sender.get().getEmail(), receiver.get().getEmail())){
            return response.failResponse("You can't send money to yourself", "", HttpStatus.BAD_REQUEST);
        }

        if(Objects.equals(receiver.get().getIsMerchantOwner(), false) && Objects.equals(receiver.get().getIsMerchant(), true)){
            return response.failResponse("Please provide official Merchant address", "", HttpStatus.BAD_REQUEST);
        }

        if(sender.get().getIsMerchant()){
            return response.failResponse("Permission denied", "Merchant not allowed to send fund out", HttpStatus.BAD_REQUEST);
        }

        if (sender.get().getIsUser() && receiver.get().getIsUser()){
            UserWallet senderWallet = walletRepository.findByUserId(sender.get().getId());

            Double senderWalletBalance = senderWallet.getBalance();

            if(Double.parseDouble(transfer.getAmount()) > senderWalletBalance){
                return response.failResponse("Insufficient balance", "", HttpStatus.BAD_REQUEST);
            }

            UserWallet recWallet = walletRepository.findByUserId(receiver.get().getId());

            Optional<UserProfile> senderUserProfile = userProfileRepository.findUserProfileByUserId(sender.get().getId());
            Optional<UserProfile> recUserProfile = userProfileRepository.findUserProfileByUserId(receiver.get().getId());

            Double recWalletBalance = recWallet.getBalance();

            Double senderNewBalance = (senderWalletBalance - Double.parseDouble(transfer.getAmount()));

            Double recNewBalance = (recWalletBalance + Double.parseDouble(transfer.getAmount()));

            recWallet.setBalance(recNewBalance);
            senderWallet.setBalance(senderNewBalance);
            walletRepository.save(recWallet);
            walletRepository.save(senderWallet);

            TransactionLogs newLogs = new TransactionLogs();
            newLogs.setAmount(Double.parseDouble(transfer.getAmount()));
            newLogs.setSenderId(sender.get().getId());
            newLogs.setReceiverId(receiver.get().getId());
            newLogs.setSender(String.format("%s %s", senderUserProfile.get().getLastName(), senderUserProfile.get().getFirstName()));
            newLogs.setReceiver(String.format("%s, %s", recUserProfile.get().getLastName(), recUserProfile.get().getFirstName()));
            newLogs.setTransactionRef(generateData.referenceNumber(12));
            newLogs.setTransactionType("WTW");
            logsRepository.save(newLogs);
            return response.successResponse("Transaction successful", newLogs.getTransactionRef(), HttpStatus.OK);
        }else if(sender.get().getIsUser() && receiver.get().getIsMerchant()){
            Merchant merchant = merchantRepository.findMerchantByUserId(receiver.get().getId());

            if(Objects.equals(merchant.getIsActivate(), false) || Objects.equals(merchant.getIsActivate(), null)){
                return response.failResponse("Merchant don't have permission to receive fund", "", HttpStatus.BAD_REQUEST);
            }
            Optional<UserProfile> senderUserProfile = userProfileRepository.findUserProfileByUserId(sender.get().getId());
            UserWallet senderWallet = walletRepository.findByUserId(sender.get().getId());

            Double senderWalletBalance = senderWallet.getBalance();
            if(Double.parseDouble(transfer.getAmount()) > senderWalletBalance){
                return response.failResponse("Insufficient balance", "", HttpStatus.BAD_REQUEST);
            }
            Wallet merchantWallet = merchantwalletRepository.findByMerchantId(merchant.getId());

            Double recWalletBalance = merchantWallet.getBalance();

            Double senderNewBalance = (senderWalletBalance - Double.parseDouble(transfer.getAmount()));

            Double recNewBalance = (recWalletBalance + Double.parseDouble(transfer.getAmount()));
            merchantWallet.setBalance(recNewBalance);
            merchantwalletRepository.save(merchantWallet);

            senderWallet.setBalance(senderNewBalance);

            walletRepository.save(senderWallet);

            TransactionLogs newLogs = new TransactionLogs();
            newLogs.setAmount(Double.parseDouble(transfer.getAmount()));
            newLogs.setSenderId(sender.get().getId());
            newLogs.setReceiverId(receiver.get().getId());
            newLogs.setSender(String.format("%s %s", senderUserProfile.get().getLastName(), senderUserProfile.get().getFirstName()));
            newLogs.setReceiver(String.format("%s", merchant.getMerchantName()));
            newLogs.setTransactionRef(generateData.referenceNumber(12));
            newLogs.setTransactionType("WTW");
            logsRepository.save(newLogs);
            return response.successResponse("Transaction successful", newLogs.getTransactionRef(), HttpStatus.OK);
        }else {
            return response.failResponse("Permission denied", "", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<TransactionLogs> myTransactionHistory(Pageable pageable) {
        // Logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        return logsRepository.findBySenderIdOrReceiverId(user.get().getId(), user.get().getId(), pageable).toList();
    }
}
