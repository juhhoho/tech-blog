package com.blog;

import java.util.List;

public record NaverNewsResponse(
        int total,
        int start,
        int display,
        String lastBuildDate,
        List<Item> items
) {
}
