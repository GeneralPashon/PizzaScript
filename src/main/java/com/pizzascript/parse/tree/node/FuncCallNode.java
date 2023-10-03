package com.pizzascript.parse.tree.node;

import com.pizzascript.parse.tree.ListNode;

public class FuncCallNode extends ListNode{

    public final String callFuncName;

    public FuncCallNode(ListNode parent, String callFuncName){
        super(parent);
        this.callFuncName = callFuncName;
    }

}
