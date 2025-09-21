package com.encryption.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.encryption.constant.Constants.*;

@Slf4j
@Service
public class CryptoService {

    private static final Logger log = LoggerFactory.getLogger(CryptoService.class);
    private PublicKey publicKey; // loaded once

    public CryptoService() throws Exception {
        // Load public key at application initialization
        String publicKeyPem = new String(Files.readAllBytes(Paths.get("D:\\JAVA\\public_key.pem")), StandardCharsets.UTF_8);
        this.publicKey = loadPublicKey(publicKeyPem);
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

    public JSONObject encryptData(String jsonData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException {
        //1. Generate AES Key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_SIZE);
        SecretKey aesKey = keyGen.generateKey();

        //2. Generate random IV (nonce)
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        // 3. AES-GCM Encryption
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);
        byte[] encryptedDataWithTag = cipher.doFinal(jsonData.getBytes(StandardCharsets.UTF_8));

        // 4. Split cipher + auth tag
        int encryptedDataLength = encryptedDataWithTag.length - AUTH_TAG_LENGTH;
        byte[] encryptedData = new byte[encryptedDataLength];
        byte[] authTag = new byte[AUTH_TAG_LENGTH];
        System.arraycopy(encryptedDataWithTag, 0, encryptedData, 0, encryptedDataLength);
        System.arraycopy(encryptedDataWithTag, encryptedDataLength, authTag, 0, AUTH_TAG_LENGTH);


        // 5. Encrypt AES Key with RSA
        Cipher cipherRsa = Cipher.getInstance("RSA/ECB/OAEPPadding");
        OAEPParameterSpec oaepParameterSpec = new OAEPParameterSpec(
                "SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT
        );
        cipherRsa.init(Cipher.ENCRYPT_MODE, publicKey, oaepParameterSpec);
        byte[] encryptedAESKey = cipherRsa.doFinal(aesKey.getEncoded());


        // 6. Base64 encode and build JSON
        JSONObject responseJson = new JSONObject();
        responseJson.put("encryptedKey", Base64.getEncoder().encodeToString(encryptedAESKey));
        responseJson.put("encryptedData", Base64.getEncoder().encodeToString(encryptedData));
        responseJson.put("authTag", Base64.getEncoder().encodeToString(authTag));
        responseJson.put("nonce", Base64.getEncoder().encodeToString(iv));

        log.info("Response JSON : {}",responseJson);

        return responseJson;

    }
}
