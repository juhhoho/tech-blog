package com.blog;

import lombok.Builder;

@Builder
public record Item(
        String title,
        String link,
        String description,
        String pubDate
) {
}
