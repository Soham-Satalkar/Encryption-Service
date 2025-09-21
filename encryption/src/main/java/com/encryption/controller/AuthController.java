package com.encryption.controller;

import com.encryption.util.CryptoService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.awt.*;


@RestController
@RequestMapping(path = "/api/encryption")
public class AuthController {

    private final CryptoService cryptoService;

    public AuthController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostMapping(path = "/auth", consumes = "application/json", produces = "application/json")
    public JSONObject authenticationRequest(@RequestBody String data) throws Exception {
        return cryptoService.encryptData(data);
    }

}
