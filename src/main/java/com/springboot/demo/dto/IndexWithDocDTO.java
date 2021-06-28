package com.springboot.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class IndexWithDocDTO {
    @NotNull(message = "索引不能为空")
    private String index;

    private String docId;

    @NotNull(message = "source不能为空")
    private Map<String, Object> docJson;
}
