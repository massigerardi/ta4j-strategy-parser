package org.ambulando.strategy.ta4j.parser;

import java.util.ArrayList;
import java.util.List;


public class ParserException extends Exception {

  private List<ParserError> errors;

  public ParserException(String message, List<ParserError> errors) {
    super(message);
    this.errors = errors;
  }

  public ParserException(Throwable e) {
    super("Parse Errors", e);
    this.errors = new ArrayList<>();
    this.errors.add(new ParserError(e.getMessage()));
  }

  public ParserException(List<ParserError> errors) {
    this("Parse Errors", errors);
  }

  public List<ParserError> getErrors()
  {
    return errors;
  }

}
