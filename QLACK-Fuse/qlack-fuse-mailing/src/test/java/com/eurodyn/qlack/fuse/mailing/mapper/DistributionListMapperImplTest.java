package com.eurodyn.qlack.fuse.mailing.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DistributionListMapperImplTest {

  @InjectMocks
  private DistributionListMapperImpl distributionListMapper;

  private InitTestValues initTestValues;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    DistributionList distributionList = initTestValues.createDistributionList();
    DistributionListDTO distributionListDTO = distributionListMapper
      .mapToDTO(distributionList);
    assertEquals(distributionList.getCreatedBy(),
      distributionListDTO.getCreatedBy());

  }

  @Test
  public void mapToDTONullTest() {
    assertEquals(null,
      distributionListMapper.mapToDTO((DistributionList) null));

    List<DistributionListDTO> distributionListDTOS = distributionListMapper
      .mapToDTO((List<DistributionList>) null);
    assertEquals(null, distributionListDTOS);
  }

  @Test
  public void mapToDTOListTest() {
    List<DistributionList> distributionLists = new ArrayList<>();
    distributionLists.add(initTestValues.createDistributionList());
    List<DistributionListDTO> distributionListDTOS = distributionListMapper
      .mapToDTO(distributionLists);

    assertEquals(distributionLists.size(), distributionListDTOS.size());
  }

  @Test
  public void mapToEntityTest() {
    DistributionListDTO distributionListDTO = initTestValues
      .createDistributionListDTO();
    DistributionList distributionList = distributionListMapper
      .mapToEntity(distributionListDTO);
    assertEquals(distributionList.getCreatedBy(),
      distributionListDTO.getCreatedBy());

  }

  @Test
  public void mapToEntityNullTest() {
    assertEquals(null,
      distributionListMapper.mapToEntity((DistributionListDTO) null));

    DistributionList distributionList = distributionListMapper
      .mapToEntity((DistributionListDTO) null);
    assertEquals(null, distributionList);
  }

  @Test
  public void mapToEntityListTest() {
    List<DistributionListDTO> distributionListDTOS = new ArrayList<>();
    distributionListDTOS.add(initTestValues.createDistributionListDTO());
    List<DistributionList> distributionLists = distributionListMapper
      .mapToEntity(distributionListDTOS);

    assertEquals(distributionListDTOS.size(), distributionLists.size());
  }

  @Test
  public void mapToEntityListNullTest() {
    assertEquals(null,
      distributionListMapper.mapToEntity((List<DistributionListDTO>) null));
    List<DistributionList> distributionLists = distributionListMapper
      .mapToEntity((List<DistributionListDTO>) null);
    assertEquals(null, distributionLists);

  }


  @Test
  public void testContactDTOListToContactSet() {
    assertNull(distributionListMapper.contactDTOListToContactSet(null));
  }

  @Test
  public void testContactSetToContactList() {
    assertNull(distributionListMapper.contactSetToContactDTOList(null));
  }

  @Test
  public void testContactToContactDTO() {
    assertNull(distributionListMapper.contactToContactDTO(null));
  }

  @Test
  public void testContactDTOToContact() {
    assertNull(distributionListMapper.contactDTOToContact(null));
  }


}
