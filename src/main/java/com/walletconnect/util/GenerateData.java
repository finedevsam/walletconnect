package com.walletconnect.util;


import org.springframework.stereotype.Service;

@Service
public class GenerateData {

    public String apikey(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        return getString(n, AlphaNumericString);
    }

    public String referenceNumber(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";
        return getString(n, AlphaNumericString);
    }

    private String getString(int n, String alphaNumericString) {
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int)(alphaNumericString.length()
                    * Math.random());
            sb.append(alphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
}
