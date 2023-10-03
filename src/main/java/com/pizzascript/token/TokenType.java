package com.pizzascript.token;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TokenType{

    NEXT_LINE     ("next line",      "^[\\n]+"    ),
    SPACE         ("space",          "^[ \\s\\t]+"),

    NUMBER_FLOAT  ("float number",   "^\\-?[0-9]+\\.[0-9]+" ),
    NUMBER_INTEGER("integer number", "^\\-?[0-9]+"          ),
    WORD          ("word",           "^[a-zA-Z_]+(?:\\w*)"  ),

    ASSIGN        ("'='",            "^[=]{1}"    ),
    PLUS          ("'+'",            "^[+]{1}"    ),
    MINUS         ("'-'",            "^[-]{1}"    ),
    POW           ("'**'",           "^[*]{2}"    ),
    MULTIPLY      ("'*'",            "^[*]{1}"    ),
    DIVIDE        ("'/'",            "^[/]{1}"    ),
    MODULE_DIVIDE ("'%'",            "^[%]{1}"    ),

    DOT           ("'.'",            "^\\."       ),
    COMMA         ("','",            "^,"         ),
    SEMICOLON     ("';'",            "^;+"        ),

    LEFT_BRACKET  ("'('",            "^[\\(]+"    ),
    RIGHT_BRACKET ("')'",            "^[\\)]+"    ),
    LEFT_BRACE    ("'{'",            "^[{]+"      ),
    RIGHT_BRACE   ("'}'",            "^[}]+"      ),

    LITERAL_2     ("\"literal\"", "^\\\"(\\\\.|[^\\\"\\\\])*\\\"" , TokenTypeFunc.LITERAL),
    LITERAL_1     ("'literal'"  , "^'(\\\\.|[^'\\\\])*'"          , TokenTypeFunc.LITERAL);

    public final String name;
    private final Pattern pattern;
    private final Function<String, String> function;

    TokenType(String name, String regex, Function<String, String> function){
        this.name = name;
        this.pattern = Pattern.compile(regex);
        this.function = function;
    }

    TokenType(String name, String regex){
        this(name, regex, TokenTypeFunc.NONE);
    }

    public Matcher matches(String input){
        return pattern.matcher(input);
    }

    public String result(String string){
        return function.apply(string);
    }

}
