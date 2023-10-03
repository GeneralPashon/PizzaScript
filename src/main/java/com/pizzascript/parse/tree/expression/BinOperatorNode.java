package com.pizzascript.parse.tree.expression;

import com.pizzascript.parse.BinOperator;
import com.pizzascript.parse.tree.INode;
import com.pizzascript.parse.tree.ListNode;

public class BinOperatorNode extends INode{

    public final BinOperator operator;
    public final INode a, b;

    public BinOperatorNode(ListNode parent, BinOperator operator, INode a, INode b){
        super(parent);
        this.operator = operator;
        this.a = a;
        this.b = b;
    }

}
