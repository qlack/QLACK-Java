package com.eurodyn.qlack.fuse.lexicon.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DataTest {

    private Data data;

    @Before
    public void init(){
        data = new Data();
    }

    @Test
    public void dbversionTest(){
        Long dbversion = 2629743L;
        data.setDbversion(dbversion);
        assertEquals(dbversion,(Long)data.getDbversion());
    }
}
