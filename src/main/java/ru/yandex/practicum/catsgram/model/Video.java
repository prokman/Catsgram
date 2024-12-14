package ru.yandex.practicum.catsgram.model;

import lombok.Data;

@Data
public class Video {
    private Long id;
    private Long postId;
    private String originalFileName;
    private String filePath;
}
