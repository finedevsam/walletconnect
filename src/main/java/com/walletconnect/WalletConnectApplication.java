package com.walletconnect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WalletConnectApplication {
    public static void main(String[] args) {
        SpringApplication.run(WalletConnectApplication.class, args);
    }

}
