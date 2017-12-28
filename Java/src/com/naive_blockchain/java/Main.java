package com.naive_blockchain.java;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.naive_blockchain.java.CryptoUtils.*;

public class Main {

    public static void main(String args[]) {
        final String message = "hello blockchain! too many wins to process...";

        // mineTest(message);

        walletTest(message);
    }

    private static  void walletTest(String message) {
        CryptoWallet wallet = new CryptoWallet();
        if (wallet.GetAddress() == null) {
            wallet = CryptoWallet.CreateNewWallet();
        }

        byte[] signatures = wallet.Sign(message);

        System.out.println("Message verficiation is: " + wallet.Verify(message, signatures));
    }

    private static void mineTest(String message) {
        try {
            List<String> answers =
                    hashMine(message, 1000000, 4);
            System.out.println(answers);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
