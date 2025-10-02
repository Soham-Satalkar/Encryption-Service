package com.receiver.model;

import lombok.Data;

@Data
public class AuthRequest {

    private String encryptedData;
    private String encryptedKey;
    private String authTag;
    private String nonce;

}
