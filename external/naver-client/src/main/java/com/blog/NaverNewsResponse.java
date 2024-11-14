package com.blog;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class NaverNewsResponse{
    private int total;
    private int start;
    private int display;
    private String lastBuildDate;
    private List<Item> items;
}
