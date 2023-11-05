package org.reader;

import lombok.SneakyThrows;

import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.reader.ChannelReader.splitFile;

public class App {

    @SneakyThrows
    public static void main(String[] args) {
        Path workDirectory = Path.of("src/main/resources/");
        Path filePath = workDirectory.resolve("test_data.csv");

//        splitFile(workDirectory, filePath);

//        SeekableByteChannel seekableByteChannel = Files.newByteChannel(filePath);
//
//        NumberReader numberReader = new NumberReader(seekableByteChannel);
//        while (numberReader.hasNext()){
//            System.out.println(numberReader.next());
//    }


//        SeekableByteChannel seekableByteChannel;
//        NumberReader numberReader;
//        Chunkfile chunkFile = new Chunkfile(filePath);
//        while (chunkFile.isHasNext()) {
//            seekableByteChannel = Files.newByteChannel(chunkFile.getFilePath());
//            numberReader = new NumberReader(seekableByteChannel);
//            if(numberReader.hasNext()) {
//                System.out.println(numberReader.next());
//            } else {
//                chunkFile.hasNext = false;
//            }
//        }
    }
}
