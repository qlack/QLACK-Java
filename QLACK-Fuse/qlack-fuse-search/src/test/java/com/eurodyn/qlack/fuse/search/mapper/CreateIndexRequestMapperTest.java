package com.eurodyn.qlack.fuse.search.mapper;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.search.InitTestValues;
import com.eurodyn.qlack.fuse.search.mapper.request.InternalUpdateMappingRequest;
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.UpdateMappingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.codec.Charsets;
import org.apache.http.nio.entity.NStringEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreateIndexRequestMapperTest {

  @InjectMocks
  private CreateIndexRequestMapperImpl createIndexRequestMapper;

  private ObjectMapper mapper;

  private InitTestValues initTestValues;

  @Before
  public void init() {
    mapper = new ObjectMapper();
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToNStringEntityCreateTest()
    throws IOException {
    CreateIndexRequest createIndexRequest = initTestValues.createIndexRequest();

    String expectedContent =
      mapper.writeValueAsString(
        createIndexRequestMapper.mapToInternal(createIndexRequest));

    NStringEntity result = createIndexRequestMapper
      .mapToNStringEntity(createIndexRequest);
    assertEquals(expectedContent, CharStreams.toString(new InputStreamReader(
      result.getContent(), Charsets.UTF_8)));
  }

  @Test
  public void mapToNStringEntityCreateNullWordsTest()
    throws IOException {
    CreateIndexRequest createIndexRequest = initTestValues.createIndexRequest();
    createIndexRequest.setStopwords(null);

    String expectedContent =
      mapper.writeValueAsString(
        createIndexRequestMapper.mapToInternal(createIndexRequest));

    NStringEntity result = createIndexRequestMapper
      .mapToNStringEntity(createIndexRequest);
    assertEquals(expectedContent, CharStreams.toString(new InputStreamReader(
      result.getContent(), Charsets.UTF_8)));
  }

  @Test
  public void mapToNStringEntityCreateNullTest()
    throws IOException {
    String expectedContent =
      mapper.writeValueAsString(createIndexRequestMapper.mapToInternal(null));

    NStringEntity result = createIndexRequestMapper
      .mapToNStringEntity((CreateIndexRequest) null);
    assertEquals(expectedContent, CharStreams.toString(new InputStreamReader(
      result.getContent(), Charsets.UTF_8)));
  }

  @Test
  public void mapToNStringEntityCreateNullFieldsTest() {
    assertEquals(null,
      createIndexRequestMapper.createIndexRequestToIndex(null));
    assertEquals(null,
      createIndexRequestMapper.createIndexRequestToMyStop(null));
    assertEquals(null,
      createIndexRequestMapper.createIndexRequestToFilter(null));
    assertEquals(null,
      createIndexRequestMapper.createIndexRequestToAnalysis(null));
    assertEquals(null,
      createIndexRequestMapper.createIndexRequestToSettings(null));
  }

  @Test
  public void mapToNStringEntityUpdateTest() throws IOException {
    UpdateMappingRequest updateMappingRequest = initTestValues
      .updateMappingRequest();

    InternalUpdateMappingRequest request = new InternalUpdateMappingRequest();
    request.setProperties(updateMappingRequest.getIndexMapping());
    String expectedContent =
      mapper.writeValueAsString(request);

    NStringEntity result = createIndexRequestMapper
      .mapToNStringEntity(updateMappingRequest);
    assertEquals(expectedContent, CharStreams.toString(new InputStreamReader(
      result.getContent(), Charsets.UTF_8)));
  }

}
