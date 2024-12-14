package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;

import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.*;

@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;

    public PostService(UserService userService) {
        this.userService = userService;
    }


    public Collection<Post> findAll(int from, int size, String sort) {
        List<Post> sortedPosts = new ArrayList<>();
        List<Post> lentaOfPosts = new ArrayList<>();
        if (sort.equals("asc")) {
            sortedPosts = posts.values().stream().sorted(new PostComparator()).toList();
        }
        if (sort.equals("desc")) {
            sortedPosts = posts.values().stream().sorted(new PostComparator().reversed()).toList();
        }
        for (int i = from; i < sortedPosts.size() && i < (from + size); i++) {
            lentaOfPosts.add(sortedPosts.get(i));
        }
        return lentaOfPosts;
    }

    public Post create(Post post) {
        // проверяем выполнение необходимых условий
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        if (userService.findUserByAuthorId(post).isEmpty()) {
            throw new ConditionsNotMetException("Автор с id = " + post.getAuthorId()
                    + " не найден");
        }

        // формируем дополнительные данные
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        // проверяем необходимые условия
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }


    public Optional<Post> findPostById(Long id) {
        return posts.values().stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
