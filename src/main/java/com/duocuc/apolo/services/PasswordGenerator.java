package com.duocuc.apolo.services;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PasswordGenerator {
    public String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}

