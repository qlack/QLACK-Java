package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.eurodyn.qlack.fuse.search.dto.queries.QueryBoolean.BooleanType;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryBooleanTest {

  private QueryBoolean queryBoolean;

  @Before
  public void init() {
    queryBoolean = new QueryBoolean();
  }

  @Test
  public void termsTest() {
    Map<QuerySpec, BooleanType> terms = new HashMap<>();
    queryBoolean.setTerms(terms);
    assertEquals(terms, queryBoolean.getTerms());
  }

  @Test
  public void enumTermTest() {
    assertNotNull(queryBoolean.setTerm(new QueryStringSpecFieldNested(), BooleanType.MUST));
    assertNotNull(queryBoolean.setTerm(new QueryStringSpecFieldNested(), BooleanType.MUSTNOT));
    assertNotNull(queryBoolean.setTerm(new QueryStringSpecFieldNested(), BooleanType.SHOULD));
  }


}
