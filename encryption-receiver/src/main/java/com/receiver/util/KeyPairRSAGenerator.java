package com.receiver.util;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

@Slf4j
@Component
public class KeyPairRSAGenerator {

    private static final Logger log = LoggerFactory.getLogger(KeyPairRSAGenerator.class);

    public void generateRSAKey() throws NoSuchAlgorithmException, IOException {

        log.info("----------RSA Key Generation Started----------");

        //1.Generate RSA Key Pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        //keyGen.initialize(4096); //stronger then 2048
        KeyPair keyPair = keyGen.generateKeyPair();

        //2.Extract Keys
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        //3.Convert key to Base64-encoded Strings
        String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n"
                + Base64.getMimeEncoder(64,new byte[]{'\n'}).encodeToString(publicKey.getEncoded())
                + "\n-----END PUBLIC KEY-----\n" ;

        String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n"
                + Base64.getMimeEncoder(64,new byte[]{'\n'}).encodeToString(privateKey.getEncoded())
                + "\n------END PRIVATE KEY-----\n";

        //4.Save to files
        try(FileOutputStream pubOut = new FileOutputStream("public_key.pem")){
            pubOut.write(publicKeyPEM.getBytes());
        }
        try(FileOutputStream privOut = new FileOutputStream("private_key.pem")){
            privOut.write(privateKeyPEM.getBytes());
        }

        log.info("----------RSA Key Generation End----------");

    }
}
