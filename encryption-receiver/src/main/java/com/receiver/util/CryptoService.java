package com.receiver.util;

import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class CryptoService {

    private static final Logger log = LoggerFactory.getLogger(CryptoService.class);
    private PublicKey publicKey; // loaded once

    private PrivateKey privateKey;

    public CryptoService() throws Exception {
        // Load public key at application initialization
//        String publicKeyPem = new String(Files.readAllBytes(Paths.get("D:\\JAVA\\public_key.pem")), StandardCharsets.UTF_8);
//        this.publicKey = loadPublicKey(publicKeyPem);

        String privateKeyPem = new String(Files.readAllBytes(Paths.get("D:\\JAVA\\private_key.pem")), StandardCharsets.UTF_8);
        this.privateKey = loadPrivateKey(privateKeyPem);
    }

    public PublicKey loadPublicKey(String publicKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        publicKeyPem = publicKeyPem
                .replace("-----BEGIN PUBLIC KEY-----","")
                .replace("-----END PUBLIC KEY-----","")
                .replaceAll("\\s+","");
        byte[] publicKeyDer = Base64.getDecoder().decode(publicKeyPem);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyDer));
    }

    public PrivateKey loadPrivateKey(String privateKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        privateKeyPem = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----","")
                .replace("-----END PRIVATE KEY-----","")
                .replaceAll("\\s+","");
        byte[] privateKeyDer = Base64.getDecoder().decode(privateKeyPem);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyDer));
    }

        public String decryptData(String jsonData) throws ParseException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
            //1. Parse JSON
//            JSONParser parser = new JSONParser();
//            JSONObject json = (JSONObject) parser.parse(jsonData);

            JSONObject json = new JSONObject(jsonData);


            //2. Decode Base64
            String encryptedKeyB64 = (String) json.get("encryptedKey");
            String encryptedDataB64 = (String) json.get("encryptedData");
            String authTagB64 = (String) json.get("authTag");
            String nonceB64 = (String) json.get("nonce");

            byte[] encryptedKey = Base64.getDecoder().decode(encryptedKeyB64);
            byte[] encryptedData = Base64.getDecoder().decode(encryptedDataB64);
            byte[] authTag = Base64.getDecoder().decode(authTagB64);
            byte[] nonce = Base64.getDecoder().decode(nonceB64);

            //3. Decrypt AES Key with RSA OAEP SHA-256
            Cipher cipherRsa = Cipher.getInstance("RSA/ECB/OAEPPadding");
            OAEPParameterSpec spec = new OAEPParameterSpec(
                    "SHA-256","MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
            cipherRsa.init(Cipher.DECRYPT_MODE, privateKey, spec);
            byte[] aesKeyBytes = cipherRsa.doFinal(encryptedKey);

            //4. Decrypt Data with AES-GCM
            SecretKey aesKey = new SecretKeySpec(aesKeyBytes,"AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, nonce);
            Cipher cipherAes = Cipher.getInstance("AES/GCM/NoPadding");
            cipherAes.init(Cipher.DECRYPT_MODE,aesKey,gcmSpec);
            byte[] encryptedDataWithTag = new byte[encryptedData.length + authTag.length];

            System.arraycopy(encryptedData,0,encryptedDataWithTag,0,encryptedData.length);
            System.arraycopy(authTag,0,encryptedDataWithTag,encryptedData.length,authTag.length);

            byte[] plainText = cipherAes.doFinal(encryptedDataWithTag);

            return new String(plainText, StandardCharsets.UTF_8);

        }
}
