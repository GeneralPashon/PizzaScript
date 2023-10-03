package com.pizzascript.parse;

public class FuncArg{

    public final DataType type;
    public final String name;

    public FuncArg(DataType type, String name){
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString(){
        return type.name + " " + name;
    }

}
