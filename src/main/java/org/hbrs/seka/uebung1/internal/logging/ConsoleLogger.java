package org.hbrs.seka.uebung1.internal.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleLogger implements Logger {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss:SS");

    @Override
    public void log(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println(timestamp + "-> " + message);
    }
}
