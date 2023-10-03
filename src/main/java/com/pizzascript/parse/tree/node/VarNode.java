package com.pizzascript.parse.tree.node;

import com.pizzascript.parse.DataType;
import com.pizzascript.parse.tree.ListNode;

public class VarNode extends ListNode{

    public final DataType dataType;
    public final String name;

    public VarNode(ListNode parent, DataType dataType, String name){
        super(parent);
        this.dataType = dataType;
        this.name = name;
    }

}
