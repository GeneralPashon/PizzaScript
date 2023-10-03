package com.pizzascript.token;

public class Token{

    public final TokenType type;
    public final int position;
    public final String string;

    public Token(TokenType type, int position, String string){
        this.type = type;
        this.position = position;
        this.string = string;
    }

    public boolean match(TokenType... expectations){
        for(TokenType expect: expectations)
            if(expect == type)
                return true;

        return false;
    }

}
