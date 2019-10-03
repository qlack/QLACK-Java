package com.eurodyn.qlack.fuse.lexicon.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TemplateTest {

    private Template template;

    @Before
    public void init(){
        template = new Template();
    }

    @Test
    public void dbversionTest(){
        Long dbversion = 2629743L;
        template.setDbversion(dbversion);
        assertEquals(dbversion,(Long)template.getDbversion());
    }
}
