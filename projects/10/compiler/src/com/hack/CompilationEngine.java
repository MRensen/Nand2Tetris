package com.hack;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CompilationEngine {
    private FileWriter fw;
    private List<Map.Entry<String, TokenType>> tokenList;
    private TokenType tokenType;
    private String token;
    private int tokenListCounter;

    public CompilationEngine(List<Map.Entry<String,TokenType>> tokenlist){
        this.tokenList = tokenlist;
        tokenListCounter = 0;

        try {
            fw = new FileWriter(Main.outputAddress);
            next();
            CompileClass();
        }catch(IOException e){e.printStackTrace();}

    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private void next(){
        //TODO make sure this sets "token" and "tokentype" to their next values
        Map.Entry entry = tokenList.get(tokenListCounter);
        token = (String)entry.getKey();
        tokenType = (TokenType) entry.getValue();
        tokenListCounter++;
    }

    private void unNext(){
        tokenListCounter--;
        Map.Entry entry = tokenList.get(tokenListCounter-1);
        token = (String)entry.getKey();
        tokenType = (TokenType) entry.getValue();
    }

    private void writeTypeToken() throws IOException{
        if(token.equals("length")){
            int g=0;
        }
        String tt = tokenType.toString();
        fw.write("<"+tt+">"+token+"</"+tt+">\n");
        fw.flush();
        if(token.equals("deAlloc") && Main.name.equals("Square.jack")){
            System.out.println();
        }
    }

    public void CompileClass() throws IOException{
        //Compiles a complete class
        fw.write("<class>\n");
        writeTypeToken(); //"class"
        next();
        writeTypeToken(); //className
        next();
        writeTypeToken(); // "{"
        next();
        CompileClassVarDec();
        CompileSubroutineDec();
        writeTypeToken(); //"}"
        fw.write("</class>\n");
    }
    public void CompileClassVarDec()throws IOException{
        //Compiles a static variable declaration, or a field declaration
        while(token.equals("static") || token.equals("field")){
            fw.write("<classVarDec>\n");
            writeTypeToken(); // "static|field"
            next();
            writeTypeToken(); // "int|char|boolean|className"
            next();
            writeTypeToken(); //varName(identifier)
            next();
            if(token.equals(",")) {
                while (token.equals(",")){
                    writeTypeToken();//","
                    next();
                    writeTypeToken(); //zero varname if while loop doesn't initiate, one or more varName if while loop does initiate
                    next();
                }
            }
            writeTypeToken();//";"
            next();
            fw.write("</classVarDec>\n");

        }

    }
    public void CompileSubroutineDec()throws IOException{
        //Compiles a complete method, function or constructor.
        while(token.equals("constructor")||token.equals("function")||token.equals("method")){
            fw.write("<subroutineDec>\n");
            writeTypeToken();//"constructor|function|method"
            next();
            writeTypeToken();//"void|type"
            next();
            writeTypeToken();//subroutineName(identifier)
            next();
            writeTypeToken();//"("
            next();
            compileParameterList();
            writeTypeToken();//")"
            next();
            compileSubroutineBody();
            fw.write("</subroutineDec>\n");
        }
    }
    public void compileParameterList()throws IOException{
        //Compiles a (possibly empty) parameter list. Does not handle the enclosing "()"
        fw.write("<parameterList>\n");
        if(tokenType.equals(TokenType.identifier)||tokenType.equals(TokenType.keyword)) {
            writeTypeToken(); //type
            next();
            writeTypeToken(); //varName(identifier)
            next();
            if (token.equals(",")) {
                while (token.equals(",")) {
                    writeTypeToken();//","
                    next();
                    writeTypeToken(); //type
                    next();
                    writeTypeToken(); //varName
                    next();
                }
            }
        }
        fw.write("</parameterList>\n");
    }
    public void compileSubroutineBody()throws IOException{
        //Compiles a subroutine's body
        fw.write("<subroutineBody>\n");
        writeTypeToken(); //"{"
        next();
        while (token.equals("var")) {
            compileVarDec();
        }
        compileStatements();
        next();
        writeTypeToken(); //"}"
        next();
        fw.write("</subroutineBody>\n");

    }
    public void compileVarDec()throws IOException{
        //Compiles a var declaration
        fw.write("<varDec>\n");
        if(token.equals("var")){
            writeTypeToken(); //"var"
            next();
            writeTypeToken(); //type
            next();
            writeTypeToken(); //varName(identifier)
            next();
            if (token.equals(",")) {
                while (token.equals(",")) {
                    writeTypeToken();//","
                    next();
                    writeTypeToken(); //zero varname if while loop doesn't initiate, one or more varName if while loop does initiate
                    next();
                }
            }
            writeTypeToken(); //";"
            next();
        }
        fw.write("</varDec>\n");
    }
    public void compileStatements()throws IOException{
        //Compiles a sequence of statements. Does not handle the enclosing "{}"
        fw.write("<statements>\n");
        boolean check = true;
        while (check) {
            switch (token) {
                case "let":
                    compileLet();
                    break;
                case "if":
                    compileIf();
                    break;
                case "while":
                    compileWhile();
                    break;
                case "do":
                    compileDo();
                    break;
                case "return":
                    compileReturn();
                    break;
            }

            next();

            if (token.equals("let") || token.equals("if") || token.equals("while") || token.equals("do") || token.equals("return")) {
                check = true;
            } else {
                check = false;
                unNext();//undo the previous next
            }
        }
        fw.write("</statements>\n");
    }
    public void compileLet()throws IOException{
        //Compiles let statements
        fw.write("<letStatement>\n");
        writeTypeToken();//"let"
        next();
        writeTypeToken();//varName(identifier)
        next();
        if(token.equals("[")){
            writeTypeToken();//"["
            next();
            CompileExpression();
            writeTypeToken(); //"]"
            next();
        }
        writeTypeToken();//"="
        next();
        CompileExpression();
        writeTypeToken(); //";"
        fw.write("</letStatement>\n");
    }
    public void compileIf()throws IOException{
        //Compiles an if statement, possibly with a trailing else clause
        fw.write("<ifStatement>\n");
        writeTypeToken(); //"if"
        next();
        writeTypeToken(); //"("
        next();
        CompileExpression();
        writeTypeToken(); //")"
        next();
        writeTypeToken(); //"{"
        next();
        compileStatements();
        next();
        writeTypeToken(); //"}"
        next();
        if (token.equals("else")){
            writeTypeToken(); //"else"
            next();
            writeTypeToken(); //"{"
            next();
            compileStatements();
            next();
            writeTypeToken();//"}"
        } else {
            unNext();
        }
        fw.write("</ifStatement>\n");

    }
    public void compileWhile()throws IOException{
        //Compiles a while statement
        fw.write("<whileStatement>\n");
        writeTypeToken(); //"while"
        next();
        writeTypeToken(); //"("
        next();
        CompileExpression();
//        next();
        writeTypeToken(); //")"
        next();
        writeTypeToken(); //"{"
        next();
        compileStatements();
        next();
        writeTypeToken();//"}"
        fw.write("</whileStatement>\n");
    }
    public void compileDo()throws IOException{
        //Compiles a do statement
        fw.write("<doStatement>\n");
        writeTypeToken();//"do"
        next();
        compileSubroutineCall();
        next();
        writeTypeToken(); //";"
        fw.write("</doStatement>\n");
    }
    public void compileReturn()throws IOException{
        //Compiles a returns statement
        fw.write("<returnStatement>\n");
        writeTypeToken(); //"return"
        next();
        if(!token.equals(";")){
            CompileExpression();
            writeTypeToken(); //";"
        } else {
            writeTypeToken(); //";"
        }

        fw.write("</returnStatement>\n");
    }
    public void CompileExpression()throws IOException{
        //Compiles an expression
        fw.write("<expression>\n");
        CompileTerm();

        if(token.equals("+")||token.equals("-")||token.equals("*")||token.equals("/")||token.equals("&amp;")||token.equals("|")||token.equals("&lt;")||token.equals("&gt;")||token.equals("=")){
            writeTypeToken(); // op (+,-,",etc)
            next();
            CompileTerm();
        } else {
//            unNext();//undo the previous next
        }
        fw.write("</expression>\n");
    }
    public void CompileTerm()throws IOException{
        //Compiles a rem. If the current token is an identifier, the routine must distinguish between a variablem a array entry, or a subroutine call.
        // A single look-ahead token, which may be one of "[", "(", or ".", suffices to distinguish between the possibilities. Anu other token is not part of this term and should not be advanced over.
        fw.write("<term>\n");
        if(tokenType == TokenType.identifier || tokenType == TokenType.keyword){
            next();
            if(token.equals("[")){
                unNext();
                writeTypeToken(); //varName
                next();
                writeTypeToken(); //"["
                next();
                CompileExpression();
                writeTypeToken();//"]"
            } else if(token.equals("(")||token.equals(".")){
                unNext();
                compileSubroutineCall();
            } else {
                unNext();
                writeTypeToken();//varName
            }
            next();
            fw.write("</term>\n");
            return;
        }

        if(token.equals("(")){
            writeTypeToken(); //"("
            next();
            CompileExpression();
            writeTypeToken(); //")"
            next();
            fw.write("</term>\n");
            return;
        }
        if (token.equals("-")||token.equals("~")){
            writeTypeToken(); //unaryOp
            next();
            CompileTerm();
            fw.write("</term>\n");
            return;
        }
        if(tokenType == TokenType.integerConstant || tokenType == TokenType.stringConstant || tokenType == TokenType.symbol){
            writeTypeToken();
            next();
            fw.write("</term>\n");
            return;
        }
//        next();
        fw.write("</term>\n");
    }
    public void CompileExpressionList()throws IOException{
        //Compiles a (possibly empty) comma-separated list of expressions
        fw.write("<expressionList>\n");
        if(token.equals(")")){
            fw.write("</expressionList>\n");
            return;
        } else {
            CompileExpression();

            if (token.equals(",")) {
                while (token.equals(",")) {
                    writeTypeToken();//","
                    next();
                    CompileExpression();
                }
            } else {
//            unNext();
            }
            fw.write("</expressionList>\n");
        }
    }
    public void compileSubroutineCall()throws IOException{
        // Compiles subroutine call
//        fw.write("<subroutineCall>\n");
        next();
        if(token.equals("(")) {
            unNext();
            writeTypeToken(); //subroutineName
            next();
            writeTypeToken(); //"("
            next();
            CompileExpressionList();
            writeTypeToken(); //")"
        } else if(token.equals(".")) {
            unNext();
            writeTypeToken();//className|varName
            next();
            writeTypeToken(); //"."
            next();
            writeTypeToken();//subroutineName
            next();
            writeTypeToken();//"("
            next();
            CompileExpressionList();
            writeTypeToken();//")"
        } else {
            unNext();
        }
//        fw.write("</subroutineCall>\n");
    }
    public void close()throws IOException{
        fw.flush();
        fw.close();
    }

    
}
