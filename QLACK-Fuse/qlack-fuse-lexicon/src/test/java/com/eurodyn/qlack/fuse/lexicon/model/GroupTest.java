package com.eurodyn.qlack.fuse.lexicon.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GroupTest {

    private Group group;

    @Before
    public void init()
    {
        group = new Group();
    }

    @Test
    public void dbversionTest(){
        Long dbversion = 2629743L;
        group.setDbversion(dbversion);
        assertEquals(dbversion,(Long)group.getDbversion());
    }

    @Test
    public void keysTest(){
        List keys = new ArrayList();
        group.setKeys(keys);
        assertEquals(keys,group.getKeys());
    }
}
