grammar Strategy;

strategy
    : go_long SEMICOLON go_short
    ;

go_long
    : signal GO_LONG
    ;
    
go_short
    : signal GO_SHORT
    ;

signal
    : LPAREN? simpleExpression RPAREN?
    | LPAREN? constantExpression RPAREN?
    | LPAREN logicExpression RPAREN 
    ;

logicExpression
    : signal logicOp signal
    ;

constantExpression
    : indicator op value
    ;

simpleExpression
    : indicator op indicator
    ;

indicator
    : sma
    | ema
    | dema
    | tema
    | williams
    | wma
    | cci
    | rsi
    | cprice
    | volume
    | ui
    | adx
    | adi
    | hma
    | zlema
    | kama
    | macd
    | vwap
    | mvwap
    | cmf
    | coi
    | iii
    | nvi
    | obv
    | pvi
    | rocv
    | ppo
    ;

sma
    : 'SMA' LBRACKET timeframe RBRACKET
    ;

ema
    : 'EMA' LBRACKET timeframe RBRACKET
    ;

dema
    : 'DEMA' LBRACKET timeframe RBRACKET
    ;

tema
    : 'TEMA' LBRACKET timeframe RBRACKET
    ;

williams
    : 'WILLIAMS' LBRACKET timeframe RBRACKET
    ;

wma
    : 'WMA' LBRACKET timeframe RBRACKET
    ;

cci
    : 'CCI' LBRACKET timeframe RBRACKET
    ;

rsi
    : 'RSI' LBRACKET timeframe RBRACKET
    ;

kama
    : 'KAMA' LBRACKET timeframe ',' timeframe ',' timeframe RBRACKET
    ;

macd
    : 'MACD' LBRACKET timeframe ',' timeframe RBRACKET
    ;

vwap
    : 'VWAP' LBRACKET timeframe RBRACKET
    ;

cmf
    : 'CMF' LBRACKET timeframe RBRACKET
    ;

coi
    : 'COI' LBRACKET timeframe ',' timeframe RBRACKET
    ;

mvwap
    : 'MVWAP' LBRACKET timeframe ',' timeframe RBRACKET
    ;

rocv
    : 'ROCV' LBRACKET timeframe RBRACKET
    ;

ui
    : 'UI' LBRACKET timeframe RBRACKET
    ;

adx
    : 'ADX' LBRACKET timeframe RBRACKET
    ;

hma
    : 'HMA' LBRACKET timeframe RBRACKET
    ;

zlema
    : 'ZLEMA' LBRACKET timeframe RBRACKET
    ;

ppo
    : 'PPO' LBRACKET timeframe ',' timeframe RBRACKET
    ;

cprice
    : 'CPRICE'
    ;

volume
    : 'VOLUME'
    ;

adi
    : 'ADI'
    ;

nvi
    : 'NVI'
    ;

obv
    : 'OBV'
    ;

pvi
    : 'PVI'
    ;

iii
    : 'III'
    ;

value
    : INTEGER
    | FLOAT
    ;

timeframe
    : INTEGER
    ;

op
    : GTE
    | LTE
    ;


logicOp
    : 'OR'
    | 'AND'
    ;

INTEGER : DIGIT+ ;

FLOAT : DIGIT+ ('.' DIGIT+)? ;

SEMICOLON
    : ';'
    ;

LBRACKET
   : '['
   ;

RBRACKET
   : ']'
   ;


LPAREN
   : '('
   ;

RPAREN
   : ')'
   ;


GO_LONG
    : '[GO_LONG]'
    ;

GO_SHORT
    : '[GO_SHORT]'
    ;

GTE
   : '>='
   ;


LTE
   : '<='
   ;


WS
   : [ \r\n\t] + -> channel (HIDDEN)
   ;

fragment DIGIT : [0-9] ;
