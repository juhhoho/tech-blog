package com.blog.politicsnews.repository.polisticnews;

import com.blog.pagination.PageResult;
import com.blog.politicsnews.dto.response.SearchResponse;
import org.springframework.data.repository.query.Param;

public interface PoliticsNewsRepository {
    PageResult<SearchResponse> search(@Param("query")String query,
                                  @Param("page")int page,
                                  @Param("size")int size);
}
