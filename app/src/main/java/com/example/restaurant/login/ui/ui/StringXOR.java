package com.example.restaurant.login.ui.ui;

import android.util.Base64;

public class StringXOR {
    public static String encode(String s) {
        String key = "asdfh234h12";
        return Base64.encodeToString(xor(s.getBytes(), key.getBytes()), 0);
    }

    // Декoдируем строку
    public static String decode(String s) {
        String key = "asdfh234h12";
        return new String(xor(Base64.decode(s, 0), key.getBytes()));
    }

    // Сама операция XOR
    private static byte[] xor(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i%key.length]);
        }
        return out;
    }

}
