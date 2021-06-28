package com.springboot.demo.service.impl;


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
import com.springboot.demo.utils.JsonConverter;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchCorruptionException;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.GetSourceRequest;
import org.elasticsearch.client.core.GetSourceResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private Integer port;
//
//    @Value("${healthcloud.elasticsearch.host}")
//    private String healthcloudHost;
//
//    @Value("${healthcloud.elasticsearch.port}")
//    private Integer healthcloudPort;

    @Resource
    protected RestHighLevelClient client;

    protected static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

        // 默认缓冲限制为100MB，此处修改为30MB。
        builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

//    @Override
//    public String getIndices() {
//        String url = String.format("%s%s/%s", localEsHostUrl, WebConstants.CAT, WebConstants.INDICES);
//        HttpResponse<String> response;
//        log.info("url:{}", url);
//        response = Unirest
//                .get(url)
//                .asString();
//        log.info("response:" + response.getBody());
//        return response.getBody();
//    }


    @Override
    public String createIndex(IndexDTO dto) throws IOException {
//        HttpHost httpHost = new HttpHost(host, port, "http");
//        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(dto.getIndex());
            // 执行得到 response
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, COMMON_OPTIONS);
            log.info("出参：" + JsonConverter.toJson(createIndexResponse));
            // 关闭
            client.close();
        } catch (Exception e) {
            throw new ElasticsearchException("创建索引 {" + dto.getIndex() + "} 失败");
        }
        return "创建索引：" + dto.getIndex() + "成功";
    }

    @Override
    public String deleteIndex(IndexDTO dto) throws IOException {
//        HttpHost httpHost = new HttpHost(host, port, "http");
//        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(dto.getIndex());
        AcknowledgedResponse acknowledgedResponse = client.indices().delete(deleteIndexRequest, COMMON_OPTIONS);
        return JsonConverter.toJson(acknowledgedResponse);
    }

    @Override
    public String addDoc(IndexWithDocDTO dto) throws IOException {
        HttpHost httpHost = new HttpHost(host, port, "http");
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
        IndexRequest indexRequest = new IndexRequest(dto.getIndex());
        indexRequest.source(dto.getDocJson());
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        return JsonConverter.toJson(indexResponse);
    }

    @Override
    public String bulk(BulkApiDTO bulkApiDTO) throws IOException {
        List<IndexWithDocDTO> bulk = bulkApiDTO.getBulk();
        if (null != bulk && bulk.size() > 0) {
            HttpHost httpHost = new HttpHost(host, port, "http");
            RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
            for (IndexWithDocDTO indexWithDocDTO : bulk) {
                BulkRequest bulkRequest = new BulkRequest();
                //todo:批量处理
            }
        }
        return null;
    }

    @Override
    public String getApi(GetApiDTO dto) throws IOException {
        HttpHost httpHost = new HttpHost(host, port, "http");
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
        GetRequest getRequest = new GetRequest(dto.getIndex(), dto.getDocId());
        String[] includes;
        String[] excludes;
        if (null != dto.getIncludeList() && dto.getIncludeList().size() > 0) {
            includes = (String[]) dto.getIncludeList().toArray();
        } else {
            includes = Strings.EMPTY_ARRAY;
        }
        if (null != dto.getExcludeList() && dto.getExcludeList().size() > 0) {
            excludes = (String[]) dto.getExcludeList().toArray();
        } else {
            excludes = Strings.EMPTY_ARRAY;
        }
        FetchSourceContext fetchSourceContext =
                new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(fetchSourceContext);
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        return JsonConverter.toJson(getResponse);
    }

    @Override
    public String multiGetApi(MultiGetApiDTO multiGetApiDTO) throws IOException {
        HttpHost httpHost = new HttpHost(host, port, "http");
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
        List<GetApiDTO> getApiDTOList = multiGetApiDTO.getGetApiList();
        if (null != getApiDTOList && getApiDTOList.size() > 0) {
            MultiGetRequest multiGetRequest = new MultiGetRequest();
            for (GetApiDTO getApiDTO : getApiDTOList) {
                multiGetRequest.add(new MultiGetRequest.Item(getApiDTO.getIndex(), getApiDTO.getDocId()));
            }
            MultiGetResponse multiGetResponse = restHighLevelClient.mget(multiGetRequest, RequestOptions.DEFAULT);
            return JsonConverter.toJson(multiGetResponse);
        }
        return null;
    }

    @Override
    public String getSource(GetSourceDTO dto) throws IOException {
        HttpHost httpHost = new HttpHost(host, port, "http");
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
        GetSourceRequest getSourceRequest = new GetSourceRequest(dto.getIndex(), dto.getDocId());
        GetSourceResponse getSourceResponse = restHighLevelClient.getSource(getSourceRequest, RequestOptions.DEFAULT);
        return JsonConverter.toJson(getSourceResponse);
    }

    @Override
    public boolean existSource(GetApiDTO getApiDTO) throws IOException {
        HttpHost httpHost = new HttpHost(host, port, "http");
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
        GetRequest getRequest = new GetRequest(getApiDTO.getIndex(), getApiDTO.getDocId());
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        return restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
    }

    @Override
    public String delete(DeleteApiDTO dto) throws IOException {
        HttpHost httpHost = new HttpHost(host, port, "http");
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
        DeleteRequest deleteRequest = new DeleteRequest(dto.getIndex(), dto.getDocId());
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        return JsonConverter.toJson(deleteResponse);
    }

    @Override
    public String update(UpdateApiDTO dto) throws IOException {
        HttpHost httpHost = new HttpHost(host, port, "http");
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
        UpdateRequest updateRequest = new UpdateRequest(dto.getIndex(), dto.getDocId());
        GetApiDTO getApiDTO = new GetApiDTO();
        getApiDTO.setIndex(dto.getIndex());
        getApiDTO.setDocId(dto.getDocId());
        if (!existSource(getApiDTO)) {
            updateRequest.upsert(dto.getUpdateJson());
        } else {
            updateRequest.doc(dto.getUpdateJson());
        }
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        return JsonConverter.toJson(updateResponse);
    }

    @Override
    public String search(SearchApiDTO searchApiDTO) throws IOException {
        HttpHost httpHost = new HttpHost(host, port, "http");
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (null != searchApiDTO.getQuery()) {
            Map<String, Object> query = searchApiDTO.getQuery();
            Set<String> keySet = query.keySet();
            for (String key : keySet) {
                searchSourceBuilder.query(QueryBuilders.termQuery(key, query.get(key)));
            }
        } else {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        }
        searchSourceBuilder.from(null != searchApiDTO.getFrom() ? searchApiDTO.getFrom() : 0);
        searchSourceBuilder.size(null != searchApiDTO.getSize() ? searchApiDTO.getSize() : 5);
        if (null != searchApiDTO.getIndex()) {
            searchRequest.indices(searchApiDTO.getIndex());
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return JsonConverter.toJson(searchResponse);
    }
}
