package com.naive_blockchain.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class CryptoWallet {
    private KeyPair cryptoWallet;

    public CryptoWallet() {
        String publicKeyPath = WalletUtils.DEFAULT_PUBLIC_KEY_PATH;
        String privateKeyPath = WalletUtils.DEFAULT_PRIVATE_KEY_PATH;

        try {
            initWalletFromKeys(publicKeyPath, privateKeyPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public CryptoWallet(String publicPath, String privatePath) {
        String publicKeyPath = publicPath.isEmpty() ? WalletUtils.DEFAULT_PUBLIC_KEY_PATH :publicPath;
        String privateKeyPath = privatePath.isEmpty() ? WalletUtils.DEFAULT_PRIVATE_KEY_PATH : privatePath;

        try {
            initWalletFromKeys(publicKeyPath, privateKeyPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public CryptoWallet(KeyPair wallet) {
        if (wallet != null) {
            cryptoWallet = wallet;
        }
    }

    public boolean CreateNewWallet() {
        try {
            cryptoWallet = WalletUtils.createWallet(2048, "", "");
            return true;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    public PublicKey GetPublicKey() {
        if (cryptoWallet != null) {
            return cryptoWallet.getPublic();
        } else {
            return null;
        }
    }

    private void initWalletFromKeys(String publicPath, String privatePath)
            throws FileNotFoundException {

        if (publicPath.isEmpty() || privatePath.isEmpty()) {
            String message = "";
            if (publicPath.isEmpty()) message += "Unable to locate public key file. ";
            if (privatePath.isEmpty()) message += "Unable to locate private key file. ";

            throw new FileNotFoundException(message);
        }

        try {
            byte[] publicKeyRaw = Files.readAllBytes(Paths.get(publicPath));
            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKeyRaw);

            byte[] privateKeyRaw = Files.readAllBytes(Paths.get(privatePath));
            PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKeyRaw);

            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(publicSpec);
            PrivateKey privateKey = kf.generatePrivate(privateSpec);

            cryptoWallet = new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
