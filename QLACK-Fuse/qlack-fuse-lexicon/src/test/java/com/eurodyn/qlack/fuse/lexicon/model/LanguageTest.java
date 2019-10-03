package com.eurodyn.qlack.fuse.lexicon.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LanguageTest {

    private Language language;

    @Before
    public void init()
    {
        language = new Language();
    }

    @Test
    public void dbversionTest(){
        Long dbversion = 2629743L;
        language.setDbversion(dbversion);
        assertEquals(dbversion,(Long)language.getDbversion());
    }

    @Test
    public void templatesTest(){
        List templates = new ArrayList();
        language.setTemplates(templates);
        assertEquals(templates,language.getTemplates());
    }
}
