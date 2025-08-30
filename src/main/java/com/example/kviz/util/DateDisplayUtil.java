package com.example.kviz.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateDisplayUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy 'at' HH:mm");

    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
