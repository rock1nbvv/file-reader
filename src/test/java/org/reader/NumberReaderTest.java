package org.reader;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NumberReaderTest {
    NumberReader numberReader;

    @Test
    public void emptyFileHandled() {
        TestData testData = initData("empty_data.csv");
        numberReader = new NumberReader(testData.seekableByteChannel);

        Assertions.assertThat(numberReader.hasNext()).isEqualTo(false);
        assertThatThrownBy(() -> numberReader.next()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void parseSingleElementFile() {
        TestData testData = initData("single_elem_data.csv");
        numberReader = new NumberReader(testData.seekableByteChannel);

        List<Long> numbers = new ArrayList<>();

        while (numberReader.hasNext()) {
            numbers.add(numberReader.next());
        }

        Assertions.assertThat(numbers).isEqualTo(
                List.of(105L)
        );
    }

    @Test
    public void wholeFileParsed() {
        TestData testData = initData("data.csv");
        numberReader = new NumberReader(testData.seekableByteChannel);

        List<Long> numbers = new ArrayList<>();

        while (numberReader.hasNext()) {
            Long next = numberReader.next();
            numbers.add(next);
        }

        Assertions.assertThat(numbers).isEqualTo(
                List.of(105L, 120L, 136L, 153L, 171L, 190L, 210L, 231L)
        );
    }

    @Test
    public void hasNextAtFileBeginning() {
        TestData testData = initData("data.csv");
        numberReader = new NumberReader(testData.seekableByteChannel);

        Assertions.assertThat(numberReader.hasNext()).isEqualTo(true);
    }

    @Test
    public void hasNextAtBufferEnd() {
        TestData testData = initData("data.csv");
        numberReader = new NumberReader(testData.seekableByteChannel);
        for (int i = 0; i < 5; i++) {
            if (numberReader.hasNext()) {
                System.out.println(numberReader.next());
            }
        }

        Assertions.assertThat(numberReader.hasNext()).isEqualTo(true);
    }

    @Test
    public void hasNextAtFileEnd() {
        TestData testData = initData("empty_data.csv");
        numberReader = new NumberReader(testData.seekableByteChannel);

        Assertions.assertThat(numberReader.hasNext()).isEqualTo(false);
    }

    @SneakyThrows
    private TestData initData(String fileName) {
        Path workDirectory = Path.of("src/test/resources/");
        Path filePath = workDirectory.resolve(fileName);
        SeekableByteChannel seekableByteChannel = Files.newByteChannel(filePath);

        return TestData.builder()
                .seekableByteChannel(seekableByteChannel)
                .build();
    }

    @Data
    @Builder
    private static class TestData {
        private SeekableByteChannel seekableByteChannel;
    }
}
