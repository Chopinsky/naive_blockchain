package com.naive_blockchain.java;

import java.security.*;

public class WalletUtils {
    private static final String DEFAULT_PUBLIC_KEY_PATH = "./keys/public.pem";
    private static final String DEFAULT_PRIVATE_KEY_PATH = "./keys/private.pem";

    public static KeyPair createWallet(int keySize, String publicPath, String privatePath) throws NoSuchAlgorithmException {
        if (keySize < 2048) keySize = 2048;

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(keySize, new SecureRandom());

        KeyPair wallet = generator.generateKeyPair();

        if (!publicPath.isEmpty()) {
            //TODO: write public key to file
        }

        if (!privatePath.isEmpty()) {
            //TODO: write private key to file
        }

        return wallet;
    }
}
