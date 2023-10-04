package com.pizzascript.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class Lexer{

    private static final TokenType[] BLACK_LIST = new TokenType[]{ TokenType.SPACE, TokenType.NEXT_LINE };

    private final List<Token> tokens;
    private int position;

    public Lexer(){
        this.tokens = new ArrayList<>();
    }

    public void tokenize(String code){
        while(position < code.length()){
            final String codePart = code.substring(position);
            nextToken(codePart);
        }
    }

    private void nextToken(String codePart){
        for(TokenType type: TokenType.values()){

            final Matcher matcher = type.matches(codePart);
            if(matcher.find()){
                if(isNotBlacklisted(type)){
                    final String result = type.result(matcher.group());
                    tokens.add(new Token(type, position, result));
                }

                position += Math.max(1, matcher.end());
                return;
            }
        }

        position++;
    }

    private boolean isNotBlacklisted(TokenType type){
        return Arrays.stream(BLACK_LIST).noneMatch(tokenType -> tokenType == type);
    }


    public List<Token> getTokens(){
        return tokens;
    }

    public void print(){
        System.out.println("Token List:");

        for(Token token: tokens){
            System.out.print("  ");
            final String position = String.valueOf(token.position);
            System.out.print(position);
            System.out.print(" ".repeat(4 - position.length()));
            final String type = token.type.name().toLowerCase();
            System.out.print(type);
            System.out.print(" ".repeat(15 - type.length()));
            System.out.println(token.string);
        }

        System.out.println();
    }

}
