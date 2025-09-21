package com.encryption;

import com.encryption.util.KeyPairRSAGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class EncryptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(EncryptionApplication.class, args);

	}

//    @PostConstruct
//    public void init() throws NoSuchAlgorithmException, IOException {
//        KeyPairRSAGenerator generator = new KeyPairRSAGenerator();
//        generator.generateRSAKey();  // Called after bean creation
//    }

}
