package com.walletconnect.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestScheduler {

//    @Scheduled(cron = "*/2 * * * * *")
    public void job() throws InterruptedException{
        System.out.println("God bless my hustle");
    }
}
