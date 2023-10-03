package com.pizzascript.token;

import java.util.function.Function;

public class TokenTypeFunc{

    public final static Function<String, String> NONE = string -> string;
    public final static Function<String, String> LITERAL = string -> {
        final String trim = string.substring(1, string.length() - 1);

        final StringBuilder builder = new StringBuilder();

        boolean escaped = false;
        for(char c: trim.toCharArray()){
            if(escaped){
                builder.append(c);
                escaped = false;
            }else if(c == '\\')
                escaped = true;
            else
                builder.append(c);
        }

        return builder.toString();
    };

}
