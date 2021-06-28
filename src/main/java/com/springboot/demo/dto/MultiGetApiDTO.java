package com.springboot.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class MultiGetApiDTO {

    private List<GetApiDTO> getApiList;
}
