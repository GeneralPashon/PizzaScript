package com.pizzascript.parse.tree;

import com.pizzascript.parse.DataType;
import com.pizzascript.parse.FuncArg;
import com.pizzascript.parse.RawValue;
import com.pizzascript.parse.tree.node.FuncNode;
import com.pizzascript.parse.tree.node.VarNode;

import java.util.List;

public abstract class FieldNode extends ListNode{

    public FieldNode(ListNode parent){
        super(parent);
    }


    @Override
    public void add(INode node){
        if(node instanceof VarNode var)
            if(findVar(var.name) != null)
                throw new Error("Var '" + var.name + "' name is busy");

        if(node instanceof FuncNode func)
            if(findFunc(func.name, func.args) != null)
                throw new Error("Func '" + func.name + "' is exists");

        super.add(node);
    }

    public VarNode findVar(String name){
        for(INode node: super.getNodes()){

            if(node instanceof VarNode var && var.name.equals(name))
                return var;
        }

        return null;
    }

    public FuncNode findFunc(String name, FuncArg... args){
        for(INode node: super.getNodes()){

            if(node instanceof FuncNode func && func.name.equals(name) && func.args.length == args.length){
                for(int i = 0; i < args.length; i++)
                    if(func.args[i].type != args[i].type)
                        break;

                return func;
            }
        }

        return null;
    }

    public FuncNode findFunc(String name, List<RawValue> args){
        for(INode node: super.getNodes())
            if(node instanceof FuncNode func && func.name.equals(name) && func.args.length == args.size())
                return func;

        return null;
    }

}
