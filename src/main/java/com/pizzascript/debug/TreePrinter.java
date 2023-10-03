package com.pizzascript.debug;

import com.pizzascript.parse.FuncArg;
import com.pizzascript.parse.tree.INode;
import com.pizzascript.parse.tree.ListNode;
import com.pizzascript.parse.tree.node.FuncNode;
import com.pizzascript.parse.tree.node.VarNode;
import com.pizzascript.parse.tree.node.*;

import java.util.StringJoiner;

public class TreePrinter{

    public static void print(ListNode tree){
        System.out.println("Program Tree:");
        System.out.println("Root:");
        renderChildren(tree, 0);
    }

    private static void renderChildren(INode node, int tabs){
        if(node instanceof ListNode block){
            // Func, Var
            if(block instanceof FuncNode func){ // Func: getIndex(int x, int y, int z) => uint
                System.out.println();
                print(tabs, "Func: " + func.name + "(");

                final StringJoiner joiner = new StringJoiner(", ");
                for(FuncArg arg: func.args)
                    joiner.add(arg.toString());

                System.out.println(joiner + ") => " + func.dataType.name + ":");

            }else if(block instanceof VarNode var){ // Var: (float) 'PI' = 3.141592
                print(tabs, "Var: (" + var.dataType.name + ") " + " ".repeat(Math.max(0, 5 - var.dataType.name.length())) + " '" + var.name + "'");
                if(!var.getNodes().isEmpty())
                    System.out.print(" ".repeat(Math.max(0, 7 - var.name.length())) + " = ");
                else
                    System.out.println();

            }else if(block instanceof FuncCallNode call){ // Call: println( literal(Hello, World!) )
                print(tabs, "Call: " + call.callFuncName + "( ");

            }else if(block instanceof ReturnNode returnNode){ // Return: variable(y)
                print(tabs, "Return: ");
                if(!returnNode.getNodes().isEmpty())
                    System.out.println(returnNode.getNodes().get(0));
                else
                    System.out.println();
            }

            // Children
            for(INode child: block.getNodes())
                renderChildren(child, tabs + 1);

            // sout
            if(block instanceof FuncCallNode)
                System.out.println(" )");
            else if(block instanceof VarNode var && !var.getNodes().isEmpty())
                System.out.println();

        // Call
        }else if(node instanceof SingleValueNode singleValue){
            System.out.print(singleValue.type.name + "(" + singleValue.string + ")");
        }
    }


    private static void print(int tabs, String text){
        System.out.print("    ".repeat(tabs) + text);
    }

    private static void println(int tabs, String text){
        System.out.println("    ".repeat(tabs) + text);
    }

}
