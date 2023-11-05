package org.reader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class NumberReader implements Closeable, Iterator<Long> {
    /**
     * Main buffer to read from channel
     */
    ByteBuffer byteBuffer = ByteBuffer.allocate(20);
    /**
     * Position of byteBuffer to avoid reading of partial token
     */
    int bufferPosition = 0;
    /**
     * amount of bytes written to buffer
     */
    int read = 0;


    ByteBuffer nextByteBuffer = ByteBuffer.allocate(20);
    int nextPosition = 0;
    int nextRead = 0;
    boolean hasNextElement = false;


    /**
     * Buffer to store token
     */
    ByteBuffer tokenBuffer = ByteBuffer.allocate(20);
    /**
     * Current length of token
     */
    int tokenLength = 0;


    private final CharsetDecoder charsetDecoder = StandardCharsets.UTF_8.newDecoder();

    @Setter
    @Getter
    public SeekableByteChannel seekableByteChannel;

    public NumberReader(SeekableByteChannel seekableByteChannel) {
        this.seekableByteChannel = seekableByteChannel;
    }


    /**
     * Use after {@link #hasNext()}.
     *
     * @return The next element when called.
     * @throws NoSuchElementException if internal {@link #hasNextElement} check returned false.
     */
    @SneakyThrows
    public Long next() {
        if (!hasNextElement) {
            throw new NoSuchElementException();
        }

        for (; bufferPosition < byteBuffer.limit(); ) {
            if (byteBuffer.get(bufferPosition) == (byte) ',') {//found delimiter
                tokenBuffer.rewind(); //found delimiter
                ByteBuffer token = tokenBuffer.slice(0, tokenLength);
                tokenLength = 0;
                bufferPosition++;
                return Long.parseLong(String.valueOf(charsetDecoder.decode(token)));

            } else if (bufferPosition == (byteBuffer.limit() - 1)/* && (byteBuffer.limit() == read)*/) {//the end of a buffer
                tokenBuffer.put(byteBuffer.get(bufferPosition));
                tokenLength++;
                bufferPosition++;
                hasNextElement = false;

                if (read != -1) { //the end of a file
                    String token = String.valueOf(charsetDecoder.decode(tokenBuffer.slice(0, tokenLength)));
                    tokenLength = 0;
                    return Long.parseLong(token);
                }
                ByteBuffer token = tokenBuffer.slice(0, tokenLength);
                tokenLength = 0;
                return Long.parseLong(String.valueOf(charsetDecoder.decode(token)));
            } else { //just char
                tokenBuffer.put(byteBuffer.get(bufferPosition));
                tokenLength++;
                bufferPosition++;
            }
        }
        return Long.parseLong(String.valueOf(charsetDecoder.decode(tokenBuffer.slice(0, bufferPosition))));

    }


    /**
     * Check if there are more elements to read.<br><br>
     * Following possible situations may occur:<br>
     * 1. At the beginning of the buffer read channel, check if any elements were read and return true<br>
     * 2. Simply finding next element and return true<br>
     * 3. If during finding next element reached the end of the buffer and read is not -1, than read channel further. If channel has read any elements return true<br>
     */
    @SneakyThrows
    public boolean hasNext() {
        nextByteBuffer = byteBuffer;
        nextPosition = bufferPosition;
        nextRead = read;

        if (nextPosition == 0) {//if we are at the start of the buffer
            if ((nextRead = seekableByteChannel.read(nextByteBuffer)) > 0) { //if more than one element was read - read more elements and return true
                nextByteBuffer.flip();
                read = nextRead;
                byteBuffer = nextByteBuffer;
                bufferPosition = 0;
                return hasNextElement = true;
            }
        } else if (nextPosition < nextByteBuffer.limit()) { //if position is not at the end of the buffer - return true
            return hasNextElement = true;
        } else if (nextPosition == nextByteBuffer.limit() && (nextRead = seekableByteChannel.read(nextByteBuffer)) != -1) { //if we are at the end of buffer but not the end of the channel - read more elements and return true
            nextByteBuffer.flip();
            read = nextRead;
            byteBuffer = nextByteBuffer;
            bufferPosition = 0;
            return hasNextElement = true;
        }
        return hasNextElement = false;
    }

    @Override
    public void close() {

    }
}
