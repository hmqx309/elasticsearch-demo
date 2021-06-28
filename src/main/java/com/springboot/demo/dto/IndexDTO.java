package com.springboot.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class IndexDTO {

    @NotNull(message = "索引名称不能为空")
    @Pattern(regexp = " ^[a-z]+$", message = "索引名称必须是小写字母")
    private String index;
}
