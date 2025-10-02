package com.receiver.controller;

import com.receiver.model.AuthResponse;
import com.receiver.util.CryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/api/receiver")
public class AuthController {

    private final CryptoService cryptoService;

    public AuthController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostMapping(path = "/auth", consumes = "application/json", produces = "application/json")
    public AuthResponse authenticationRequest(@RequestBody String authRequest) throws Exception {

        log.info("Raw request received: {}", authRequest);

        cryptoService.decryptData(authRequest);

        AuthResponse response = new AuthResponse();

        response.setToken("ABCDEFG");

//        log.info("Decrypted  Received : {}",response);

        return response;

    }

}