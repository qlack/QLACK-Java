package com.eurodyn.qlack.fuse.lexicon.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {

    private Application application;

    @Before
    public void init() {
        application = new Application();
    }

    @Test
    public void idTest() {
        String id = "id";
        application.setId(id);
        assertEquals(id, application.getId());
    }

    @Test
    public void dbVersionTest() {
        Long dbVersion = 2629743L;
        application.setDbversion(dbVersion);
        assertEquals(dbVersion, (Long) application.getDbversion());
    }

    @Test
    public void symbolicNameTest() {
        String symbolicName = "symbolicName";
        application.setSymbolicName(symbolicName);
        assertEquals(symbolicName, application.getSymbolicName());
    }

    @Test
    public void executedOnTest() {
        Long executedOn = 2629743L;
        application.setExecutedOn(executedOn);
        assertEquals(executedOn, (Long) application.getExecutedOn());
    }
}
