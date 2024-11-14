package com.blog.politicsblog.repository;

import com.blog.politicsblog.dto.response.PageResult;
import com.blog.politicsblog.dto.response.SearchResponse;
import org.springframework.data.repository.query.Param;

public interface PoliticsNewsRepository {
PageResult<SearchResponse> search(@Param("query")String query,
                                  @Param("page")int page,
                                  @Param("size")int size);
}
