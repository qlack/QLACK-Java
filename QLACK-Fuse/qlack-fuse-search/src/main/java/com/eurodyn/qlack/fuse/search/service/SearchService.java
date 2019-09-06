package com.eurodyn.qlack.fuse.search.service;

import com.eurodyn.qlack.fuse.search.dto.SearchHitDTO;
import com.eurodyn.qlack.fuse.search.dto.SearchResultDTO;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryBoolean;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryBoolean.BooleanType;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryMultiMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryRange;
import com.eurodyn.qlack.fuse.search.dto.queries.QuerySort;
import com.eurodyn.qlack.fuse.search.dto.queries.QuerySpec;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryString;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryStringSpecField;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryStringSpecFieldNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryTerm;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryTermNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryTerms;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryTermsNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryWildcard;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryWildcardNested;
import com.eurodyn.qlack.fuse.search.dto.queries.SimpleQueryString;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.mappers.request.InternalScollRequest;
import com.eurodyn.qlack.fuse.search.mappers.request.InternalSearchRequest;
import com.eurodyn.qlack.fuse.search.mappers.response.QueryResponse;
import com.eurodyn.qlack.fuse.search.mappers.response.QueryResponse.Aggregations.Agg.Bucket;
import com.eurodyn.qlack.fuse.search.mappers.response.QueryResponse.Hits.Hit;
import com.eurodyn.qlack.fuse.search.request.ScrollRequest;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Log
public class SearchService {

  private static final ObjectMapper mapper = new ObjectMapper();

  // The ES client injected by blueprint.
  private ESClient esClient;
  private AdminService adminService;

  @Autowired
  public SearchService(ESClient esClient, AdminService adminService) {
    this.esClient = esClient;
    this.adminService = adminService;
  }

  /**
   * Searches for documents matching the given Query.
   *
   * @param dto contains the query information needed to proceed with the search
   * @return a dto containing the search results
   */
  public SearchResultDTO search(QuerySpec dto) {
    log.info(MessageFormat.format("Searching for documents with query {0}", dto));
    StringBuilder endpointBuilder = new StringBuilder();

    endpointBuilder.append(processIndices(dto));
    endpointBuilder.append(processTypes(dto));
    endpointBuilder.append(dto.isCountOnly() ? "/_count" : "/_search");

    Map<String, String> params = new HashMap<>();
    InternalSearchRequest internalRequest = createRequest(dto, params);

    try {
      ContentType contentType = ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8"));
      Request request = new Request("GET", endpointBuilder.toString());
      params.forEach((key, value) -> request.addParameter(key, value));
      request.setEntity(new NStringEntity(mapper.writeValueAsString(internalRequest), contentType));
      Response response = esClient.getClient().getLowLevelClient().performRequest(request);
      QueryResponse queryResponse = getQueryResponse(response);

      SearchResultDTO result = buildResultFrom(queryResponse, dto.isCountOnly(), dto.isIncludeAllSource(), dto.isIncludeResults());

      if (!dto.isCountOnly()) {
        result.setHasMore(queryResponse.getHits().getTotal() > dto.getPageSize());
      }

      return result;
    } catch (IOException e) {
      log.log(Level.SEVERE, "Could not execute query.", e);
      throw new SearchException("Could not execute query.", e);
    }
  }

  /**
   * Searches for a document matching the given arguments.
   *
   * @param indexName the name of the index that the document is part of
   * @param typeName the name of the index type that the document is part of
   * @param id the id of the document
   * @return a wrapper containing the retrieved document
   */
  public SearchHitDTO findById(String indexName, String typeName, String id) {
    log.info(MessageFormat.format("Searching index {0} of {1} type for document with id {2}", indexName, typeName, id));
    String endpoint = indexName + "/" + typeName + "/" + id;
    try {
      Response response = esClient.getClient().getLowLevelClient().performRequest(new Request("GET", endpoint));
      if (response.getStatusLine().getStatusCode() == 200) {
        Hit hit = mapper.readValue(response.getEntity().getContent(), Hit.class);
        return map(hit);
      } else {
        return null;
      }
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Creates a ScrollRequest that can be used for searches with large results ("page" type results).
   *
   * @param indexName the name of the index that the serach will be performed
   * @param query the query of the search
   * @param maxResults the maximum number of hits to be returned with each batch of results
   * @return a ScrollRequest to use in a search
   */
  public ScrollRequest prepareScroll(String indexName, QueryMatch query, int maxResults) {
    log.info(MessageFormat.format("Creating a ScrollRequest for index {0} with max {1} results", indexName, maxResults));
    ScrollRequest scrollRequest = new ScrollRequest();
    try {

      SearchRequest searchRequest = new SearchRequest(indexName);
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

      searchSourceBuilder.query(QueryBuilders.matchQuery(query.getField(), query.getValue()));
      searchSourceBuilder.size(maxResults);
      searchRequest.source(searchSourceBuilder);
      searchRequest.scroll(TimeValue.timeValueMinutes(1L));
      SearchResponse searchResponse;

      searchResponse = esClient.getClient().search(searchRequest, RequestOptions.DEFAULT);
      String scrollId = searchResponse.getScrollId();
      scrollRequest.setScroll(1); //how long it should keep the 'search context' alive
      scrollRequest.setScrollId(scrollId);

    } catch (IOException e) {
      log.log(Level.SEVERE, "Could not execute scroll query.", e);
    }

    return scrollRequest;
  }

  /**
   * Performs a search that may have more than one results
   *
   * @param scrollRequest contains the search query and the multi-result configuration
   * @return a dto containing the results of the search
   */
  public SearchResultDTO scroll(ScrollRequest scrollRequest) {
    log.info(MessageFormat.format("Executing scroll query {0}", scrollRequest));
    InternalScollRequest internalRequest = new InternalScollRequest();
    internalRequest.setScroll(scrollRequest.getScroll().toString() + "m");
    internalRequest.setScrollId(scrollRequest.getScrollId());

    Response response;
    try {
      ContentType contentType = ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8"));
      Request request = new Request("POST", "_search/scroll");
      request.setEntity(new NStringEntity(mapper.writeValueAsString(internalRequest), contentType));
      response = esClient.getClient().getLowLevelClient().performRequest(request);

    } catch (IOException e) {
      log.log(Level.SEVERE, "Could not execute scroll query.", e);
      throw new SearchException("Could not execute scroll query.", e);
    }

    QueryResponse queryResponse = getQueryResponse(response);
    SearchResultDTO result = buildResultFrom(queryResponse, false, true, true);
    result.setHasMore(!result.getHits().isEmpty());

    return result;
  }

  private String processIndices(QuerySpec dto) {
    // This is done to remove duplicates
    String indicesEndpoint = "";
    List<String> indices = new ArrayList<>(new HashSet<>(dto.getIndices()));

    // If no indices are defined then search them all
    if (indices.isEmpty()) {
      indicesEndpoint = indicesEndpoint.concat("_all");
    }

    // append indices to the query
    for (String index : indices) {
      if (indices.indexOf(index) > 0) {
        indicesEndpoint = indicesEndpoint.concat(",");
      }
      indicesEndpoint = indicesEndpoint.concat(index);
    }
    return indicesEndpoint;
  }

  private String processTypes(QuerySpec dto) {
    // This is done to remove duplicates
    String typesEndpoint = "";
    List<String> types = new ArrayList<>(new HashSet<>(dto.getTypes()));

    // if no types are defined then search them all
    if (!types.isEmpty()) {
      typesEndpoint = typesEndpoint.concat("/");
    }

    // append types to the query
    for (String type : types) {
      if (types.indexOf(type) > 0) {
        typesEndpoint = typesEndpoint.concat(",");
      }
      typesEndpoint = typesEndpoint.concat(type);
    }
    return typesEndpoint;
  }

  private InternalSearchRequest createRequest(QuerySpec dto, Map<String, String> params) {
    QuerySort dtoSort = dto.getQuerySort();
    InternalSearchRequest internalRequest = new InternalSearchRequest();
    if (!dto.isCountOnly()) {
      internalRequest.setFrom(dto.getStartRecord());
      internalRequest.setSize(dto.getPageSize());
      internalRequest.setExplain(dto.isExplain());
      internalRequest.setSort(buildSort(dtoSort));

      if (dto.getScroll() != null) {
        params.put("scroll", dto.getScroll().toString() + "m");
      }

      if (dto.getAggregate() != null) {
        internalRequest.setSource(new ArrayList<>());
        internalRequest.getSource().add(dto.getAggregate());
        internalRequest.setAggs(buildAggregate(dto.getAggregate(), dto.getAggregateSize()));
      }
    }
    internalRequest.setQuery(buildQuery(dto));
    return internalRequest;
  }

  private String buildQuery(QuerySpec dto) {
    StringBuilder builder = new StringBuilder("{");

    if (dto instanceof QueryBoolean) {
      QueryBoolean query = (QueryBoolean) dto;
      builder.append("\"bool\" : {");

      Map<BooleanType, List<QuerySpec>> queriesMap = new HashMap<>();
      for (Entry<QuerySpec, BooleanType> entry : query.getTerms().entrySet()) {
        if (entry.getValue() != null) {
          queriesMap.putIfAbsent(entry.getValue(), new ArrayList<>());
          queriesMap.get(entry.getValue()).add(entry.getKey());
        }
      }

      boolean appendComa = false;
      for (Entry<BooleanType, List<QuerySpec>> entry : queriesMap.entrySet()) {
        if (appendComa) {
          builder.append(",");
        }
        if (BooleanType.MUSTNOT.equals(entry.getKey())) {
          builder.append("\"must_not\" : [");
        } else if (BooleanType.SHOULD.equals(entry.getKey())) {
          builder.append("\"should\" : [");
        } else {
          builder.append("\"must\" : [");
        }
        for (QuerySpec querySpec : entry.getValue()) {
          if (entry.getValue().indexOf(querySpec) > 0) {
            builder.append(",");
          }
          builder.append(buildQuery(querySpec));
        }
        builder.append("]");
        appendComa = true;
      }
      builder.append("}");
    } else if (dto instanceof QueryMatch) {
      QueryMatch query = (QueryMatch) dto;
      builder.append("\"match\" : { \"").append(query.getField()).append("\" : \"").append(query.getValue()).append("\" }");
    } else if (dto instanceof QueryMultiMatch) {
      QueryMultiMatch query = (QueryMultiMatch) dto;
      builder.append("\"multi_match\" : { \"query\" : \"").append(query.getValue()).append("\", \"fields\" : [");
      for (int i = 0; i < query.getFields().length; i++) {
        if (i > 0) {
          builder.append(", ");
        }
        builder.append("\"").append(query.getFields()[i]).append("\"");
      }
      builder.append("]}");
    } else if (dto instanceof QueryString) {
      QueryString query = (QueryString) dto;
      builder.append("\"query_string\" : { \"query\" : \"").append(query.getQueryString()).append("\"}");
    } else if (dto instanceof QueryTerm) {
      QueryTerm query = (QueryTerm) dto;

      builder.append("\"term\" : { \"").append(query.getField()).append("\" : \"").append(query.getValue())
        .append("\" }");
    } else if (dto instanceof QueryTermNested) {
      QueryTermNested query = (QueryTermNested) dto;
      builder.append("\"nested\" : { ").append("\"path\": \"").append(query.getPath()).append("\", \"query\": { ")
        .append("\"term\" : { \"").append(query.getField()).append("\" : \"").append(query.getValue())
        .append("\" }").append(" } , \"inner_hits\": {").append("\"_source\" : false, ")
        .append("\"docvalue_fields\" : [ \"").append(query.getDocvalueFields()).append("\"]").append("}}");
    } else if (dto instanceof QueryWildcard) {
      QueryWildcard query = (QueryWildcard) dto;

      builder.append("\"wildcard\" : { \"").append(query.getField()).append("\" : \"").append(query.getWildcard())
        .append("\" }");
    } else if (dto instanceof QueryWildcardNested) {
      QueryWildcardNested query = (QueryWildcardNested) dto;

      builder.append("\"nested\" : { ").append("\"path\": \"").append(query.getPath()).append("\", \"query\": { ")
        .append("\"wildcard\" : { \"").append(query.getField()).append("\" : \"")
        .append(query.getWildcard()).append("\" }").append(" } , \"inner_hits\": {")
        .append("\"_source\" : false, ").append("\"docvalue_fields\" : [ \"")
        .append(query.getDocvalueFields()).append("\"]").append("}}");
    } else if (dto instanceof QueryTerms) {
      QueryTerms query = (QueryTerms) dto;
      builder.append("\"terms\" : { \"").append(query.getField()).append("\" : [ ").append(query.getValues())
        .append(" ] }");
    } else if (dto instanceof QueryTermsNested) {
      QueryTermsNested query = (QueryTermsNested) dto;
      builder.append("\"nested\" : { ").append("\"path\": \"").append(query.getPath()).append("\", \"query\": { ")
        .append("\"terms\" : { \"").append(query.getField()).append("\" : [ ").append(query.getValues())
        .append(" ] }").append(" } , \"inner_hits\": {").append("\"_source\" : false, ")
        .append("\"docvalue_fields\" : [ \"").append(query.getDocvalueFields()).append("\"]").append("}}");
    } else if (dto instanceof QueryRange) {
      QueryRange query = (QueryRange) dto;
      builder.append("\"range\" : { \"").append(query.getField()).append("\" : { \"gte\" : \"")
        .append(query.getFromValue()).append("\" , \"lte\" : \"").append(query.getToValue())
        .append("\" } }");
    } else if (dto instanceof QueryStringSpecField) {
      QueryStringSpecField query = (QueryStringSpecField) dto;
      builder.append("\"query_string\" : { \"fields\" : [\"").append(query.getField())
        .append("\"] , \"query\" : \"").append(query.getValue()).append("\" , \"default_operator\" : \"")
        .append(query.getOperator()).append("\" }");
    } else if (dto instanceof QueryStringSpecFieldNested) {
      QueryStringSpecFieldNested query = (QueryStringSpecFieldNested) dto;
      builder.append("\"nested\" : { ").append("\"path\": \"").append(query.getPath()).append("\", \"query\": { ")
        .append("\"query_string\" : { \"fields\" : [\"").append(query.getField())
        .append("\"] , \"query\" : \"").append(query.getValue()).append("\" , \"default_operator\" : \"")
        .append(query.getOperator()).append("\" }").append(" } , \"inner_hits\": {")
        .append("\"_source\" : false, ").append("\"docvalue_fields\" : [ \"")
        .append(query.getDocvalueFields()).append("\"]").append("}}");
    } else if (dto instanceof SimpleQueryString) {
      SimpleQueryString query = (SimpleQueryString) dto;
      builder.append("\"simple_query_string\" : { \"fields\" : [\"").append(query.getField())
        .append("\"] , \"query\" : \"").append(query.getValue()).append("\" , \"default_operator\" : \"")
        .append(query.getOperator()).append("\" }");
    }
    return builder.append("}").toString().replace("\"null\"", "null");
  }

  private String buildAggregate(String aggregate, int aggregateSize) {
    return new StringBuilder("{").append("\"agg\" : {\"terms\" : {\"field\" : \"").append(aggregate)
      .append("\", \"size\" : ").append(aggregateSize).append(",\"order\" : {\"_term\" : \"desc\"}")
      .append("}}}").toString();
  }

  private String buildSort(QuerySort dto) {
    StringBuilder builder = new StringBuilder("[");
    for (Entry<String, String> entry : dto.getSortMap().entrySet()) {
      if (builder.length() > 1) {
        builder.append(',');
      }
      builder.append("{").append("\"").append(entry.getKey()).append("\"").append(" : {").append("\"order\"")
        .append(" : ").append("\"").append(entry.getValue()).append("\"").append("}").append("}");
    }
    builder.append("]");
    return builder.toString();
  }

  private SearchResultDTO buildResultFrom(QueryResponse queryResponse, boolean countOnly, boolean includeAllSource,
    boolean includeResults) {

    SearchResultDTO result = new SearchResultDTO();
    if (!countOnly) {
      result.setBestScore(queryResponse.getHits().getMaxScore());
      result.setExecutionTime(queryResponse.getTook());
      result.setTimedOut(queryResponse.isTimeOut());
      result.setTotalHits(queryResponse.getHits().getTotal());
      result.setScrollId(queryResponse.getScrollId());
    } else {
      result.setTotalHits(queryResponse.getCount());
    }
    result.setShardsFailed(queryResponse.getShards().getFailed());
    result.setShardsSuccessful(queryResponse.getShards().getSuccessful());
    result.setShardsTotal(queryResponse.getShards().getTotal());

    if (!countOnly && includeAllSource) {
      try {
        result.setSource(mapper.writeValueAsString(queryResponse));
      } catch (JsonProcessingException e) {
        log.log(Level.SEVERE, "Could not serialize response.", e);
        throw new SearchException("Could not serialize response.", e);
      }
    }

    if (!countOnly && includeResults) {
      for (Hit hit : queryResponse.getHits().getHits()) {
        result.getHits().add(map(hit));
      }
    }

    if (queryResponse.getAggregations() != null && queryResponse.getAggregations().getAgg() != null) {
      for (Bucket bucket : queryResponse.getAggregations().getAgg().getBuckets()) {
        result.getAggregations().put(bucket.getKeyAsString(), bucket.getDocCount());
      }
    }

    return result;
  }

  private SearchHitDTO map(Hit hit) {
    SearchHitDTO sh = new SearchHitDTO();
    sh.setScore(hit.getScore());
    sh.setType(hit.getType());
    sh.setSource(hit.getSource());
    sh.setId(hit.getId());
    sh.setInnerHits(hit.getInnerHits());
    return sh;
  }

  private QueryResponse getQueryResponse(Response response) {
    try {
      return mapper.readValue(response.getEntity().getContent(), QueryResponse.class);
    } catch (UnsupportedOperationException | IOException e) {
      log.log(Level.SEVERE, "Could not deserialize response.", e);
      throw new SearchException("Could not deserialize response.", e);
    }
  }
}
