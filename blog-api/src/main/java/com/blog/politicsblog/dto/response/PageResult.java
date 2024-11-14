package com.blog.politicsblog.dto.response;

import java.util.List;

public record PageResult<T>(int page, int size, int totalElements, List<T> contents) {
}
