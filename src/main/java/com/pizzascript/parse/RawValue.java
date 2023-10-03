package com.pizzascript.parse;

import com.pizzascript.token.Token;

public class RawValue{

    public final Token[] tokens;

    public RawValue(Token... tokens){
        this.tokens = tokens;
    }

}
