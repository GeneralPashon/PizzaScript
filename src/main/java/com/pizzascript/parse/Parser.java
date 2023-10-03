package com.pizzascript.parse;

import com.pizzascript.parse.tree.ListNode;
import com.pizzascript.parse.tree.node.FuncNode;
import com.pizzascript.parse.tree.node.RootNode;
import com.pizzascript.parse.tree.node.VarNode;
import com.pizzascript.parse.tree.node.*;
import com.pizzascript.token.Token;
import com.pizzascript.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser{

    private final TokenIterator tokens;

    public Parser(List<Token> tokens){
        this.tokens = new TokenIterator(tokens);
    }


    public ListNode parseRootCode(){
        final RootNode root = new RootNode();

        while(tokens.hasNext()){
            // start parse word - var or func type
            if(tokens.current.match(TokenType.WORD))
                parseRootCodePart(root);

            tokens.next();
        }

        return root;
    }

    private void parseRootCodePart(ListNode root){
        // data type
        final DataType dataType = DataType.parseType(tokens.current);

        // name
        tokens.requireNextToken(TokenType.WORD);
        final String name = tokens.current.string;

        // function or variable
        if(tokens.matchNextToken(TokenType.LEFT_BRACKET)){
            // function
            final FuncArg[] args = parseFuncArguments();
            parseFunc(root, dataType, name, args);
        }else{
            // variable
            tokens.prev(); // now at var name token (not parsed)
            parseVariable(root, dataType);
        }
    }

    private FuncArg[] parseFuncArguments(){
        final List<FuncArg> args = new ArrayList<>();

        while(!tokens.matchNextToken(TokenType.RIGHT_BRACKET)){
            if(!args.isEmpty()){
                tokens.require(TokenType.COMMA);
                tokens.next();
            }

            if(!tokens.current.match(TokenType.WORD))
                throw new Error("Unexpected token '" + tokens.current.string + "' at position " + tokens.position);

            final Token dataTypeToken = tokens.current;

            if(!tokens.matchNextToken(TokenType.WORD))
                throw new Error("Unexpected token '" + tokens.current.string + "' at position " + tokens.position);

            final String name = tokens.current.string;
            final DataType dataType = DataType.parseType(dataTypeToken);
            if(dataType == null)
                throw new Error("Unexpected datatype at position " + tokens.position);

            final FuncArg argument = new FuncArg(dataType, name);
            args.add(argument);
        }

        return args.toArray(new FuncArg[0]);
    }

    private void parseFunc(ListNode parent, DataType dataType, String name, FuncArg[] args){
        final FuncNode funcNode = new FuncNode(parent, dataType, name, args);

        tokens.requireNextToken(TokenType.LEFT_BRACE);
        int leftBraces = 1;
        while(leftBraces > 0){

            tokens.next();
            switch(tokens.current.type){
                case LEFT_BRACE -> leftBraces++;
                case RIGHT_BRACE -> {
                    leftBraces--;
                    if(dataType != DataType.VOID && leftBraces == 0)
                        throw new Error("Expect that func " + name + " return " + dataType.name);
                }
                case WORD -> {
                    // return
                    if(leftBraces == 1 && parseReturn(funcNode)){
                        leftBraces--;
                        break;
                    }

                    // func calls, variables
                    parseFuncCodePart(funcNode);
                }
            }
        }

        parent.add(funcNode);
    }

    private boolean parseReturn(FuncNode func){
        if(tokens.current.string.equals("return")){
            final ReturnNode returnNode = new ReturnNode(func);
            tokens.next();

            // return [value];
            if(!tokens.current.match(TokenType.SEMICOLON)){
                // check return value
                if(!func.dataType.checkValueToken(tokens.current))
                    throw new Error("Unexpected func '" + func.name + "' return value: '" + tokens.current.string + "' at position " + tokens.position);

                // list value tokens
                final List<Token> valueTokens = new ArrayList<>();
                while(!tokens.matchNextToken(TokenType.SEMICOLON))
                    valueTokens.add(tokens.current);

                // parse value
                final RawValue value = new RawValue(valueTokens.toArray(new Token[0]));
                parseValue(returnNode, func.dataType, value);

                // check if void
                if(func.dataType == DataType.VOID)
                    throw new Error("Void cannot return a value");

            // return;
            }else if(func.dataType != DataType.VOID)
                throw new Error("Expect that func " + func.name + " return " + func.dataType.name);

            tokens.requireNextToken(TokenType.RIGHT_BRACE); // }

            func.add(returnNode);
            return true;
        }

        return false;
    }

    private void parseFuncCodePart(FuncNode funcNode){
        // call func name || var data type
        final Token token1 = tokens.current;

        // func call
        if(tokens.matchNextToken(TokenType.LEFT_BRACKET)){
            funcNode.add(parseFuncCall(funcNode, token1.string));

        // var parse
        }else{
            // now at var name token
            tokens.require(TokenType.WORD);

            final DataType dataType = DataType.parseType(token1);
            if(dataType == null)
                throw new Error("Unexpected datatype at position " + tokens.position);

            parseVariable(funcNode, dataType);
        }
    }

    private FuncCallNode parseFuncCall(ListNode parent, String funcName){
        final FuncCallNode funcCall = new FuncCallNode(parent, funcName);

        // args:
        final List<RawValue> argValues = parseCallFuncArgsValues();

        final ListNode root = parent.getFirstMatchParent(RootNode.class);
        if(root == null)
            throw new Error("THEORETICALLY IMPOSSIBLE ERROR #3 (root = null)");

        final FuncNode func = ((RootNode) root).findFunc(funcName, argValues);
        if(func == null)
            throw new Error("Func '" + funcName + "' is undefined");

        for(int i = 0; i < argValues.size(); i++){
            final DataType type = func.args[i].type;
            final RawValue value = argValues.get(i);

            parseValue(funcCall, type, value);
        }

        // ;
        tokens.requireNextToken(TokenType.SEMICOLON);

        return funcCall;
    }

    private List<RawValue> parseCallFuncArgsValues(){
        final List<RawValue> argValues = new ArrayList<>();

        tokens.next();
        while(!tokens.current.match(TokenType.RIGHT_BRACKET)){
            final List<Token> valueTokens = new ArrayList<>();

            while(!tokens.current.match(TokenType.COMMA, TokenType.RIGHT_BRACKET)){
                valueTokens.add(tokens.current);
                tokens.next();
            }

            if(valueTokens.isEmpty())
                throw new Error("THEORETICALLY IMPOSSIBLE ERROR #1 (call func arg = null)");

            final Token[] tokensArray = valueTokens.toArray(new Token[0]);
            argValues.add(new RawValue(tokensArray));
        }

        return argValues;
    }

    private void parseVariable(ListNode parent, DataType dataType){
        while(!tokens.current.match(TokenType.SEMICOLON)){

            final String name = tokens.current.string;
            final VarNode var = new VarNode(parent, dataType, name);

            // check if var has value '= ...'
            if(!tokens.matchNextToken(TokenType.COMMA, TokenType.SEMICOLON)){

                tokens.require(TokenType.ASSIGN);
                tokens.next();

                // list value tokens
                final List<Token> valueTokens = new ArrayList<>();
                while(!tokens.current.match(TokenType.COMMA, TokenType.SEMICOLON)){
                    valueTokens.add(tokens.current);
                    tokens.next();
                }

                final RawValue value = new RawValue(valueTokens.toArray(new Token[0]));

                if(value.tokens.length == 1 && !dataType.checkValueToken(value.tokens[0]))
                     throw new Error("Unexpected '" + name + "' variable value: '" + value.tokens[0].string + "' at position " + tokens.position);

                parseValue(var, dataType, value);
            }

            // add node
            parent.add(var);
        }
    }

    private void parseValue(ListNode parent, DataType valueDataType, RawValue value){
        switch(value.tokens.length){
            case 0 -> { }
            case 1 -> parseSingleTokenValue(parent, valueDataType, value.tokens[0]);
            default -> parseMultipleTokenValue(parent, valueDataType, value.tokens);
        }
    }

    private void parseMultipleTokenValue(ListNode parent, DataType valueDataType, Token[] valueTokens){
        //    1 * 3  + 3  -  4 * 2
        //   (1 * 3) + 3  -  4 * 2
        //   (1 * 3) + 3  - (4 * 2)
        //  ((1 * 3) + 3) - (4 * 2)
        // (((1 * 3) + 3) - (4 * 2))


    }

    private void parseSingleTokenValue(ListNode parent, DataType valueDataType, Token token){
        final String valueString = token.string;

        // literal
        if(token.match(TokenType.LITERAL_1, TokenType.LITERAL_2))
            parent.add(new SingleValueNode(parent, ValueType.LITERAL, valueString));

        // number
        else if(token.match(TokenType.NUMBER_INTEGER, TokenType.NUMBER_FLOAT))
            parent.add(new SingleValueNode(parent, ValueType.NUMBER, valueString));

        // variable
        else if(token.match(TokenType.WORD)){
            parseVariableValue(parent, valueDataType, token);

        // error
        }else
            throw new Error("Unexpected " + valueDataType.name + " value: '" + valueString + "' at position " + tokens.position); // position may be incorrect
    }

    private void parseVariableValue(ListNode parent, DataType type, Token token){
        final String name = token.string;
        final ListNode fieldNode = parent.getFirstMatchParent(RootNode.class, FuncNode.class);
        if(fieldNode == null)
            throw new Error("THEORETICALLY IMPOSSIBLE ERROR #2 (root | func = null)");

        if(!(fieldNode instanceof FuncNode func) || (!func.hasArg(type, name) && func.findVar(name) == null))
            if(fieldNode.getParent() == null || !(fieldNode.getParent() instanceof RootNode root) || root.findVar(name) == null)
                throw new Error("Variable: '" + type.name + " " + name + "' is undefined");

        parent.add(new SingleValueNode(parent, ValueType.VARIABLE, name));
    }

}
