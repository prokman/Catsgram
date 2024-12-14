package ru.yandex.practicum.catsgram.service;

import ru.yandex.practicum.catsgram.model.Post;


import java.util.Comparator;

public class PostComparator implements Comparator<Post> {


    @Override
    public int compare(Post o1, Post o2) {
        if (o1.getPostDate().equals(o2.getPostDate())) {
            return (int) (o1.getId() - o2.getId());
        } else {
            return o1.getPostDate().compareTo(o2.getPostDate());
        }
    }

    @Override
    public Comparator<Post> reversed() {
        return Comparator.super.reversed();
    }
}
