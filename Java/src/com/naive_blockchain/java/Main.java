package com.naive_blockchain.java;

import java.util.List;

import static com.naive_blockchain.java.CryptoUtils.*;

public class Main {

    public static void main(String args[]) {
        try {
            List<String> answers =
                    hashMine("hello blockchain!", 1000000, 4);
            System.out.println(answers);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
