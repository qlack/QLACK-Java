package com.eurodyn.qlack.fuse.lexicon.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class KeyTest {

    private Key key;

    @Before
    public void init(){
        key = new Key();
    }

    @Test
    public void dbversionTest(){
        Long dbversion = 2629743L;
        key.setDbversion(dbversion);
        assertEquals(dbversion,(Long)key.getDbversion());
    }
}
