package com.pizzascript.parse.tree.node;

import com.pizzascript.parse.ValueType;
import com.pizzascript.parse.tree.INode;
import com.pizzascript.parse.tree.ListNode;

public class SingleValueNode extends INode{

    public final ValueType type;
    public final String string;

    public SingleValueNode(ListNode parent, ValueType type, String string){
        super(parent);
        this.type = type;
        this.string = string;
    }

    @Override
    public String toString(){
        return type.name + "(" + string + ")";
    }

}
