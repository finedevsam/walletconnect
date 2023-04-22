package com.walletconnect.util;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class Response {
    public ResponseEntity<Object> successResponse(String message, String data, HttpStatus status){
        JSONObject json = new JSONObject();
        json.put("code", status.value());
        json.put("status", "successful");
        json.put("message", message);
        json.put("data", data);
        return new ResponseEntity<>(json, status);
    }

    public ResponseEntity<Object> failResponse(String message, String data, HttpStatus status){
        JSONObject json = new JSONObject();
        json.put("code", status.value());
        json.put("status", "fail");
        json.put("message", message);
        json.put("data", data);
        return new ResponseEntity<>(json, status);
    }
}
