package com.springboot.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class UpdateApiDTO {
    @NotNull(message = "索引不能为空")
    private String index;

    @NotNull(message = "文档ID不能为空")
    private String docId;

    private Map<String, Object> updateJson;
}
