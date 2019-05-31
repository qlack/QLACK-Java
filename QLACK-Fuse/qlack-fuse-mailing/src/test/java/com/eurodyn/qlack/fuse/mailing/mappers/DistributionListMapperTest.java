package com.eurodyn.qlack.fuse.mailing.mappers;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.model.Contact;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;


@RunWith(MockitoJUnitRunner.class)
public class DistributionListMapperTest {

  @InjectMocks
  private DistributionListMapperImpl distributionListMapperImpl;

  private InitTestValues initTestValues;
  private com.eurodyn.qlack.fuse.mailing.model.DistributionList distributionList;
  private com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO distributionListDTO;
  private List<DistributionList> distributionLists;
  private List<DistributionListDTO> distributionListsDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();

    distributionList = initTestValues.createDistributionList();
    distributionListDTO = initTestValues.createDistributionListDTO();
    distributionLists = initTestValues.createDistributionLists();
    distributionListsDTO = initTestValues.createDistributionListsDTO();
  }

  @Test
  public void testMapToDTOName() {
    DistributionListDTO distributionListDTO = distributionListMapperImpl.mapToDTO(distributionList);
    assertEquals(distributionList.getName(), distributionListDTO.getName());
  }

  @Test
  public void testMapToDTODescription() {
    DistributionListDTO distributionListDTO = distributionListMapperImpl.mapToDTO(distributionList);
    assertEquals(distributionList.getDescription(), distributionListDTO.getDescription());
  }

  @Test
  public void testMapToDTOContacts() {
    DistributionListDTO distributionListDTO = distributionListMapperImpl.mapToDTO(distributionList);
    assertEquals(distributionList.getContacts().size(), distributionListDTO.getContacts().size());
  }

  @Test
  public void testMapToDTOCreatedBy() {
    DistributionListDTO distributionListDTO = distributionListMapperImpl.mapToDTO(distributionList);
    assertEquals(distributionList.getCreatedBy(), distributionListDTO.getCreatedBy());
  }

  @Test
  public void testMapToDTOCreatedOn() {
    DistributionListDTO distributionListDTO = distributionListMapperImpl.mapToDTO(distributionList);
    assertEquals(distributionList.getCreatedOn(), distributionListDTO.getCreatedOn());
  }

  @Test
  public void testMapToDTOList() {
    distributionListsDTO = distributionListMapperImpl.mapToDTO(distributionLists);
    assertEquals(distributionLists.size(), distributionListsDTO.size());
  }

  @Test
  public void testMapToEntityName() {
    DistributionList distributionList = distributionListMapperImpl.mapToEntity(distributionListDTO);
    assertEquals(distributionListDTO.getName(), distributionList.getName());
  }

  @Test
  public void testMapToEntityDescription() {
    DistributionList distributionList = distributionListMapperImpl.mapToEntity(distributionListDTO);
    assertEquals(distributionListDTO.getDescription(), distributionList.getDescription());
  }

  @Test
  public void testMapToEntityContacts() {
    DistributionList distributionList = distributionListMapperImpl.mapToEntity(distributionListDTO);
    assertEquals(distributionListDTO.getContacts().size(), distributionList.getContacts().size());
  }

  @Test
  public void testMapToEntityCreatedBy() {
    DistributionList distributionList = distributionListMapperImpl.mapToEntity(distributionListDTO);
    assertEquals(distributionListDTO.getCreatedBy(), distributionList.getCreatedBy());
  }

  @Test
  public void testMapToEntityCreatedOn() {
    DistributionList distributionList = distributionListMapperImpl.mapToEntity(distributionListDTO);
    assertEquals(distributionListDTO.getCreatedOn(), distributionList.getCreatedOn());
  }

  @Test
  public void testMapToEntityList() {
    distributionLists = distributionListMapperImpl.mapToEntity(distributionListsDTO);
    assertEquals(distributionListsDTO.size(), distributionLists.size());
  }
}
