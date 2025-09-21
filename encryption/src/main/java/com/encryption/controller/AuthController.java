package com.encryption.controller;

import com.encryption.service.IAuthenticationService;
import com.encryption.util.CryptoService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/encryption")
public class AuthController {

    private final CryptoService cryptoService;
    private final IAuthenticationService authenticationService;

    public AuthController(CryptoService cryptoService, IAuthenticationService authenticationService) {
        this.cryptoService = cryptoService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/auth", consumes = "application/json", produces = "application/json")
    public String authenticationRequest(@RequestBody String data) throws Exception {
//        return cryptoService.encryptData(data);
        return authenticationService.authRequest(data);
    }

}
