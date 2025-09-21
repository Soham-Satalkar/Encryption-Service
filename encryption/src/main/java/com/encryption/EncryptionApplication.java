package com.encryption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
