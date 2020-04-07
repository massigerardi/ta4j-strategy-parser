package org.ambulando.strategy.ta4j.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.Strategy;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TA4JIndicatorsStrategyListenerTest {

  private Properties indicators = new Properties();

  private TA4JStrategyParser parser = new TA4JStrategyParser();

  @Before
  public void before() throws Exception {
    indicators.load(new FileInputStream(new File("src/test/resources/indicators.properties")));
  }

  @Test
  public void testAll() throws Exception {
    Enumeration<?> keys = indicators.propertyNames();
    Map<String, String> errors = new HashMap<>();
    
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      testIndicator(key, errors);
    }
    Assert.assertTrue(errors.toString(), errors.isEmpty());
  }

  private void testIndicator(String key, Map<String, String> errors) throws Exception {
    String input = indicators.getProperty(key);
    try {
      Strategy strategy = parser.parse(input, new BaseBarSeries(new ArrayList<>()));
      if (strategy == null) {
        errors.put(key, null);
      }
    } catch (ParserException e) {
      errors.put(key, e.getErrors().toString());
    }
  }



}
