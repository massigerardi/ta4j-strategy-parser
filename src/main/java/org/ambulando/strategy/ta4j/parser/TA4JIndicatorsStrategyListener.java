package org.ambulando.strategy.ta4j.parser;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CCIIndicator;
import org.ta4j.core.indicators.DoubleEMAIndicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.HMAIndicator;
import org.ta4j.core.indicators.KAMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.PPOIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.TripleEMAIndicator;
import org.ta4j.core.indicators.UlcerIndexIndicator;
import org.ta4j.core.indicators.WMAIndicator;
import org.ta4j.core.indicators.WilliamsRIndicator;
import org.ta4j.core.indicators.ZLEMAIndicator;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.ConstantIndicator;
import org.ta4j.core.indicators.helpers.VolumeIndicator;
import org.ta4j.core.indicators.volume.AccumulationDistributionIndicator;
import org.ta4j.core.indicators.volume.ChaikinMoneyFlowIndicator;
import org.ta4j.core.indicators.volume.ChaikinOscillatorIndicator;
import org.ta4j.core.indicators.volume.IIIIndicator;
import org.ta4j.core.indicators.volume.MVWAPIndicator;
import org.ta4j.core.indicators.volume.NVIIndicator;
import org.ta4j.core.indicators.volume.OnBalanceVolumeIndicator;
import org.ta4j.core.indicators.volume.PVIIndicator;
import org.ta4j.core.indicators.volume.ROCVIndicator;
import org.ta4j.core.indicators.volume.VWAPIndicator;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

public class TA4JIndicatorsStrategyListener extends TA4JStrategyListenerImpl {

  private BarSeries barSeries;
  private ClosePriceIndicator closePrice;
  private VolumeIndicator volume;

  public TA4JIndicatorsStrategyListener(BarSeries barSeries) {
    assert barSeries != null : "BarSeries cannot be null";    
    this.barSeries = barSeries;
    this.closePrice = new ClosePriceIndicator(barSeries);
    this.volume = new VolumeIndicator(barSeries);
  }

  @Override
  public void exitSimpleExpression(StrategyParser.SimpleExpressionContext ctx) {
      Indicator<Num> right = indicatorStack.pop();
      Indicator<Num> left = indicatorStack.pop();
      Operator operator = operatorStack.pop();
      switch (operator) {
        case GTE:
          ruleStack.push(new OverIndicatorRule(left, right));
          break;
        case LTE:
          ruleStack.push(new UnderIndicatorRule(left, right));
          break;
      }
  }

  @Override
  public void exitConstantExpression(StrategyParser.ConstantExpressionContext ctx) {
    try {
      Indicator<Num> right = indicatorStack.pop();
      Indicator<Num> left = indicatorStack.pop();
      Operator operator = operatorStack.pop();
      switch (operator) {
        case GTE:
          ruleStack.push(new CrossedUpIndicatorRule(left, right));
          break;
        case LTE:
          ruleStack.push(new CrossedDownIndicatorRule(left, right));
          break;
      }
    } catch (Exception e) {
      //addError(ParserError.builder().msg(e.getMessage()).build());
    }
  }


  @Override
  public void exitEma(StrategyParser.EmaContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new EMAIndicator(closePrice, timeFrame));
  }

  @Override
  public void exitSma(StrategyParser.SmaContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new SMAIndicator(closePrice, timeFrame));
  }

  @Override
  public void exitDema(StrategyParser.DemaContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new DoubleEMAIndicator(closePrice, timeFrame));
  }

  @Override
  public void exitTema(StrategyParser.TemaContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new TripleEMAIndicator(closePrice, timeFrame));
  }

  @Override
  public void exitWilliams(StrategyParser.WilliamsContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new WilliamsRIndicator(barSeries, timeFrame));
  }

  @Override
  public void exitWma(StrategyParser.WmaContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new WMAIndicator(closePrice, timeFrame));
  }

  @Override
  public void exitCci(StrategyParser.CciContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new CCIIndicator(barSeries, timeFrame));

  }

  @Override
  public void exitRsi(StrategyParser.RsiContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new RSIIndicator(closePrice, timeFrame));
  }

  @Override
  public void exitCprice(StrategyParser.CpriceContext ctx) {
    indicatorStack.push(closePrice);
  }

  @Override
  public void exitVolume(StrategyParser.VolumeContext ctx) {
    indicatorStack.push(volume);
  }

  @Override
  public void exitUi(StrategyParser.UiContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new UlcerIndexIndicator(closePrice, timeFrame));
  }

  @Override
  public void exitAdx(StrategyParser.AdxContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new ADXIndicator(barSeries, timeFrame));
  }

  @Override
  public void exitHma(StrategyParser.HmaContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new HMAIndicator(closePrice, timeFrame));
  }

  @Override
  public void exitZlema(StrategyParser.ZlemaContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new ZLEMAIndicator(closePrice, timeFrame));
  }

  @Override
  public void exitKama(StrategyParser.KamaContext ctx) {
    Integer timeFrame3 = timeFrameStack.pop();
    Integer timeFrame2 = timeFrameStack.pop();
    Integer timeFrame1 = timeFrameStack.pop();
    indicatorStack.push(new KAMAIndicator(closePrice, timeFrame1, timeFrame2, timeFrame3));
  }

  @Override
  public void exitMacd(StrategyParser.MacdContext ctx) {
    Integer timeFrame2 = timeFrameStack.pop();
    Integer timeFrame1 = timeFrameStack.pop();
    indicatorStack.push(new MACDIndicator(closePrice, timeFrame1, timeFrame2));
  }

  @Override
  public void exitPpo(StrategyParser.PpoContext ctx) {
    Integer timeFrame2 = timeFrameStack.pop();
    Integer timeFrame1 = timeFrameStack.pop();
      indicatorStack.push(new PPOIndicator(closePrice, timeFrame1, timeFrame2));
  }

  @Override
  public void exitVwap(StrategyParser.VwapContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new VWAPIndicator(barSeries, timeFrame));
  }

  @Override
  public void exitCoi(StrategyParser.CoiContext ctx) {
    Integer timeFrame2 = timeFrameStack.pop();
    Integer timeFrame1 = timeFrameStack.pop();
    indicatorStack.push(new ChaikinOscillatorIndicator(barSeries, timeFrame1, timeFrame2));
  }

  @Override
  public void exitAdi(StrategyParser.AdiContext ctx) {
    indicatorStack.push(new AccumulationDistributionIndicator(barSeries));
  }

  @Override
  public void exitIii(StrategyParser.IiiContext ctx) {
    indicatorStack.push(new IIIIndicator(barSeries));
  }

  @Override
  public void exitCmf(StrategyParser.CmfContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new ChaikinMoneyFlowIndicator(barSeries, timeFrame));
  }

  @Override
  public void exitMvwap(StrategyParser.MvwapContext ctx) {
    Integer timeFrame2 = timeFrameStack.pop();
    Integer timeFrame1 = timeFrameStack.pop();
    VWAPIndicator vwap = new VWAPIndicator(barSeries, timeFrame1);
    indicatorStack.push(new MVWAPIndicator(vwap, timeFrame2));
  }

  @Override
  public void enterNvi(StrategyParser.NviContext ctx) {
    indicatorStack.push(new NVIIndicator(barSeries));
  }

  @Override
  public void exitRocv(StrategyParser.RocvContext ctx) {
    Integer timeFrame = timeFrameStack.pop();
    indicatorStack.push(new ROCVIndicator(barSeries, timeFrame));

  }

  @Override
  public void exitPvi(StrategyParser.PviContext ctx) {
    indicatorStack.push(new PVIIndicator(barSeries));
  }

  @Override
  public void exitObv(StrategyParser.ObvContext ctx) {
    indicatorStack.push(new OnBalanceVolumeIndicator(barSeries));
  }

  @Override
  public void exitValue(StrategyParser.ValueContext ctx) {
    indicatorStack.push(new ConstantIndicator<>(barSeries, DoubleNum.valueOf(ctx.getText())));
  }

}
