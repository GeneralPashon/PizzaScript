package com.pizzascript;

import com.pizzascript.token.Lexer;
import com.pizzascript.token.Token;
import com.pizzascript.parse.Parser;
import com.pizzascript.parse.tree.ListNode;
import com.pizzascript.debug.TreePrinter;
import jpize.files.Resource;

import java.util.List;

public class Main{

    public static void main(String[] args){
        // Compile file name
        final String fileName = "hello_world";

        // Src file
        final Resource resourceFile = new Resource("src/" + fileName + ".ps");
        final String code = resourceFile.getReader().readString();
        // Output file
        final Resource outputFile = new Resource("output/" + fileName + ".asm", true);
        outputFile.create();

        // Lexer
        final Lexer lexer = new Lexer();
        lexer.tokenize(code);
        lexer.print();
        final List<Token> tokens = lexer.getTokens();

        // Parser
        final Parser parser = new Parser(tokens);
        final ListNode programTree = parser.parseRootCode();

        // Compile output nasm file  &  Run
        //NasmUtils.compileAndRun(fileName);

        // Print program tree
        System.out.println();
        TreePrinter.print(programTree);
    }

}
