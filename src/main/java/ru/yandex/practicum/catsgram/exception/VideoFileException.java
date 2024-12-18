package ru.yandex.practicum.catsgram.exception;

import java.io.IOException;

public class VideoFileException extends RuntimeException {
    public VideoFileException(String message) {
        super(message);
    }

    public VideoFileException(String message, IOException e) {
        super(message, e);
    }

}
