package com.eurodyn.qlack.fuse.search.service;

import com.eurodyn.qlack.fuse.search.dto.SearchHitDTO;
import com.eurodyn.qlack.fuse.search.dto.SearchResultDTO;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryBoolean;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryExists;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryExistsNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryMultiMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryRange;
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
import com.eurodyn.qlack.fuse.search.request.ScrollRequest;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Provides Elastic search related functionality
 *
 * @author European Dynamics SA.
 */
@Service
@Validated
@Log
public class SearchService {

  private static final String SCROLL_EXCEPTION = "Could not execute scroll query.";
  /**
   * Jackson object mapper
   */
  private ObjectMapper mapper;
  /**
   * The ES client injected by blueprint.
   */
  private ESClient esClient;

  @Autowired
  public SearchService(ESClient esClient) {
    this.esClient = esClient;
    this.mapper = new ObjectMapper();
  }

  /**
   * Searches for documents matching the given Query.
   *
   * @param dto contains the query information needed to proceed with the search
   * @return a dto containing the search results
   */
  public SearchResultDTO search(QuerySpec dto) {
    log.info(
        MessageFormat.format("Searching for documents with query {0}", dto));

    RestHighLevelClient client = esClient.getClient();

    SearchRequest searchRequest = new SearchRequest(dto.getIndices().toArray(new String[0]));
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    //set the size option that determines the number of search hits to return
    searchSourceBuilder.size(dto.getPageSize());

    //set the sorting fields
    dto.getQuerySort().getSortMap().forEach(searchSourceBuilder::sort);

    QueryBuilder queryBuilder = buildQuery(dto);
    //set the query
    searchSourceBuilder.query(queryBuilder);

    List<String> docValueFields = getDocValueFields(dto);
    if (!docValueFields.isEmpty()) {

      docValueFields.forEach(
          docValueField -> searchSourceBuilder.docValueField(docValueField, "use_field_mapping"));
    }

    if (!dto.isCountOnly()) {
      //add included / excluded fields
      if (!dto.getIncludes().isEmpty() || !dto.getExcludes().isEmpty()) {
        FetchSourceContext fetchSourceContext =
            new FetchSourceContext(true,
                dto.getIncludes().toArray(new String[0]),
                dto.getExcludes().toArray(new String[0]));
        searchSourceBuilder.fetchSource(fetchSourceContext);
      }

      //add aggregations
      if (dto.getAggregate() != null) {
        searchSourceBuilder.aggregation(AggregationBuilders.terms("agg").field(dto.getAggregate())
            .size(dto.getAggregateSize()));
      }

      //add scroll
      if (dto.getScroll() != null) {
        searchRequest.scroll(TimeValue.timeValueMinutes(dto.getScroll()));
      }
    }

    //add search source to the request
    searchRequest.source(searchSourceBuilder);

    //prints the executed query - useful when debugging.
    log.info(searchSourceBuilder.toString());

    try {
      SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
      return buildResultFrom(response, dto.isCountOnly(), dto.isIncludeAllSource(),
          dto.isIncludeResults());
    } catch (IOException e) {
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
    log.info(MessageFormat
        .format("Searching index {0} of {1} type for document with id {2}",
            indexName, typeName,
            id));

    try {
      GetRequest request = new GetRequest(indexName, typeName, id);
      GetResponse response = esClient.getClient().get(request, RequestOptions.DEFAULT);

      SearchHitDTO sh = new SearchHitDTO();
      sh.setType(response.getType());
      sh.setSource(response.getSourceAsString());
      sh.setId(response.getId());

      return sh;

    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Creates a ScrollRequest that can be used for searches with large results ("page" type
   * results).
   *
   * @param indexName the name of the index that the search will be performed
   * @param query the query of the search
   * @param maxResults the maximum number of hitList to be returned with each batch of results
   * @return a ScrollRequest to use in a search
   */
  public ScrollRequest prepareScroll(String indexName, QueryMatch query,
      int maxResults) {
    log.info(MessageFormat
        .format("Creating a ScrollRequest for index {0} with max {1} results",
            indexName,
            maxResults));
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
      log.log(Level.SEVERE, SCROLL_EXCEPTION, e);
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

    SearchScrollRequest searchScrollRequest =
        new SearchScrollRequest(scrollRequest.getScrollId())
            .scroll(TimeValue.timeValueMinutes(scrollRequest.getScroll()));

    try {
      SearchResponse scroll = esClient.getClient()
          .scroll(searchScrollRequest, RequestOptions.DEFAULT);
      SearchResultDTO result = buildResultFrom(scroll, false, true, true);
      result.setHasMore(!result.getHits().isEmpty());

      return result;
    } catch (IOException e) {
      log.log(Level.SEVERE, SCROLL_EXCEPTION, e);
      throw new SearchException(SCROLL_EXCEPTION, e);
    }
  }

  /**
   * Utility method that constructs the search query
   *
   * @param dto the dto that contains the query specs.
   */
  private QueryBuilder buildQuery(QuerySpec dto) {
    if (dto instanceof QueryBoolean) {
      return buildQueryBoolean(dto);
    } else if (dto instanceof QueryExists) {
      return QueryBuilders.existsQuery(((QueryExists) dto).getField());
    } else if (dto instanceof QueryExistsNested) {
      return QueryBuilders.nestedQuery(
          ((QueryExistsNested) dto).getPath(),
          QueryBuilders.existsQuery(((QueryExistsNested) dto).getField())
          , ScoreMode.None);
    } else if (dto instanceof QueryMatch) {
      return QueryBuilders.matchQuery(((QueryMatch) dto).getField(), ((QueryMatch) dto).getValue());
    } else if (dto instanceof QueryMultiMatch) {
      return QueryBuilders.multiMatchQuery(((QueryMultiMatch) dto).getValue(),
          ((QueryMultiMatch) dto).getFields());
    } else if (dto instanceof QueryRange) {
      return QueryBuilders
          .rangeQuery(((QueryRange) dto).getField())
          .from(((QueryRange) dto).getFromValue())
          .to(((QueryRange) dto).getToValue())
          .includeLower(true)
          .includeUpper(true);
    } else if (dto instanceof QueryString) {
      return QueryBuilders.queryStringQuery(((QueryString) dto).getQueryStringValue());
    } else if (dto instanceof QueryStringSpecField) {
      return buildQueryString(((QueryStringSpecField) dto).getValue(),
          ((QueryStringSpecField) dto).getField(), ((QueryStringSpecField) dto).getOperator());
    } else if (dto instanceof QueryStringSpecFieldNested) {
      return QueryBuilders.nestedQuery((
              (QueryStringSpecFieldNested) dto).getPath(),
          buildQueryString(((QueryStringSpecFieldNested) dto).getValue(),
              ((QueryStringSpecFieldNested) dto).getField(),
              ((QueryStringSpecFieldNested) dto).getOperator()),
          ScoreMode.None);
    } else if (dto instanceof QueryTerm) {
      return QueryBuilders.termQuery(((QueryTerm) dto).getField(), ((QueryTerm) dto).getValue());
    } else if (dto instanceof QueryTermNested) {
      return QueryBuilders.nestedQuery(((QueryTermNested) dto).getPath(),
          QueryBuilders.termQuery(((QueryTermNested) dto).getField(),
              ((QueryTermNested) dto).getValue()), ScoreMode.None);
    } else if (dto instanceof QueryTerms) {
      return QueryBuilders
          .termsQuery(((QueryTerms) dto).getField(), ((QueryTerms) dto).getValues());
    } else if (dto instanceof QueryTermsNested) {
      return QueryBuilders.nestedQuery(((QueryTermsNested) dto).getPath(), QueryBuilders
              .termsQuery(((QueryTermsNested) dto).getField(), ((QueryTermsNested) dto).getValues()),
          ScoreMode.None);
    } else if (dto instanceof QueryWildcard) {
      return QueryBuilders.wildcardQuery(((QueryWildcard) dto).getField(),
          ((QueryWildcard) dto).getWildcard());
    } else if (dto instanceof QueryWildcardNested) {
      return QueryBuilders.nestedQuery(((QueryWildcardNested) dto).getPath(),
          QueryBuilders.wildcardQuery(((QueryWildcardNested) dto).getField(),
              ((QueryWildcardNested) dto).getWildcard()), ScoreMode.None);
    } else if (dto instanceof SimpleQueryString) {
      return QueryBuilders.simpleQueryStringQuery(((SimpleQueryString) dto).getValue().toString())
          .field(((SimpleQueryString) dto).getField())
          .defaultOperator(Operator.fromString(((SimpleQueryString) dto).getOperator()));
    }

    return QueryBuilders.matchAllQuery();
  }

  /**
   * This method extends the functionality of the buildQuery method fot QueryBoolean classes.
   *
   * @param dto the dto object to be examined
   * @return the updated string builder
   */
  @SuppressWarnings("squid:S2692")
  private BoolQueryBuilder buildQueryBoolean(QuerySpec dto) {

    QueryBoolean query = (QueryBoolean) dto;
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    query.getTerms().forEach((querySpec, booleanType) -> {

      switch (booleanType) {
        case MUST:
          boolQueryBuilder.must(buildQuery(querySpec));
          break;
        case MUSTNOT:
          boolQueryBuilder.mustNot(buildQuery(querySpec));
          break;
        case SHOULD:
          boolQueryBuilder.should(buildQuery(querySpec));
          break;
      }
    });

    return boolQueryBuilder;
  }

  /**
   * This method extends the functionality of the buildQuery method fot QueryString classes.
   *
   * @param value the value that should be matched
   * @param field the field of the document that will be checked against the given value
   * @param operator the operator of the query (OR / AND).
   */
  private QueryStringQueryBuilder buildQueryString(Object value, String field, String operator) {
    return QueryBuilders.queryStringQuery(value.toString()).
        field(field).defaultOperator(Operator.fromString(operator));
  }

  private List<String> getDocValueFields(QuerySpec dto) {
    if (dto instanceof QueryTermNested) {
      return ((QueryTermNested) dto).getDocvalueFields();
    } else if (dto instanceof QueryTermsNested) {
      return ((QueryTermsNested) dto).getDocvalueFields();
    } else if (dto instanceof QueryStringSpecFieldNested) {
      return ((QueryStringSpecFieldNested) dto).getDocvalueFields();
    } else if (dto instanceof QueryWildcardNested) {
      return ((QueryWildcardNested) dto).getDocvalueFields();
    }

    return new ArrayList<>();

  }

  /**
   * Creates and returns a search result DTO based on the Elastic search response wrapper object
   * {@link SearchResponse)
   *
   * @param response an Elastic search response wrapper object
   * @param countOnly flag to indicate whether only the result count should be returned
   * @param includeAllSource flag to indicate whether the whole query response should be included
   * @param includeResults flag to indicate whether the results should be included
   * @return a {@link SearchResultDTO} containing the results of the Elastic search executed query
   */
  private SearchResultDTO buildResultFrom(SearchResponse response,
      boolean countOnly,
      boolean includeAllSource,
      boolean includeResults) {

    SearchResultDTO resultDTO = new SearchResultDTO();

    SearchHits hits = response.getHits();

    if (countOnly) {
      resultDTO.setBestScore(hits.getMaxScore());
      resultDTO.setExecutionTime(response.getTook().getMillis());
      resultDTO.setTimedOut(response.isTimedOut());
      resultDTO.setTotalHits(response.getHits().getTotalHits());
      resultDTO.setScrollId(response.getScrollId());
    } else {
      resultDTO.setTotalHits(response.getHits().getTotalHits());
    }

    resultDTO.setShardsFailed(response.getFailedShards());
    resultDTO.setShardsSuccessful(response.getSuccessfulShards());
    resultDTO.setShardsTotal(response.getTotalShards());

    if (!countOnly && includeAllSource) {
      try {
        resultDTO.setSource(mapper.writeValueAsString(response));
      } catch (JsonProcessingException e) {
        log.log(Level.SEVERE, "Could not serialize response.", e);
        throw new SearchException("Could not serialize response.", e);
      }
    }

    if (!countOnly && includeResults) {
      for (SearchHit hit : response.getHits().getHits()) {
        resultDTO.getHits().add(map(hit));
      }
    }

    Aggregations aggs = response.getAggregations();

    if (aggs != null) {
      Terms agg = aggs.get("agg");
      agg.getBuckets().forEach(bucket ->
          resultDTO.getAggregations().put(bucket.getKeyAsString(), bucket.getDocCount())
      );
    }

    return resultDTO;
  }

  /**
   * Maps a {@link SearchHit} object to a {@link SearchHitDTO}
   *
   * @param hit a {@link SearchHit} object
   * @return a {@link SearchHitDTO} object
   */
  private SearchHitDTO map(SearchHit hit) {
    SearchHitDTO sh = new SearchHitDTO();
    sh.setScore(hit.getScore());
    sh.setType(hit.getType());
    sh.setSource(hit.getSourceAsString());
    sh.setId(hit.getId());
    if (hit.getInnerHits() != null) {
      sh.setInnerHits(hit.getInnerHits().toString());
    }
    return sh;
  }
}
