package com.pizzascript.parse;

import com.pizzascript.token.Token;
import com.pizzascript.token.TokenType;

public enum Operator{

    ADD (1, TokenType.PLUS    , Double::sum),
    SUB (1, TokenType.MINUS   , (a, b) -> (a - b)),
    MUL (2, TokenType.MULTIPLY, (a, b) -> (a * b)),
    DIV (2, TokenType.DIVIDE  , (a, b) -> (a / b)),
    POW (3, TokenType.POW     , Math::pow);

    public final TokenType tokenType;
    public final int priority;
    private final OperatorFunc func;

    Operator(int priority, TokenType tokenType, OperatorFunc func){
        this.priority = priority;
        this.tokenType = tokenType;
        this.func = func;
    }

    public double calculate(double a, double b){
        return func.apply(a, b);
    }


    public static boolean isOperator(Token token){
        for(Operator operator: values())
            if(token.match(operator.tokenType))
                return true;

        return false;
    }

    public static Operator getOperator(Token token){
        for(Operator operator: values())
            if(token.match(operator.tokenType))
                return operator;

        return null;
    }

    public static int getPriority(Token token){
        for(Operator operator: values())
            if(token.match(operator.tokenType))
                return operator.priority;

        return -1;
    }

}
