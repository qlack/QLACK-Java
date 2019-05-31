package com.eurodyn.qlack.fuse.search.mappers;

import com.eurodyn.qlack.fuse.search.mappers.request.InternalCreateIndexRequest;
import com.eurodyn.qlack.fuse.search.mappers.request.InternalUpdateMappingRequest;
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.UpdateMappingRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import org.apache.http.nio.entity.NStringEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class CreateIndexRequestMapper {

  private ObjectMapper mapper = new ObjectMapper();

  @Mappings({
    @Mapping(source = "shards", target = "settings.index.numberOfShards"),
    @Mapping(source = "replicas", target = "settings.index.numberOfReplicas"),
    @Mapping(source = "indexMapping", target = "mappings"),
    @Mapping(source = "stopwords", target = "settings.analysis.filter.myStop.stopwords")
  })
  abstract InternalCreateIndexRequest mapToInternal(CreateIndexRequest request);

  public NStringEntity mapToNStringEntity(CreateIndexRequest createIndexRequest)
    throws JsonProcessingException, UnsupportedEncodingException {

    return new NStringEntity(mapper.writeValueAsString(mapToInternal(createIndexRequest)));
  }

  public NStringEntity mapToNStringEntity(UpdateMappingRequest updateMappingRequest)
    throws JsonProcessingException, UnsupportedEncodingException {

    InternalUpdateMappingRequest request = new InternalUpdateMappingRequest();
    request.setProperties(updateMappingRequest.getIndexMapping());

    return new NStringEntity(mapper.writeValueAsString(request));
  }
}
