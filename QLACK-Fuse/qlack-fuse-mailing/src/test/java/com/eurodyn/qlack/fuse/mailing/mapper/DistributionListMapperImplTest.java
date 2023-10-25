package com.eurodyn.qlack.fuse.mailing.mapper;

import static org.junit.jupiter.api.Assertions.*;
import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DistributionListMapperImplTest {

  @InjectMocks
  private DistributionListMapperImpl distributionListMapper;

  private InitTestValues initTestValues;

  @BeforeEach
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
    assertNull(distributionListMapper.mapToDTO((DistributionList) null));

    List<DistributionListDTO> distributionListDTOS = distributionListMapper
      .mapToDTO((List<DistributionList>) null);
    assertNull(distributionListDTOS);
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
    assertNull(distributionListMapper.mapToEntity((DistributionListDTO) null));

    DistributionList distributionList = distributionListMapper
      .mapToEntity((DistributionListDTO) null);
    assertNull(distributionList);
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
    assertNull(distributionListMapper.mapToEntity((List<DistributionListDTO>) null));
    List<DistributionList> distributionLists = distributionListMapper
      .mapToEntity((List<DistributionListDTO>) null);
    assertNull(distributionLists);

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
