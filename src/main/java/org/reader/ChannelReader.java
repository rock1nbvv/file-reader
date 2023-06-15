package org.reader;

import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

public class ChannelReader {

    @SneakyThrows
    public static void readFile(Path workDirectory, Path filePath) {
        CharsetDecoder charsetDecoder = StandardCharsets.UTF_8.newDecoder();
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        Set<StandardOpenOption> options = new HashSet<>();
        options.add(StandardOpenOption.CREATE);
        options.add(StandardOpenOption.APPEND);

        try (SeekableByteChannel seekableByteChannel = Files.newByteChannel(filePath)) {
            ByteBuffer partialToken = ByteBuffer.allocate(128);
            int read = 0;
            int partialTokenRead = 0;
            int chunkCount = 0;
            int chunkWriteSize = 0;
            FileChannel fileChannel = FileChannel.open(workDirectory.resolve(chunkCount + ".csv"), options);
            while (read > -1) {
                read = seekableByteChannel.read(byteBuffer);
                byteBuffer.flip();
                for (int i = 0; i < read; i++) {
                    if (chunkWriteSize >= 5) {
                        fileChannel.close();
                        chunkCount++;
                        chunkWriteSize = 0;
                        fileChannel = FileChannel.open(workDirectory.resolve(chunkCount + ".csv"), options);
                    }
                    if (!(byteBuffer.get(i) == (byte) ',')) {
                        partialToken.put(byteBuffer.get(i));
                        partialTokenRead++;
                    } else {
                        if (chunkWriteSize < 4) {
                            partialToken.put(byteBuffer.get(i));
                            partialTokenRead++;
                        }
                        partialToken.rewind();
                        System.out.println(charsetDecoder.decode(partialToken.slice(0, partialTokenRead)));
//                        fileChannel.write(partialToken.slice(0, partialTokenRead));
                        chunkWriteSize++;
                        partialTokenRead = 0;
                        partialToken.rewind();
                    }
                    if ((i == read - 1) && byteBuffer.limit() < byteBuffer.capacity()) {
                        System.out.println(charsetDecoder.decode(partialToken.slice(0, partialTokenRead)));
//                        fileChannel.write(partialToken.slice(0, partialTokenRead));
                    }
                }
            }
        }
    }

}
