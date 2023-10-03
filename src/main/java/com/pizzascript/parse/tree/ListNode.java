package com.pizzascript.parse.tree;

import java.util.ArrayList;
import java.util.List;

public abstract class ListNode extends INode{

    private final List<INode> nodes;

    public ListNode(ListNode parent){
        super(parent);
        this.nodes = new ArrayList<>();
    }

    public void add(INode node){
        nodes.add(node);
    }

    public List<INode> getNodes(){
        return nodes;
    }

}
