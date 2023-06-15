package org.reader;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LegacyReader {

    public static void infLoop(Path filePath) {
        while (true) {
            synchronized (filePath) {
                try {
                    filePath.wait(Duration.ofSeconds(5).toMillis());
                } catch (InterruptedException e) {
                    /*nothing to do*/
                }
            }
        }
    }

    @SneakyThrows
    public static void scannerMethod(Path filePath) {
        Scanner scanner = new Scanner(filePath).useDelimiter(",");
        while (scanner.hasNext()) {
            System.out.println(scanner.next());
        }
    }

    @SneakyThrows
    public static void byteReader(Path filePath) {
        byte[] fileBytes = Files.readAllBytes(filePath);
        List<String> loadedFile = Arrays.stream(new String(fileBytes).split(","))
                .collect(Collectors.toList());
        System.out.println(loadedFile);
    }

    @SneakyThrows
    public static void oldBufferedReader(Path filePath) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath)))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
