package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        // проверяем выполнение необходимых условий
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        Boolean isDuplicate=users.keySet().stream()
                .anyMatch(id->user.getEmail().equals(users.get(id).getEmail()));
        if (isDuplicate) throw new DuplicatedDataException("Этот имейл уже используется");

        // формируем дополнительные данные
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            boolean isDuplicate = users.keySet().stream()
                    .anyMatch(id->users.get(id).getEmail().equals(newUser.getEmail()));
            if (isDuplicate) throw new DuplicatedDataException("Этот имейл уже используется");
            User oldUser = users.get(newUser.getId());
            /*if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }*/
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            if (newUser.getUsername()!=null) {
                oldUser.setUsername(newUser.getUsername());
            }
            if (newUser.getEmail()!=null) {
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getPassword()!=null) {
                oldUser.setPassword(newUser.getPassword());
            }
            //oldUser.setDescription(newPost.getDescription());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }



}
