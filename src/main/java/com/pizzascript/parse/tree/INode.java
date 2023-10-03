package com.pizzascript.parse.tree;

public abstract class INode{

    private final ListNode parent;

    public INode(ListNode parent){
        this.parent = parent;
    }

    public ListNode getParent(){
        return parent;
    }


    public final ListNode getFirstMatchParent(Class<?>... parentClasses){
        if(parent == null)
            return null;

        INode node = this;
        while(node != null){

            for(Class<?> parentClass: parentClasses)
                if(node.getClass() == parentClass)
                    return (ListNode) node;

            // next parent
            node = node.getParent();
        }

        return null;
    }

}