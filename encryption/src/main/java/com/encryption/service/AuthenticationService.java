package com.encryption.service;

import com.encryption.config.RestTemplateHelper;
import com.encryption.model.AuthResponse;
import com.encryption.util.CryptoService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Service
public class AuthenticationService implements IAuthenticationService{

    @Value("${external.service.receiver}")
    String receiverUrl;

    private final CryptoService cryptoService;
    private final RestTemplateHelper restTemplateHelper;

    public AuthenticationService(CryptoService cryptoService,
                                 RestTemplateHelper restTemplateHelper) {
        this.cryptoService = cryptoService;
        this.restTemplateHelper = restTemplateHelper;
    }

    public AuthResponse authRequest(String data) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, IOException, InvalidKeyException {

        JSONObject encryptedRequest = cryptoService.encryptData(data);
        log.info("Sending encrypted request to receiver: {}", encryptedRequest);

        AuthResponse response = restTemplateHelper.postForEntity(
                AuthResponse.class,                  // response type
                receiverUrl, // full URL
                encryptedRequest.toString()    // body as String
        );
        log.info("Response from receiver: {}", response);
        return response;
    };

}
