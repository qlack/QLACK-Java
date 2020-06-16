package com.eurodyn.qlack.fuse.rules.component;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class CamundaComponentTest {

    private static final List<Map<String, Object>> mapList = new ArrayList<>();
    @InjectMocks
    private CamundaComponent camundaComponent;

    @Before
    public void init() {
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map1.put("season", "Spring");
        map1.put("guestCount", 4);
        map2.put("season", "Fall");
        map2.put("guestCount", 2);
        mapList.add(map1);
        mapList.add(map2);
    }

    @Test
    public void serializeMapTest() {
        assertNotNull(CamundaComponent.serializeMap(mapList));
    }

    @Test
    public void deserializeTest() {
        assertNotNull(CamundaComponent
                .deserialize(CamundaComponent.serializeMap(mapList)));
    }
}
