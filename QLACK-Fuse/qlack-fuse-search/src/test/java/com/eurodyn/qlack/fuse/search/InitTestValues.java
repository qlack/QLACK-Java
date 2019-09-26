package com.eurodyn.qlack.fuse.search;

import com.eurodyn.qlack.fuse.search.dto.IndexingDTO;
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.UpdateMappingRequest;
import java.util.Arrays;

public class InitTestValues {

  public CreateIndexRequest createIndexRequest() {
    CreateIndexRequest createIndexRequest = new CreateIndexRequest();
    createIndexRequest.setName("name");
    createIndexRequest.setType("type");
    createIndexRequest.setIndexMapping("indexMapping");
    createIndexRequest.setStopwords(Arrays.asList("word1"));

    return createIndexRequest;
  }

  public UpdateMappingRequest updateMappingRequest() {
    UpdateMappingRequest updateMappingRequest = new UpdateMappingRequest();
    updateMappingRequest.setIndexName("indexName");
    updateMappingRequest.setTypeName("typeName");
    updateMappingRequest.setIndexMapping("indexMappings");
    updateMappingRequest.setAsync(true);

    return updateMappingRequest;
  }

  public IndexingDTO createIndexingDTO() {
    IndexingDTO indexingDTO = new IndexingDTO();
    indexingDTO.setIndex("indexName");
    indexingDTO.setType("typeName");
    indexingDTO.setId("id");
    indexingDTO.setSourceObject(new SerializableClass("id_obj"));
    indexingDTO.setConvertToJSON(true);

    return indexingDTO;
  }

}
