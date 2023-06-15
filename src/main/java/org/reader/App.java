package org.reader;

import lombok.SneakyThrows;

import java.nio.file.Path;

import static org.reader.ChannelReader.readFile;

public class App {


    @SneakyThrows
    public static void main(String[] args) {
        Path workDirectory = Path.of("src/main/resources/");
        Path filePath = workDirectory.resolve("test_data.csv");

        readFile(workDirectory, filePath);
    }
}