package com.springboot.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkApiDTO {

    private String bulkType;

    private List<IndexWithDocDTO> bulk;
}
