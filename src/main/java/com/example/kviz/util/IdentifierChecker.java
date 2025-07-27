package com.example.kviz.util;

import java.util.regex.Pattern;

public class IdentifierChecker {
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final String USERNAME_REGEX =
            "^[A-Za-z0-9_]{3,20}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);


    public enum IdentifierType {
        EMAIL,
        USERNAME,
        INVALID
    }

    public static boolean isEmail(String input) {
        return input != null && EMAIL_PATTERN.matcher(input).matches();
    }

    public static boolean isUsername(String input) {
        return input != null && USERNAME_PATTERN.matcher(input).matches();
    }

    public static IdentifierType identify(String input) {
        if (isEmail(input)) {
            return IdentifierType.EMAIL;
        } else if (isUsername(input)) {
            return IdentifierType.USERNAME;
        } else {
            return IdentifierType.INVALID;
        }
    }

}
