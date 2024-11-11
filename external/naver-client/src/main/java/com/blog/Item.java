package com.blog;

public record Item(
        String title,
        String originallink,
        String description,
        String pubDate
) {
}
