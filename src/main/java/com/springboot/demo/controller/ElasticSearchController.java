package com.springboot.demo.controller;

import com.springboot.demo.dto.BulkApiDTO;
import com.springboot.demo.dto.DeleteApiDTO;
import com.springboot.demo.dto.GetApiDTO;
import com.springboot.demo.dto.GetSourceDTO;
import com.springboot.demo.dto.IndexDTO;
import com.springboot.demo.dto.IndexWithDocDTO;
import com.springboot.demo.dto.MultiGetApiDTO;
import com.springboot.demo.dto.SearchApiDTO;
import com.springboot.demo.dto.UpdateApiDTO;
import com.springboot.demo.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/elasticSearch")
public class ElasticSearchController {

    @Autowired
    private ElasticSearchService elasticSearchService;

//    /**
//     * 获取索引
//     *
//     * @return
//     */
//    @GetMapping("/getIndices")
//    public String getIndices() {
//        return elasticSearchService.getIndices();
//    }

    /**
     * 创建索引
     *
     * @param dto
     * @return
     * @throws IOException
     */
    @PostMapping("createIndex")
    public String createIndex(@Validated @RequestBody IndexDTO dto) throws IOException {
        return elasticSearchService.createIndex(dto);
    }

    /**
     * 删除索引
     *
     * @param dto
     * @return
     * @throws IOException
     */
    @PostMapping("deleteIndex")
    public String deleteIndex(@Validated @RequestBody IndexDTO dto) throws IOException {
        return elasticSearchService.deleteIndex(dto);
    }

    /**
     * 添加数据
     *
     * @param indexWithDocDTO
     * @return
     */
    @PostMapping("addDoc")
    public String addDoc(@Validated @RequestBody IndexWithDocDTO indexWithDocDTO) throws IOException {
        return elasticSearchService.addDoc(indexWithDocDTO);
    }

    /**
     * 批量添加数据
     *
     * @param bulkApiDTO
     * @return
     */
    @PostMapping("bulk")
    public String bulk(@RequestBody BulkApiDTO bulkApiDTO) throws IOException {
        return elasticSearchService.bulk(bulkApiDTO);
    }

    /**
     * 获取信息
     *
     * @param getApiDTO
     * @return
     */
    @PostMapping("getApi")
    public String getAPI(@RequestBody GetApiDTO getApiDTO) throws IOException {
        return elasticSearchService.getApi(getApiDTO);
    }

    /**
     * 批量获取信息
     *
     * @param multiGetApiDTO
     * @return
     */
    @PostMapping("multiGetApi")
    public String multiGetApi(@RequestBody MultiGetApiDTO multiGetApiDTO) throws IOException {
        return elasticSearchService.multiGetApi(multiGetApiDTO);
    }

    /**
     * 获取Source
     *
     * @param getSourceDTO
     * @return
     */
    @PostMapping("getSource")
    public String getSource(@RequestBody GetSourceDTO getSourceDTO) throws IOException {
        return elasticSearchService.getSource(getSourceDTO);
    }

    /**
     * 资源是否存在
     *
     * @param getApiDTO
     * @return
     */
    @PostMapping("existSource")
    public boolean existSource(@RequestBody GetApiDTO getApiDTO) throws IOException {
        return elasticSearchService.existSource(getApiDTO);
    }

    /**
     * 删除资源
     *
     * @param deleteApiDTO
     * @return
     * @throws IOException
     */
    @PostMapping("delete")
    public String delete(@RequestBody DeleteApiDTO deleteApiDTO) throws IOException {
        return elasticSearchService.delete(deleteApiDTO);
    }

    /**
     * 更新资源
     *
     * @param updateApiDTO
     * @return
     * @throws IOException
     */
    @PostMapping("update")
    public String update(@RequestBody UpdateApiDTO updateApiDTO) throws IOException {
        return elasticSearchService.update(updateApiDTO);
    }

    /**
     * 搜索资源
     *
     * @param searchApiDTO
     * @return
     * @throws IOException
     */
    @PostMapping("search")
    public String searchApi(@RequestBody SearchApiDTO searchApiDTO) throws IOException {
        return elasticSearchService.search(searchApiDTO);
    }
}
