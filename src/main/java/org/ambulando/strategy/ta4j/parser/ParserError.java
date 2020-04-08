package org.ambulando.strategy.ta4j.parser;

public class ParserError {

  int line;
  int charPositionInLine;
  String msg;


  public ParserError(String msg)
  {
    this(0, 0, msg);
  }
  public ParserError(int line, int charPositionInLine, String msg)
  {
    this.line = line;
    this.charPositionInLine = charPositionInLine;
    this.msg = msg;
  }


  public int getLine()
  {
    return line;
  }


  public int getCharPositionInLine()
  {
    return charPositionInLine;
  }


  public String getMsg()
  {
    return msg;
  }


  @Override public String toString()
  {
    StringBuilder builder = new StringBuilder("ParseError[");
    if (line>0) builder.append(line).append(",");
    if (charPositionInLine>0) builder.append(charPositionInLine).append(",");
    if (msg!=null) builder.append(msg);
    builder.append("]");
    return builder.toString();
  }
}
