package com.pizzascript.parse;

import com.pizzascript.token.Token;
import com.pizzascript.token.TokenType;
import jpize.math.Mathc;

import java.util.List;
import java.util.Stack;

public class NumberExpressionParser{

    private final Stack<Token> operators = new Stack<>();
    private final Stack<Token> operands = new Stack<>();

    public void parseExpression(List<Token> inputTokens){
        for(Token token: inputTokens){

            final boolean tokenIsOperator = Operator.isOperator(token);
            final boolean tokenIsBracket = token.match(TokenType.LEFT_BRACKET);
            final boolean tokenIsEndBracket = token.match(TokenType.RIGHT_BRACKET);

            if(tokenIsBracket){
                operators.push(token);
            }else if(tokenIsOperator){

                final boolean stackIsEmpty = operators.empty();

                if(stackIsEmpty || operators.peek().type == TokenType.LEFT_BRACKET){
                    operators.push(token);

                }else{

                    final Token stackPeekOperator = operators.peek();
                    final int stackPeekOperatorPriority = Operator.getPriority(stackPeekOperator);
                    final int tokenOperatorPriority = Operator.getPriority(token);

                    if(tokenOperatorPriority < stackPeekOperatorPriority){
                        calculateOrPush();
                    }

                    operators.push(token);
                }

            }else if(tokenIsEndBracket){

                // calculate to '('
                while(!operators.empty() && !operators.peek().match(TokenType.LEFT_BRACKET))
                    calculateOrPush();

                // pop the '('
                if(!operators.empty())
                    operators.pop();

            }else{ // token is number

                operands.push(token);
            }
        }

        if(!operators.empty()){
            // calculate all

            while(!operators.empty())
                calculateOrPush();
        }

        // done
        printStep();
    }

    private int step = 1;

    private void printStep(){
        System.out.println("Step " + step + ":");
        step++;

        if(!operators.empty()){
            System.out.print("    ");
            for(Token token: operators)
                System.out.print(token.string + " ");
            System.out.println();
        }

        System.out.print("    ");
        for(Token token: operands)
            System.out.print(token.string + " ");
        System.out.println();
    }

    private void calculateOrPush(){
        printStep();

        final Token operatorToken = operators.pop();

        final Operator operator = Operator.getOperator(operatorToken);
        if(operator == null)
            throw new Error("THEORETICALLY IMPOSSIBLE ERROR #4 (operator = null)");

        final Token bOperand = operands.pop();
        final Token aOperand = operands.pop();

        final boolean aIsNumber = aOperand.match(TokenType.NUMBER_FLOAT, TokenType.NUMBER_INTEGER);
        final boolean bIsNumber = bOperand.match(TokenType.NUMBER_FLOAT, TokenType.NUMBER_INTEGER);

        if(aIsNumber && bIsNumber){
            // calc

            final double aFloat = Double.parseDouble(aOperand.string);
            final double bFloat = Double.parseDouble(bOperand.string);

            final double resultFloat = operator.calculate(aFloat, bFloat);

            final boolean aIsInteger = aOperand.match(TokenType.NUMBER_INTEGER);
            final boolean bIsInteger = bOperand.match(TokenType.NUMBER_INTEGER);

            final Token resultToken;

            if(aIsInteger && bIsInteger){
                final int resultInt = (int) resultFloat;

                if(resultInt != Mathc.round(resultFloat))
                    throw new Error("THEORETICALLY possible ERROR #1 (result int cast)");

                resultToken = new Token(TokenType.NUMBER_INTEGER, aOperand.position, String.valueOf(resultInt));
            }else{
                resultToken = new Token(TokenType.NUMBER_FLOAT, aOperand.position, String.valueOf(resultFloat));
            }

            operands.push(resultToken);

        }else{
            // put it in tree

            System.out.println("Push to tree: " + aOperand.string + " " + operator.tokenType.name + " " + bOperand.string);
        }
    }

}
