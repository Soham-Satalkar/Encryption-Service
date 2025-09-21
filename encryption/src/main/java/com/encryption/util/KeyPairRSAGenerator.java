package com.encryption.util;


import java.security.*;

public class KeyPairRSAGenerator {

    public void generateRSAKey() throws NoSuchAlgorithmException {

        //1.Generate RSA Key Pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        //keyGen.initialize(4096); //stronger then 2048
        KeyPair keyPair = keyGen.generateKeyPair();

        //2.Extract Keys
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

    }

}
