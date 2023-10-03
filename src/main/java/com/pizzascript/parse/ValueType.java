package com.pizzascript.parse;

public enum ValueType{

    NUMBER     ("number"       ),
    LITERAL    ("literal"      ),
    VARIABLE   ("variable"     ),
    FUNC_CALL  ("function call"),
    EXPRESSION ("expression"   );

    public final String name;

    ValueType(String name){
        this.name = name;
    }

}
