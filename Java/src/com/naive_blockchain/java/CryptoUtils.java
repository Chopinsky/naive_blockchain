package com.naive_blockchain.java;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class CryptoUtils {
    private static final String defaultDigestName = "SHA-256";
    private static final char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static final int THREAD_POOL = 4;

    private static String hexDigest(String message, String digestName) {
        try {
            digestName = (digestName == null || digestName.isEmpty()) ? defaultDigestName : digestName;
            MessageDigest md = MessageDigest.getInstance(digestName);
            byte[] digest = md.digest(message.getBytes(StandardCharsets.UTF_8));
            char[] hex = new char[digest.length * 2];

            for (int i = 0; i < digest.length; i++) {
                hex[2 * i] = charSet[((digest[i] & 0xf0) >> 4)];
                hex[2 * i + 1] = charSet[(digest[i] & 0x0f)];
            }

            return new String(hex);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Callable<String> hashRunner(String msg, String prefix, int start, int end) {
        return () -> {
            String digest;
            for (Integer guess = start; guess < end; guess++) {
                digest = hexDigest((msg + guess.toString()), defaultDigestName);
                if (digest.startsWith(prefix))
                    return guess.toString();
            }
            return "";
        };
    }

    public static List<String> hashMine(String message, int nonceRange, int difficulty) throws InterruptedException {
        if (message == null || message.isEmpty())
            return null;

        if (nonceRange < 1000)
            nonceRange = 1000;

        if (difficulty < 2) {
            difficulty = 2;
        } else if (difficulty > 16) {
            difficulty = 16;
        }

        String prefix = String.join("", Collections.nCopies(difficulty, "1"));
        int start, end, count = nonceRange / THREAD_POOL;

        ExecutorService executor = Executors.newWorkStealingPool();
        List<Callable<String>> callableList = new ArrayList<>();

        for (int i = 0; i < THREAD_POOL; i++) {
            start = i * count;
            end = (i + 1) * count - 1;
            end = end < nonceRange ? end : nonceRange;
            callableList.add(hashRunner(message, prefix, start, end));
        }

        List<String> answers = new CopyOnWriteArrayList<>();
        executor.invokeAll(callableList)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(s -> {
                    if (s != null && !s.isEmpty())
                        answers.add(s);
                });

        return answers;
    }
}
