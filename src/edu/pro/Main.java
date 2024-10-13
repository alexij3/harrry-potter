package edu.pro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static String cleanText(String url) throws IOException {
        Path filePath = Paths.get(url);
        if (Files.exists(filePath)) {
            String content = new String(Files.readAllBytes(filePath));
            content = content.replaceAll("[^A-Za-z ]"," ").toLowerCase(Locale.ROOT);
            return content;
        } else {
            throw new IOException("Файл не знайдено");
        }

    }

    public static void main(String[] args) throws IOException {
        System.out.println(getExecutionTime(countWords()));
    }

    private static Runnable countWords() throws IOException {
        return () -> {
            String[] words;
            try {
                words = extractWords();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Map<String, Long> wordsCount = Arrays.stream(words)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            wordsCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(30)
                    .forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue()));

            System.out.println("------");
        };
    }

    private static String[] extractWords() throws IOException {
        String content = cleanText("src/edu/pro/txt/harry.txt");

        String[] words = content.split(" +"); // 400 000

        Arrays.sort(words);
        return words;
    }

    public static long getExecutionTime(Runnable task) {
        LocalDateTime start = LocalDateTime.now();
        task.run();
        LocalDateTime finish = LocalDateTime.now();
        return ChronoUnit.MILLIS.between(start, finish);
    }

}
