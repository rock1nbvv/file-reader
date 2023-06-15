package org.reader;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateFile {

    @SneakyThrows
    public static void main(String[] args) {
        long factorial = 1L;
        List<Long> numbers = new ArrayList<>();

//        for (long i = 2; i < 9223372036854775807L; i++) {
        for (long i = 2; i < 9223372L; i++) {
            factorial += i;
            numbers.add(factorial);
        }

        String collected = numbers.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        Path workDirectory = Path.of("src/main/resources/");
        Path filePath = workDirectory.resolve("test_data.csv");

        Files.writeString(
                filePath,
                collected,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE
        );
    }
}
