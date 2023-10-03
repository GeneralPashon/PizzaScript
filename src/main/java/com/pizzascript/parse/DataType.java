package com.pizzascript.parse;

import com.pizzascript.token.Token;
import com.pizzascript.token.TokenType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum DataType{

    BYTE   ("byte"  ,  1,  TokenType.NUMBER_INTEGER),
    SHORT  ("short" ,  2,  TokenType.NUMBER_INTEGER),
    INT    ("int"   ,  4,  TokenType.NUMBER_INTEGER),
    LONG   ("long"  ,  8,  TokenType.NUMBER_INTEGER),

    UBYTE  ("ubyte" ,  1,  TokenType.NUMBER_INTEGER),
    USHORT ("ushort",  2,  TokenType.NUMBER_INTEGER),
    UINT   ("uint"  ,  4,  TokenType.NUMBER_INTEGER),
    ULONG  ("ulong" ,  8,  TokenType.NUMBER_INTEGER),

    FLOAT  ("float" ,  4,  TokenType.NUMBER_FLOAT, TokenType.NUMBER_INTEGER),
    DOUBLE ("double",  8,  TokenType.NUMBER_FLOAT, TokenType.NUMBER_INTEGER),

    CHAR   ("char"  ,  2,  "^\\w{1}"        ,  TokenType.LITERAL_1     ),
    BOOL   ("bool"  ,  1,  "^true|false|1|0",  TokenType.NUMBER_INTEGER),
    STRING ("string", -1,  TokenType.LITERAL_1, TokenType.LITERAL_2    ),

    VOID   ("void"  ,  0),
    OBJECT ("Object", -1);

    public final String name;
    private final Pattern pattern;
    public final int size;
    public final TokenType[] valueTokens;

    DataType(String name, int size, String regex, TokenType... valueTokens){
        this.name = name;
        this.size = size;
        this.pattern = Pattern.compile(regex);
        this.valueTokens = valueTokens;
    }

    DataType(String name, int size, TokenType... valueTokens){
        this.name = name;
        this.size = size;
        this.pattern = null;
        this.valueTokens = valueTokens;
    }

    public boolean checkValueToken(Token token){
        // void => false
        if(this == VOID)
            return false;

        // variable => true
        if(token.type == TokenType.WORD)
            return true;

        // check other => true or pattern match
        for(TokenType type: valueTokens)
            if(token.type == type)
                return pattern == null || pattern.matcher(token.string).find();

        return false;
    }


    public static DataType parseType(Token token){
        for(DataType type: values())
            if(token.string.startsWith(type.name.toLowerCase()))
                return type;

        if(token.type == TokenType.WORD)
            return OBJECT;

        return null;
    }
    
}
