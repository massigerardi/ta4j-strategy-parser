package org.ambulando.strategy.ta4j.parser;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.CCIIndicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.WMAIndicator;
import org.ta4j.core.indicators.helpers.ConstantIndicator;
import org.ta4j.core.indicators.helpers.CrossIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.trading.rules.OrRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.fail;

public class TA4JStrategyParserTest {

  private BarSeries barSeries;

  private final String WRONG_STRATEGY = "((EMA[30] >= WMA[87]) OR (CII[5] <= SA[87])) [GO_LONG] ; (CCI[5] <= 2.6) [GO_SHORT]";

  private final String WRONG_STRATEGY_2 = "(MACD[15,10] >= 10) [GO_LONG] ; (CCI[5] <= 2.6) [GO_SHORT]";

  private final String WRONG_STRATEGY_3 = "(KAMA[10, 20, 30] = WMA[87]) [GO_LONG] ; (CCI[5] = 2.6) [GO_SHORT]";

  private TA4JStrategyParser parser = new TA4JStrategyParser();

  Properties strategies = new Properties();

  @Before
  public void before() throws Exception {
    strategies.load(new FileInputStream(new File("src/test/resources/strategies.properties")));
    barSeries = new BaseBarSeries("test");
  }

  @Test
  public void testAllStrategies() throws Exception {
    Enumeration<?> keys = strategies.propertyNames();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      testStrategy(key);
    }
  }

  private Strategy testStrategy(String key) {
    String input = strategies.getProperty(key);
    Strategy strategy = null;
    try
    {
      strategy = parser.parse(input, barSeries);
    }
    catch (ParserException e)
    {
      fail(e.getErrors().toString());
    }
    Assert.assertNotNull(key, strategy);
    return strategy;
  }

  @Test
  public void testParseS11() throws Exception {
    testStrategy("STRATEGY_11");
  }

  @Test
  public void testParseS1() throws Exception {
    Strategy strategy = testStrategy("STRATEGY_1");
    Rule entryRule = strategy.getEntryRule();
    Assert.assertTrue(entryRule instanceof OrRule);
    Rule rule1 = (Rule) FieldUtils.readDeclaredField(entryRule, "rule1", true);
    checkRule(rule1, OverIndicatorRule.class, "first", EMAIndicator.class, "second", WMAIndicator.class);
    Rule rule2 = (Rule) FieldUtils.readDeclaredField(entryRule, "rule2", true);
    checkRule(rule2, UnderIndicatorRule.class, "first", CCIIndicator.class, "second", SMAIndicator.class);
    Rule exitRule = strategy.getExitRule();
    Indicator<Num> crossIndicator = (Indicator<Num>) FieldUtils.readDeclaredField(exitRule, "cross", true);
    checkRule(crossIndicator, CrossIndicator.class, "up", CCIIndicator.class, "low", ConstantIndicator.class);
  }

  private void checkRule(Object rule, Class ruleClass, String firstArgumentName, Class firstArgumentClass, String secondArgumentName, Class secondArgumentClass) throws Exception {
    Assert.assertTrue(rule.getClass().getSimpleName(), ruleClass.isAssignableFrom(rule.getClass()));
    Indicator<Num> first = (Indicator<Num>) FieldUtils.readDeclaredField(rule, firstArgumentName, true);
    Indicator<Num> second = (Indicator<Num>) FieldUtils.readDeclaredField(rule, secondArgumentName, true);
    Assert.assertTrue(first.getClass().getSimpleName(), firstArgumentClass.isAssignableFrom(first.getClass()));
    Assert.assertTrue(second.getClass().getSimpleName(), secondArgumentClass.isAssignableFrom(second.getClass()));

  }

  @Test
  public void testParseS2() throws Exception {
    Strategy strategy = testStrategy("STRATEGY_2");
    Rule entryRule = strategy.getEntryRule();
    checkRule(entryRule, OverIndicatorRule.class, "first", EMAIndicator.class, "second", WMAIndicator.class);
    Rule exitRule = strategy.getExitRule();
    Assert.assertTrue(exitRule instanceof OrRule);
    Rule rule1 = (Rule) FieldUtils.readDeclaredField(exitRule, "rule1", true);
    Indicator<Num> crossIndicator = (Indicator<Num>) FieldUtils.readDeclaredField(rule1, "cross", true);
    checkRule(crossIndicator, CrossIndicator.class, "up", CCIIndicator.class, "low", ConstantIndicator.class);
    Rule rule2 = (Rule) FieldUtils.readDeclaredField(exitRule, "rule2", true);
    checkRule(rule2, OverIndicatorRule.class, "first", CCIIndicator.class, "second", SMAIndicator.class);
  }

  @Test
  public void testParseWrong() {
    try {
      parser.parse(WRONG_STRATEGY, barSeries);
    } catch (ParserException e) {
      Assert.assertNotNull(e);
      List<ParserError> errors = e.getErrors();
      Assert.assertEquals(5, errors.size());
      Assert.assertTrue(errors.toString(), errors.toString().contains("token recognition error at: 'CI'"));
      Assert.assertTrue(errors.toString(), errors.toString().contains("token recognition error at: 'I['"));
      Assert.assertTrue(errors.toString(), errors.toString().contains("token recognition error at: 'SA'"));
      Assert.assertTrue(errors.toString(), errors.toString().contains("no viable alternative at input '(5'"));
      Assert.assertTrue(errors.toString(), errors.toString().contains("extraneous input ')' expecting '[GO_LONG]'"));
      return;
    }
    fail();
  }

  @Test
  public void testParseWrong2() {
    try {
      parser.parse(WRONG_STRATEGY_2, barSeries);
    } catch (ParserException e) {
      Assert.assertNotNull(e);
      List<ParserError> errors = e.getErrors();
      Assert.assertEquals(1, errors.size());
      Assert.assertEquals("Long term period count must be greater than short term period count", errors.get(0).getMsg());
      return;
    }
    fail();
  }

  @Test
  public void testParseWrong3() {
    try {
      parser.parse(WRONG_STRATEGY_3, barSeries);
    } catch (ParserException e) {
      Assert.assertNotNull(e);
      List<ParserError> errors = e.getErrors();
      Assert.assertEquals(4, errors.size());
      Assert.assertTrue(errors.toString(), errors.toString().contains("token recognition error at: '='"));
      Assert.assertTrue(errors.toString(), errors.toString().contains("no viable alternative at input '(KAMA[10, 20, 30]  WMA'"));
      Assert.assertTrue(errors.toString(), errors.toString().contains("no viable alternative at input '(CCI[5]  2.6'"));
      return;
    }
    fail();
  }

}
