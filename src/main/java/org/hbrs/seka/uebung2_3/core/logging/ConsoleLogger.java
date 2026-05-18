package org.hbrs.seka.uebung2_3.core.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleLogger implements Logger {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Override
    public void sendLog(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println("++++ LOG: " + message + " (" + timestamp + ")");
    }

}