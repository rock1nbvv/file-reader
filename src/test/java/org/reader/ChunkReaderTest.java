package org.reader;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reader.utils.SortHelper;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ChunkReaderTest {

    @Test
    public void arraySorting() {
        Long[] arr = {10L, 80L, 30L, 90L, 40L};
        Long[] test = {10L, 30L, 40L, 80L, 90L};
        SortHelper.quickSort(arr);
        Assertions.assertArrayEquals(test, arr);
        }

    @Test
    @SneakyThrows
    public void splitOnChunks() {
        Path workDirectory = Path.of("src/test/resources/");
        Path filePath = workDirectory.resolve("data.csv");

        SeekableByteChannel seekableByteChannel = Files.newByteChannel(filePath);

        NumberReader numberReader = new NumberReader(seekableByteChannel);

        List<Path> chunkfiles = splitFileIntoChunks(numberReader, workDirectory);
        System.out.println();

        for (int i = 0; i < chunkfiles.size(); i++) {
            Files.deleteIfExists(chunkfiles.get(i));
        }
    }

    private List<Path> splitFileIntoChunks(NumberReader numberReader, Path workDirectory) throws IOException {
        List<Path> result = new ArrayList<>();
        List<Long> numbers = new ArrayList<>();
        int filesize = 0;

        while (numberReader.hasNext()) {
            if (filesize < 3) {
                Long next = numberReader.next();
                numbers.add(next);
                filesize++;
            } else {
                Long[] arr = numbers.toArray(new Long[0]);
                SortHelper.quickSort(arr);
                result.add(Files.writeString(
                        workDirectory.resolve(String.valueOf(UUID.randomUUID())),
                        Arrays.stream(arr).map(Object::toString)
                                .collect(Collectors.joining(",")),
                        StandardOpenOption.WRITE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.CREATE
                ));
                numbers.clear();
                filesize = 0;
            }
        }
        if (filesize > 0) {
            Long[] arr = numbers.toArray(new Long[0]);
            SortHelper.quickSort(arr);
            result.add(Files.writeString(
                    workDirectory.resolve(String.valueOf(UUID.randomUUID())),
                    Arrays.stream(arr).map(Object::toString)
                            .collect(Collectors.joining(",")),
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE
            ));
            numbers.clear();
        }
        return result;
    }
}
