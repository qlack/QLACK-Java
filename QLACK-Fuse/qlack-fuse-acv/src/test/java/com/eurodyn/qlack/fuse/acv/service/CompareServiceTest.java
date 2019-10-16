package com.eurodyn.qlack.fuse.acv.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.metamodel.object.GlobalId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompareServiceTest {

  @InjectMocks
  private CompareService compareService;

  @Mock
  private Javers javers;

  @Mock
  private VersioningService versioningService;

  @Mock
  private Diff diff;

  @Mock
  private List<ValueChange> valueChangeList;

  @Mock
  private GlobalId globalId;

  @Mock
  private Object object1;

  @Mock
  private Object object2;

  @Before
  public void init() {
    compareService = new CompareService(javers, versioningService);
  }

  @Test
  public void testCompare() {
    when(javers.compare(object1, object2)).thenReturn(diff);
    when(diff.getChangesByType(ValueChange.class)).thenReturn(valueChangeList);

    assertEquals(0, compareService.compare(object1, object2).size());
  }

  @Test
  public void compareVersionsTest() {
    when(versioningService.retrieveVersion(object1, 1L)).thenReturn(object2);
    when(versioningService.retrieveVersion(object1, 2L)).thenReturn(object1);

    when(javers.compare(object2, object1)).thenReturn(diff);
    when(diff.getChangesByType(ValueChange.class)).thenReturn(valueChangeList);

    assertEquals(Collections.emptyList(), compareService.compareVersions(object1, 1L, 2L));
  }

  @Test
  public void testHasChanges() {
    when(javers.compare(object1, object2)).thenReturn(diff);
    when(diff.hasChanges()).thenReturn(true);
    assertTrue(compareService.hasChanges(object1, object2));
  }

  @Test(expected = NullPointerException.class)
  public void hasChangesNullObj1Test() {
    compareService.hasChanges(null, object2);
  }

  @Test(expected = NullPointerException.class)
  public void hasChangesNullObj2Test() {
    compareService.hasChanges(object1, null);
  }

  @Test(expected = NullPointerException.class)
  public void compareNullObj1Test() {
    compareService.compare(null, object2);
  }

  @Test(expected = NullPointerException.class)
  public void compareNullObj2Test() {
    compareService.compare(object1, null);
  }

  @Test
  public void compareObjectWithVersionTest() {
    when(versioningService.retrieveVersion(object1, 1L)).thenReturn(object1);
    when(javers.compare(object1, object1)).thenReturn(diff);
    when(diff.getChangesByType(ValueChange.class)).thenReturn(valueChangeList);

    assertEquals(Collections.emptyList(), compareService.compareObjectWithVersion(object1, 1L));
  }

  @Test
  public void compareObjectWithLatestVersionTest() {
    when(versioningService.retrieveLatestVersion(object1)).thenReturn(object2);
    when(javers.compare(object2, object1)).thenReturn(diff);
    when(diff.getChangesByType(ValueChange.class)).thenReturn(valueChangeList);

    assertEquals(Collections.emptyList(),
        compareService.compareObjectWithLatestVersion(object1));
  }

  @Test
  public void convertToChangeDTOTest() {
    ValueChange valueChange = new ValueChange(globalId, "property", object1, object2);
    assertNotNull(compareService.convertToChangeDTO(valueChange));
  }

}
