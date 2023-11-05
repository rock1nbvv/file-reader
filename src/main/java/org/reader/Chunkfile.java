package org.reader;

import lombok.Data;

import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;

@Data
public class Chunkfile {
    // TODO Add SeekableByteChannel to this class and use it in the collection. NumberReader should work with that collection
    private final Path filePath;
    SeekableByteChannel seekableByteChannel;

    int position = 0;
    int checkedPosition = 0;

    public Chunkfile(Path filePath) {
        this.filePath = filePath;
        //check next
    }

}
