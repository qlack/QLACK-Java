package com.eurodyn.qlack.fuse.rules.util;

import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.rules.InitTestValues;
import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import java.util.ArrayList;
import java.util.List;
import org.drools.compiler.compiler.ConfigurableSeverityResult;
import org.drools.compiler.compiler.DroolsError;
import org.drools.compiler.compiler.DroolsErrorWrapper;
import org.drools.compiler.compiler.PackageBuilderErrors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderResult;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompileRulesUtilTest {

  @InjectMocks
  private CompileRulesUtil compileRulesUtil;

  @Mock
  private KnowledgeBase knowledgeBase;

  @Mock
  private KnowledgeBuilder knowledgeBuilder;

  private List<String> rules;


  @Before
  public void init() {
    InitTestValues initTestValues = new InitTestValues();
    knowledgeBase = initTestValues.createKnowledgeBase();
    rules = initTestValues.createRulesAdd();
  }

  @Test(expected = QRulesException.class)
  public void testCompileWithErrors() {
    when(knowledgeBuilder.hasErrors()).thenReturn(true);

    List<DroolsError> errors = new ArrayList<>();
    DroolsError error1 = new DroolsError() {
      @Override
      public String getMessage() {
        return "drools test error";
      }

      @Override
      public int[] getLines() {
        return new int[1];
      }
    };
    errors.add(error1);

    when(knowledgeBuilder.getErrors()).thenReturn(new PackageBuilderErrors(
        errors.toArray(new DroolsError[errors.size()])));

    compileRulesUtil.compileRules(rules, knowledgeBuilder);
  }

}
