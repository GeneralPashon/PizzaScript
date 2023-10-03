package com.pizzascript.parse;

import com.pizzascript.token.Token;
import com.pizzascript.token.TokenType;

import java.util.List;

public class TokenIterator{

    private final List<Token> tokens;
    public Token current;
    public int position;

    public TokenIterator(List<Token> tokens){
        this.tokens = tokens;
        this.current = tokens.get(0);
    }


    public boolean next(){
        if(position + 1 == tokens.size())
            return false;

        position++;
        current = tokens.get(position);

        return true;
    }

    public void prev(){
        position--;
        current = tokens.get(position);
    }

    public boolean hasNext(){
        return position + 1 < tokens.size();
    }


    public boolean matchNextToken(TokenType... expectations){
        if(next())
            return current.match(expectations);

        return false;
    }

    public void requireNextToken(TokenType... expectations){
        if(next())
            require(expectations);
    }

    public void require(TokenType... expectations){
        if(!current.match(expectations))
            throw new Error("Expect " + expectations[0].name + " at position " + position + " '" + current.string + "'");
    }

}
