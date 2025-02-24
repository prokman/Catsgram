package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        if (size < 0) {
            throw new ParameterNotValidException("size=" + size, "Некорректный размер выборки." +
                    " Размер должен быть больше нуля");
        }
        if (from < 0) {
            throw new ParameterNotValidException("from=" + from, "Некорректное начальное значение." +
                    " Начальное значение должно быть больше нуля");
        }

        if (!sort.equals("desc")) {
            throw new ParameterNotValidException("sort=" + sort, "Некорректное направление поиска." +
                    " направление поиска должно быть \"desc\" или \"asc\"");
        }
        return postService.findAll(from, size, sort);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }

    @GetMapping("/{postId}")
    public Optional<Post> findPostById(@PathVariable Long postId) {
        return postService.findPostById(postId);
    }
}