package com.springboot.demo.service;

import com.springboot.demo.dto.BulkApiDTO;
import com.springboot.demo.dto.DeleteApiDTO;
import com.springboot.demo.dto.GetApiDTO;
import com.springboot.demo.dto.GetSourceDTO;
import com.springboot.demo.dto.IndexDTO;
import com.springboot.demo.dto.IndexWithDocDTO;
import com.springboot.demo.dto.MultiGetApiDTO;
import com.springboot.demo.dto.SearchApiDTO;
import com.springboot.demo.dto.UpdateApiDTO;

import java.io.IOException;

public interface ElasticSearchService {

//    String getIndices();

    String createIndex(IndexDTO indexDTO) throws IOException;

    String deleteIndex(IndexDTO indexDTO) throws IOException;

    String addDoc(IndexWithDocDTO indexWithDocDTO) throws IOException;

    String bulk(BulkApiDTO bulkApiDTO) throws IOException;

    String getApi(GetApiDTO getApiDTO) throws IOException;

    String multiGetApi(MultiGetApiDTO multiGetApiDTO) throws IOException;

    String getSource(GetSourceDTO getSourceDTO) throws IOException;

    boolean existSource(GetApiDTO getApiDTO) throws IOException;

    String delete(DeleteApiDTO deleteApiDTO) throws IOException;

    String update(UpdateApiDTO updateApiDTO) throws IOException;

    String search(SearchApiDTO searchApiDTO) throws IOException;
}
