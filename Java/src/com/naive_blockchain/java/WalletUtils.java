package com.naive_blockchain.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.ArrayList;

public class WalletUtils {

    static final String DEFAULT_PUBLIC_KEY_PATH = "./keys/public.pem";
    static final String DEFAULT_PRIVATE_KEY_PATH = "./keys/private.pem";

    public static KeyPair CreateWallet(int keySize, String publicPath, String privatePath) throws NoSuchAlgorithmException {
        if (keySize < 2048) keySize = 2048;

        publicPath = publicPath.isEmpty() ? DEFAULT_PUBLIC_KEY_PATH : publicPath;
        privatePath = privatePath.isEmpty() ? DEFAULT_PRIVATE_KEY_PATH : privatePath;

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(keySize, new SecureRandom());

        KeyPair wallet = generator.generateKeyPair();
        FileOutputStream stream;

        if (!publicPath.isEmpty()) {
            wrtieKeyToFile(publicPath, wallet.getPublic().getEncoded());
        }

        if (!privatePath.isEmpty()) {
            wrtieKeyToFile(privatePath, wallet.getPrivate().getEncoded());
        }

        return wallet;
    }

    public double ComputeBalance(CryptoWallet wallet, ArrayList<Transaction> transactions) {
        double balance = 0;

        for (Transaction t: transactions) {
            balance += t.ComputeBalance(wallet);
        }

        return balance;
    }

    private static void wrtieKeyToFile(String path, byte[] rawKey) {
        if (path.isEmpty() || rawKey.length == 0) return;

        try {
            File keyFile = new File(path);
            if (!keyFile.exists() || !keyFile.isFile()) {
                // check if folder exists, create if not
                File folder = new File(Paths.get(path).getParent().toString());
                if (!folder.exists()) folder.mkdir();
                // create key file if not existing
                keyFile.createNewFile();
            }

            // file stream write to file
            FileOutputStream stream = new FileOutputStream(keyFile);
            stream.write(rawKey);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
