package org.ambulando.strategy.ta4j.parser;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Indicator;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.num.Num;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * This class provides an implementation of {@link StrategyListener},
 */
public abstract class TA4JStrategyListenerImpl extends StrategyBaseListener
{

    protected Deque<Rule> ruleStack = new LinkedList<>();
    protected Deque<Indicator<Num>> indicatorStack = new LinkedList<>();
    protected Deque<Integer> timeFrameStack = new LinkedList<>();
    protected Deque<Operator> operatorStack = new LinkedList<>();
    private Strategy strategy;
    private List<ParserError> errors = new ArrayList<>();


    protected void addError(ParserError error)
    {
        errors.add(error);
    }


    public Strategy getStrategy()
    {
        return strategy;
    }


    public List<ParserError> getErrors()
    {
        return errors;
    }


    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation does nothing.</p>
     */
    @Override
    public void exitStrategy(StrategyParser.StrategyContext ctx)
    {
        try
        {
            Rule exitRule = ruleStack.pop();
            Rule enterRule = ruleStack.pop();
            strategy = new BaseStrategy(enterRule, exitRule);
        }
        catch (Exception e)
        {
            addError(new ParserError("Rules are not defined correctly".concat(e.getMessage() != null ? e.getMessage() : "")));
        }
    }


    @Override
    public void exitLogicExpression(StrategyParser.LogicExpressionContext ctx)
    {
        Rule simpleRule = null;
        Rule right = ruleStack.pop();
        Rule left = ruleStack.pop();
        Operator operator = operatorStack.pop();
        switch (operator)
        {
            case AND:
                simpleRule = left.and(right);
                break;
            case OR:
                simpleRule = left.or(right);
                break;
        }
        ruleStack.push(simpleRule);
    }


    @Override
    public void exitOp(StrategyParser.OpContext ctx)
    {
        operatorStack.push(Operator.get(ctx.getText()));
    }


    @Override
    public void exitLogicOp(StrategyParser.LogicOpContext ctx)
    {
        operatorStack.push(Operator.get(ctx.getText()));
    }


    @Override
    public void exitTimeframe(StrategyParser.TimeframeContext ctx)
    {
        Integer timeFrame = Integer.parseInt(ctx.getText());
        timeFrameStack.push(new Integer(timeFrame % 700));
    }


    @Override
    public void visitErrorNode(ErrorNode node)
    {
        errors.add(new ParserError(node.getText()));
    }


    protected enum Operator
    {
        GTE(">="), LTE("<="), AND("AND"), OR("OR"), EQ("=");
        private String operator;


        Operator(String op)
        {
            this.operator = op;
        }


        public static Operator get(String op)
        {
            switch (op)
            {
                case ">=":
                    return GTE;
                case "<=":
                    return LTE;
                case "OR":
                    return OR;
                case "AND":
                    return AND;
                default:
                    throw new IllegalArgumentException(op);
            }
        }
    }

}

