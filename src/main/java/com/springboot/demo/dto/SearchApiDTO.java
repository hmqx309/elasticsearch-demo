package com.springboot.demo.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SearchApiDTO {

    private String index;

    private Integer from;

    private Integer size;

    private Map<String, Object> query;
}
