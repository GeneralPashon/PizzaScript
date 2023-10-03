package com.pizzascript.parse.tree.node;

import com.pizzascript.parse.DataType;
import com.pizzascript.parse.FuncArg;
import com.pizzascript.parse.tree.FieldNode;
import com.pizzascript.parse.tree.ListNode;

public class FuncNode extends FieldNode{

    public final DataType dataType;
    public final String name;
    public final FuncArg[] args;

    public FuncNode(ListNode parent, DataType dataType, String name, FuncArg[] args){
        super(parent);
        this.dataType = dataType;
        this.name = name;
        this.args = args;
    }

    public boolean hasArg(DataType dataType, String name){
        for(FuncArg arg: args)
            if(arg.type == dataType && arg.name.equals(name))
                return true;

        return false;
    }

}
