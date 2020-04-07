# ta4j Strategy Parser

**ta4j Strategy Parser** is a library to parse Technical Analysis strategies into [ta4j](https://github.com/ta4j/ta4j) using [antlr4](https://www.antlr.org/).

## Strategies

Strategies are defined as combination of `GO_LONG` and `GO_SHORT` combined signals.

A signal could be a combination of indicator expressions and logical expressions.

```
(((DEMA[5] >= SMA[45]) OR ((TEMA[30] <= 1.3) OR ((DEMA[74] >= 0.3637) OR (CCI[30] <= 0.5)))) AND ((TEMA[30] <= 1.3) OR ((DEMA[74] >= 0.3637) OR (CCI[30] <= 0.5)))) [GO_LONG] 
; 
((TEMA[30] <= 1.3) OR ((DEMA[74] >= 0.3637) OR (CCII[30] <= 0.5))) [GO_SHORT]",
```

Logical expressions must be inserted between parentheses

* correct
```
((DEMA[74] >= 0.3637) OR (CCII[30] <= 0.5))
```

* incorrect
```
(DEMA[74] >= 0.3637) OR (CCII[30] <= 0.5)
```


## Use

```java

import org.ambulando.strategy.ta4j.parser.*;

    ...
    String input = "(RSI[5] <= 7.5) [GO_LONG] ; (CCII[5] >= 7.9) [GO_SHORT]";
    try
    {
      BarSeries barSeries = getBarSeries();
      Strategy strategy = parser.parse(input, barSeries);
    }
    catch (ParserException e)
    {
      List<ParserError> errors = e.getErrors();
      //handle errors
    }
    ...
```

## Operators

* Logic operators between signals
    * `OR`
    * `AND`
* mathematical operators between indicators:
    * `>=`
    * `<=`


## Indicators

Use of the indicator can be found in [ta4j docs](https://oss.sonatype.org/service/local/repositories/releases/archive/org/ta4j/ta4j-core/0.13/ta4j-core-0.13-javadoc.jar/!/index.html)
At now, the following indicators are recognized by the parser:

|INDICATOR |USE|
|----------|---|
| [Close Price]()|`CPRICE`|
| [Volume]()|`VOLUME`|
| [Simple Moving Average - SMA](https://www.investopedia.com/terms/s/sma.asp)|`SMA[timeframe:int]`|
| [Double Exponential Moving Average - DEMA](https://www.investopedia.com/terms/d/double-exponential-moving-average.asp)|`DEMA[timeframe:int]`|
| [Exponential Moving Average - EMA](https://www.investopedia.com/terms/e/ema.asp)|`EMA[timeframe:int]`|
| [Triple Exponential Moving Average – TEMA](https://www.investopedia.com/terms/t/triple-exponential-moving-average.asp)|`TEMA[timeframe:int]`|
| [Weighted Moving Average WMA](https://www.investopedia.com/articles/technical/060401.asp)|`WMA[timeframe:int]`|
| [William's R - WILLIAMS](https://www.investopedia.com/terms/w/williamsr.asp)|`WILLIAMS[timeframe:int]`|
| [Commodity Channel Index - CCI](https://www.investopedia.com/terms/c/commoditychannelindex.asp)|`CCI[timeframe:int]`|
| [Relative Strength Index - RSI](https://www.investopedia.com/terms/r/rsi.asp)|`RSI[timeframe:int]`|
| [Ulcer Index - UI](https://www.investopedia.com/terms/u/ulcerindex.asp)|`UI[timeframe:int]`|
| [Average Directional Index - ADX](https://www.investopedia.com/terms/a/adx.asp)|`ADX[timeframe:int]`|
| [Hull Moving Average HMA](http://alanhull.com/hull-moving-average)|`HMA[timeframe:int]`|
| [Zero-Lag Exponential Moving Average - ZLEMA](http://www.fmlabs.com/reference/default.htm?url=ZeroLagExpMA.htm)|`ZLEMA[timeframe:int]`|
| [Kaufman's Adaptive Moving Average - KAMA](http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:kaufman_s_adaptive_moving_average)|`KAMA[timeframe:int,timeframe:int,timeframe:int]`|
| [Moving Average Convergence Divergence - MACD](http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:moving_average_convergence_divergence_macd)|`MACD[timeframe:int,timeframe:int]`|
| [Percentage price oscillator - PPO](https://www.investopedia.com/terms/p/ppo.asp)|`PPO[timeframe:int,timeframe:int]`|
| [Volume-Weighted Average Price - VWAP](http://www.investopedia.com/articles/trading/11/trading-with-vwap-mvwap.asp)|`VWAP[timeframe:int]`|
| [Chaikin Oscillator - COI](http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_oscillator)|`COI[timeframe:int,timeframe:int]`|
| [Accumulation Distribution - ADI](https://www.investopedia.com/terms/a/accumulationdistribution.asp)|`ADI`|
| [Intraday Intensity Index - III](https://www.investopedia.com/terms/i/intradayintensityindex.asp)|`III`|
| [Chaikin Money Flow - CMF](http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_money_flow_cmf)|`CMF[timeframe:int]`|
| [Moving Volume Weighted Average Price - MVWAP](http://www.investopedia.com/articles/trading/11/trading-with-vwap-mvwap.asp)|`MVWAP[timeframe:int,timeframe:int]`|
| [Negative Volume Index - NVI](http://www.investopedia.com/terms/n/nvi.asp)|`NVI`|
| [Rate Of Change of Volume - ROCV](https://www.investopedia.com/articles/technical/02/091002.asp)|`ROCV[timeframe:int]`|
| [Positive Volume Index - PVI](http://www.investopedia.com/terms/p/pvi.asp)|`PVI`|
| [On Balance Volume - OBV](https://www.investopedia.com/terms/o/onbalancevolume.asp)|`OBV`|


