package org.reader.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateLargeTxt {
    private static final long TARGET_FILE_SIZE = 200L * 1024 * 1024; // 200MB
    private static final String FILE_NAME = "target/random_longs.txt";

    public static void main(String[] args) {
        Random random = new Random();
        long fileSize = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            while (fileSize < TARGET_FILE_SIZE) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < 100; i++) {
                    long randomLong = random.nextLong();
                    line.append(randomLong).append(",");
                }
                line.setLength(line.length() - 1); // Remove last comma
                line.append("\n");
                writer.write(line.toString());
                fileSize += line.length();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("CSV file generated: " + FILE_NAME);
    }
}
