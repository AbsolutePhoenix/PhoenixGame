package com.absolutephoenix.engine.logging;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
    private static final String RESET = "\u001B[0m";

    private static LogLevel currentLevel = LogLevel.INFO;
    private static BufferedWriter writer;
    private static boolean initialized = false;

    private Logger() {
    }

    public static void setLevel(LogLevel level) {
        currentLevel = level;
    }

    public static LogLevel getLevel() {
        return currentLevel;
    }

    public static void trace(String message) {
        log(LogLevel.TRACE, message);
    }

    public static void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public static void info(String message) {
        log(LogLevel.INFO, message);
    }

    public static void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public static void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public static void fatal(String message) {
        log(LogLevel.FATAL, message);
    }

    public static void log(LogLevel level, String message) {
        if (!shouldLog(level)) {
            return;
        }
        init();
        StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        String threadName = Thread.currentThread().getName();
        String method = caller.getClassName() + "-" + caller.getMethodName();
        if (method.contains("com.absolutephoenix."))
            method = method.replace("com.absolutephoenix.", "");
        String time = LocalDateTime.now().format(FORMATTER);
        String levelName = level.name();
        String baseMessage = String.format("[%s] [%s/%s/%s] : %s", time, levelName, threadName, method, message);
        // Console with color
        String coloredLevel = color(level) + levelName + RESET;
        String consoleMessage = String.format("[%s] [%s/%s/%s] : %s", time, coloredLevel, threadName, method, message);
        System.out.println(consoleMessage);
        // File without color
        try {
            writer.write(baseMessage);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean shouldLog(LogLevel level) {
        return level.getPriority() >= currentLevel.getPriority() && level != LogLevel.OFF && currentLevel != LogLevel.OFF;
    }

    private static String color(LogLevel level) {
        return switch (level) {
            case TRACE -> "\u001B[90m"; // bright black
            case DEBUG -> "\u001B[34m"; // blue
            case INFO -> "\u001B[32m";  // green
            case WARN -> "\u001B[33m";  // yellow
            case ERROR -> "\u001B[31m"; // red
            case FATAL -> "\u001B[35m"; // magenta
            default -> "";
        };
    }

    private static void init() {
        if (initialized) {
            return;
        }
        try {
            Path logDir = Path.of("logs");
            Files.createDirectories(logDir);
            rotateLogs(logDir);
            Path logFile = logDir.resolve("latest.log");
            writer = Files.newBufferedWriter(logFile,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void rotateLogs(Path logDir) throws IOException {
        for (int i = 3; i >= 1; i--) {
            Path src = logDir.resolve("latest.log" + (i == 1 ? "" : "." + (i - 1)));
            Path dest = logDir.resolve("latest.log." + i);
            if (Files.exists(src)) {
                Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}

