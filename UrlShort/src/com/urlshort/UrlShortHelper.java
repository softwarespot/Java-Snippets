package com.urlshort;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * @author SoftwareSpot
 */
final class UrlShortHelper {
    public static final byte DECODE_ERROR = -1; // This could be private as there isDecodeOK() to check the return of the decode() method.
    public static final String ENCODE_ERROR = "-1"; // This could be private as there isEncodeOK() to check the return of the encode() method.
    private static final String chars = "FKM7TJP528YIGOE4Q6SW0D9NAZLVX13CRUBH"; // ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789;
    private static final int base = chars.length(); // Base type. Should be 36.
    private static final byte shortLength = 5;
    private static final UrlValidator urlValidator = new UrlValidator();

    public static int decode(String shortId) {
        if (shortId.trim().isEmpty() || !isEncodeOK(shortId)) {
            return DECODE_ERROR;
        }

        int id = 0, power = 0;
        for (int i = 0; i < shortId.length(); i++) {
            final char ch = shortId.charAt(i);
            if (!isAlphabetic(ch)) { // If the char is not A-Z or 0-9 then exit the loop.
                id = DECODE_ERROR;
                break;
            }

            id += chars.indexOf(ch) * Math.pow(base, power);
            power++;
        }

        return id;
    }

    public static String encode(int id) {
        if (id <= DECODE_ERROR) {
            return ENCODE_ERROR;
        }

        final char firstChar = chars.charAt(0);
        if (id == 0) {
            return String.valueOf(firstChar);
        }

        final StringBuilder shortId = new StringBuilder();
        while (id > 0) {
            shortId.append(chars.charAt(id % base));
            id /= base;
        }

        while (shortId.length() < shortLength) {
            shortId.append(firstChar); // Append the first character if the length is less than 5.
        }

        return shortId.toString();
    }

    private static boolean isAlphabetic(char ch) {
        return ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'Z'; // Check if the char is 0-9, A-Z.
    }

    public static boolean isDecodeOK(int id) {
        return id != DECODE_ERROR; // Check if the id is not equal to the decoding error.
    }

    public static boolean isEncodeOK(String shortId) {
        return !shortId.equalsIgnoreCase(ENCODE_ERROR); // Check if the shortId is not equal to the encoding error.
    }

    public static boolean isShortId(String shortId) {
        return shortId.chars().allMatch(ch -> ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'Z');
        // return Pattern.matches("^[\\dA-Z]{5}$", shortId); // Regular expression.
        // for (int i = 0; i < shortId.length(); i++) {
        // if (!isAlphabetic(shortId.charAt(i))) {
        // return false;
        // }
        // }
        // return true;
    }

    public static boolean isValidUrl(String url) {
        return urlValidator.isValid(url); // External Jar provide by Apache Commons.
    }
}
