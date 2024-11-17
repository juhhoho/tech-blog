package com.blog.politicsnews.dto.response;

import java.util.List;

public record PageResult<T>(int page, int size, int totalElements, List<T> contents) {
}
