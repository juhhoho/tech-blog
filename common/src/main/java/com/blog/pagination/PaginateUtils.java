package com.blog.pagination;

import java.util.Collections;
import java.util.List;

public class PaginateUtils {
    /**
     * 리스트를 페이징 처리하여 PageResult를 반환하는 유틸리티 메서드
     *
     * @param content 원본 리스트
     * @param page    페이지 번호 (1-based)
     * @param size    페이지 크기
     * @param mapper  변환 함수 (요소를 다른 객체로 매핑)
     * @param <T>     원본 리스트의 요소 타입
     * @param <R>     결과 리스트의 요소 타입
     * @return PageResult<R> 페이징 결과
     */
    public static <T, R> PageResult<R> paginate(List<T> content, int page, int size, java.util.function.Function<T, R> mapper) {
        int totalElements = content.size();
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        // 페이지네이션 범위가 올바르지 않은 경우 빈 결과 반환
        if (startIndex >= totalElements) {
            return new PageResult<>(page, size, totalElements, Collections.emptyList());
        }

        // 필요한 데이터 추출 및 변환
        List<R> paginatedContents = content.subList(startIndex, endIndex).stream()
                .map(mapper)
                .toList();

        return new PageResult<>(page, size, totalElements, paginatedContents);
    }
}
