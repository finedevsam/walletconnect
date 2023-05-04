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

import java.util.*;

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

    @Autowired
    private TeamRepository teamRepository;


    @Override
    public ResponseEntity<Object> walletTransfer(WalletTransfer transfer) {

        // Logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> sendUser = userRepository.findByEmail(authentication.getName());
        // Receiver
//        Optional<User> receiver = userRepository.findByEmail(transfer.getReceiverEmail());

        String userData = null;
        String check = null;
        String receiverId = null;
        Optional<UserProfile> userProfile = userProfileRepository.findUserProfileByPaymentTag(transfer.getPaymentTag());
        Optional<Merchant> merchantData = merchantRepository.findByPaymentTag(transfer.getPaymentTag());
        if(userProfile.isPresent()){
            userData = userProfile.get().getPaymentTag();
            check = "1";
            receiverId = userProfile.get().getId();
        }else if (merchantData.isPresent()){
            userData = merchantData.get().getPaymentTag();
            check = "2";
            receiverId = merchantData.get().getId();
        }else {
            return response.failResponse("User not found", "", HttpStatus.BAD_REQUEST);
        }

        if (sendUser.get().getIsUser()){
            Optional<UserProfile> senderProfile = userProfileRepository.findUserProfileByUserId(sendUser.get().getId());
            if(Objects.equals(senderProfile.get().getPaymentTag(), transfer.getPaymentTag())){
                return response.failResponse("You can't send money to yourself", "", HttpStatus.BAD_REQUEST);
            }
            UserWallet senderWallet = walletRepository.findByUserId(sendUser.get().getId());

            Double senderWalletBalance = senderWallet.getBalance();

            if(Double.parseDouble(transfer.getAmount()) > senderWalletBalance){
                return response.failResponse("Insufficient balance", "", HttpStatus.BAD_REQUEST);
            }

            if(Objects.equals(check, "1")){
                Optional<UserProfile> receiver = userProfileRepository.findUserProfileByPaymentTag(transfer.getPaymentTag());
                UserWallet recWallet = walletRepository.findByUserId(receiver.get().getUser().getId());

                Optional<UserProfile> senderUserProfile = userProfileRepository.findUserProfileByUserId(sendUser.get().getId());
                Optional<UserProfile> recUserProfile = userProfileRepository.findUserProfileByUserId(receiver.get().getUser().getId());

                Double recWalletBalance = recWallet.getBalance();

                Double senderNewBalance = (senderWalletBalance - Double.parseDouble(transfer.getAmount()));

                Double recNewBalance = (recWalletBalance + Double.parseDouble(transfer.getAmount()));

                recWallet.setBalance(recNewBalance);
                senderWallet.setBalance(senderNewBalance);
                walletRepository.save(recWallet);
                walletRepository.save(senderWallet);

                TransactionLogs newLogs = new TransactionLogs();
                newLogs.setAmount(Double.parseDouble(transfer.getAmount()));
                newLogs.setSenderId(sendUser.get().getId());
                newLogs.setReceiverId(receiver.get().getId());
                newLogs.setSender(String.format("%s %s", senderUserProfile.get().getLastName(), senderUserProfile.get().getFirstName()));
                newLogs.setReceiver(String.format("%s, %s", recUserProfile.get().getLastName(), recUserProfile.get().getFirstName()));
                newLogs.setTransactionRef(generateData.referenceNumber(12));
                newLogs.setTransactionType("WTW");
                logsRepository.save(newLogs);
                return response.successResponse("Transaction successful", newLogs.getTransactionRef(), HttpStatus.OK);
            }else {
                Optional<Merchant> merch = merchantRepository.findByPaymentTag(transfer.getPaymentTag());
                Merchant merchant = merchantRepository.findMerchantByUserId(merch.get().getUser().getId());

                if(Objects.equals(merchant.getIsActivate(), false) || Objects.equals(merchant.getIsActivate(), null)){
                    return response.failResponse("Merchant don't have permission to receive fund", "", HttpStatus.BAD_REQUEST);
                }
                Optional<UserProfile> senderUserProfile = userProfileRepository.findUserProfileByUserId(sendUser.get().getId());
                UserWallet sendWallet = walletRepository.findByUserId(sendUser.get().getId());

                Double sendWalletBalance = senderWallet.getBalance();
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
                newLogs.setSenderId(sendUser.get().getId());
                newLogs.setReceiverId(receiverId);
                newLogs.setSender(String.format("%s %s", senderUserProfile.get().getLastName(), senderUserProfile.get().getFirstName()));
                newLogs.setReceiver(String.format("%s", merchant.getMerchantName()));
                newLogs.setTransactionRef(generateData.referenceNumber(12));
                newLogs.setTransactionType("WTW");
                logsRepository.save(newLogs);
                return response.successResponse("Transaction successful", newLogs.getTransactionRef(), HttpStatus.OK);
            }
        }else {
            return response.failResponse("Merchant can't send money", "", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<TransactionLogs> myTransactionHistory(Pageable pageable) {
        // Logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        return logsRepository.findBySenderIdOrReceiverId(user.get().getId(), user.get().getId(), pageable).toList();
    }

    @Override
    public ResponseEntity<Object> walletBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());

        Map<String, Object> data = new HashMap<>();
        if(Objects.equals(user.get().getIsMerchant(), true)){
            Optional<Team> team = teamRepository.findTeamByUserId(user.get().getId());
            Optional<Merchant> merchant = merchantRepository.findMerchantById(team.get().getMerchant().getId());
            Optional<Wallet> wallet = Optional.ofNullable(merchantwalletRepository.findByMerchantId(merchant.get().getId()));
            data.put("balance", wallet.get().getBalance());
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        else {
            Optional<UserWallet> wallet = Optional.ofNullable(walletRepository.findByUserId(user.get().getId()));
            data.put("balance", wallet.get().getBalance());
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
    }
}
