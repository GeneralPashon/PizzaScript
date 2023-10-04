package com.pizzascript;

import com.pizzascript.parse.NumberExpressionParser;
import com.pizzascript.token.Lexer;
import com.pizzascript.token.Token;
import com.pizzascript.parse.Parser;
import com.pizzascript.parse.tree.ListNode;
import com.pizzascript.debug.TreePrinter;
import jpize.files.Resource;

import java.util.List;

public class Main{

    public static void main(String[] args){
        parseMathExpression();
        // compileHelloWorld();
    }

    private static void parseMathExpression(){
        // num test
        final Lexer numLexer = new Lexer();
        numLexer.tokenize("(((((2)))) + 16) ** 2 + (2 + 0 + 3 * 54)");
        numLexer.print();
        final List<Token> numTokens = numLexer.getTokens();

        final NumberExpressionParser numParser = new NumberExpressionParser();
        numParser.parseExpression(numTokens);
    }

    private static void compileHelloWorld(){
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
        TreePrinter.print(programTree);
    }

}
