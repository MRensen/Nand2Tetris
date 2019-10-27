package com.hack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CompilationEngine {
    private int generalLabelCounter;
//    private FileWriter fw;
    private VMWriter vm;
    private List<Map.Entry<String, TokenType>> tokenList;
    private TokenType tokenType;
    public String token;
    private int tokenListCounter;
    private SymbolTable st;
    private String subroutineType;
    private Boolean expressionArray;
    String className;
    String functionName;

    public CompilationEngine(List<Map.Entry<String,TokenType>> tokenlist){
        vm = new VMWriter(new File(Main.VMAddress));
        this.tokenList = tokenlist;
        tokenListCounter = 0;
        generalLabelCounter = 0;

        try {
            next();
            CompileClass();
        }catch(Exception e){e.printStackTrace();
            System.out.println("token: " +token+"\n" +
                                "subroutine: " + functionName +"\n" +
                                "class: "+ className +"\n");}

    }



    public VMWriter.Segment getSeg(String expr){
        //if expr isFIELD
        if(st.kindOf(expr).equals(Kind.FIELD)){
            return VMWriter.Segment.THIS;
        }
        //if expr isLOCAL
        if(st.kindOf(expr).equals(Kind.LOCAL)){
            return VMWriter.Segment.LOCAL;
        }
        //if expr isArg
        if (st.kindOf(expr).equals(Kind.ARG)){
            return VMWriter.Segment.ARGUMENT;
        }
        //if expr isStatic
        if (st.kindOf(expr).equals(Kind.STATIC)){
            return VMWriter.Segment.STATIC;
        }
        if(st.kindOf(expr).equals(Kind.POINTER)){
            return VMWriter.Segment.POINTER;
        }
        return null;
    }


    private void next(){
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


    public void CompileClass() throws IOException{
        //Compiles a complete class
        st = new SymbolTable();
        vm.writecomment("<class>\n");
         //"class"
        next();
         //className
        className = token;
        st.define("this", className, Kind.POINTER);
        next();
         // "{"
        next();
        CompileClassVarDec();
        CompileSubroutineDec();
         //"}"
        vm.writecomment("</class>\n");
    }
    public void CompileClassVarDec()throws IOException{ ;
        //Compiles a static variable declaration, or a field declaration
        Kind kind;
        String type;
        while(token.equals("static") || token.equals("field")){
            vm.writecomment("<classVarDec>\n");
             // "static|field"
            kind = Kind.valueOf(token.toUpperCase());
            next();
             // "int|char|boolean|className"
            type = token;
            next();
             //varName(identifier)
            st.define(token, type, kind);
            next();
            if(token.equals(",")) {
                while (token.equals(",")){
                    //","
                    next();
                     //zero varname if while loop doesn't initiate, one or more varName if while loop does initiate
                    st.define(token, type, kind);
                    next();
                }
            }
            //";"
            next();
            vm.writecomment("</classVarDec>\n");

        }

    }
    public void CompileSubroutineDec()throws IOException{
        //Compiles a complete method, function or constructor.
        while(token.equals("constructor")||token.equals("function")||token.equals("method")){
            vm.writecomment("<subroutineDec>\n");
            st.startSubroutine();
            //"constructor|function|method"
            subroutineType = token;
            next();
            //"void|type"
            String type = token;
            next();
            //subroutineName(identifier)
            functionName = token;
//            if(sub.equals("method")){
//                st.define("this", className, Kind.ARG);
//            }
            next();
            //"("
            next();
            int numberOfParameters = compileParameterList();
            //")"
            //a jack function|constructor with k args becomes a vm function with k args. A jack method with k args becomes a vm function with k+1 args

            next();
            compileSubroutineBody();
//            if(type.equals("void")){
//                vm.writePop(VMWriter.Segment.TEMP, 0);
//            }
            vm.writecomment("</subroutineDec>\n");
        }
    }
    public int compileParameterList()throws IOException{
        //Compiles a (possibly empty) parameter list. Does not handle the enclosing "()"
        vm.writecomment("<parameterList>\n");
        int parameters = 0;
        if(tokenType.equals(TokenType.identifier)||tokenType.equals(TokenType.keyword)) {
             //type
            String type = token;
            next();
             //varName(identifier)
            parameters ++;
            if(subroutineType.equals("method")){
                st.define("this",className, Kind.ARG);
            }
            st.define(token, type, Kind.ARG);
            next();
            if (token.equals(",")) {
                while (token.equals(",")) {
                    //","
                    next();
                     //type
                    type = token;
                    next();
                     //varName
                    parameters++;
                    st.define(token, type, Kind.ARG);
                    next();
                }
            }
        }
        vm.writecomment("</parameterList>\n");
        return parameters;
    }
    public void compileSubroutineBody()throws IOException{
        //Compiles a subroutine's body
        vm.writecomment("<subroutineBody>\n");
         //"{"
        next();

        while (token.equals("var")) {
            compileVarDec();
        }

        vm.writeFunction(className + "." + functionName, st.varCount(Kind.LOCAL));

        if(subroutineType.equals("constructor")){
            vm.writePush(VMWriter.Segment.CONSTANT, st.varCount(Kind.FIELD)+st.varCount(Kind.STATIC));
            vm.writeCall("Memory.alloc", 1);
            vm.writePop(VMWriter.Segment.POINTER, 0);
        } else if(subroutineType.equals("method")){
            vm.writePush(VMWriter.Segment.ARGUMENT, 0);
            vm.writePop(VMWriter.Segment.POINTER, 0);
        } if(token.equals("}")){
            next();
            return;
        }
        compileStatements();
        next();
         //"}"
        next();
        vm.writecomment("</subroutineBody>\n");

    }
    public void compileVarDec()throws IOException{
        //Compiles a var declaration
        vm.writecomment("<varDec>\n");
        if(token.equals("var")){
             //"var"
            next();
             //type
            String type = token;
            next();
             //varName(identifier)
            st.define(token, type, Kind.LOCAL);
            next();
            if (token.equals(",")) {
                while (token.equals(",")) {
                    //","
                    next();
                     //zero varname if while loop doesn't initiate, one or more varName if while loop does initiate
                    st.define(token, type, Kind.LOCAL);
                    next();
                }
            }
             //";"
            next();
        }
        vm.writecomment("</varDec>\n");
    }
    public void compileStatements()throws IOException{
        //Compiles a sequence of statements. Does not handle the enclosing "{}"
        vm.writecomment("<statements>\n");
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
        vm.writecomment("</statements>\n");
    }
    public void compileLet()throws IOException{
        //Compiles let statements
        vm.writecomment("<letStatement>\n");
        //"let"
        next();
        //varName(identifier)
        String varName = token;
        int index = st.indexOf(varName);
        next();
        if(token.equals("[")){
            //"["
            next();
            CompileExpression();
             //"]"
            vm.writePush(getSeg(varName), st.indexOf(varName));
            vm.writeArithmetic(VMWriter.Command.ADD);
            next();
            //"="
            next();
            CompileExpression();
            vm.writePop(VMWriter.Segment.TEMP, 0);
            vm.writePop(VMWriter.Segment.POINTER, 1);
            vm.writePush(VMWriter.Segment.TEMP, 0);
            vm.writePop(VMWriter.Segment.THAT, 0);
             //";"
        } else {
            //"="
            next();
            if(token.equals("false")){
                System.out.println(token);
            }
            CompileExpression();
            vm.writePop(getSeg(varName), st.indexOf(varName));
             //";"
        }
        vm.writecomment("</letStatement>\n");
    }
    public void compileIf()throws IOException{
        int labelCounter = generalLabelCounter++;
        //Compiles an if statement, possibly with a trailing else clause
        vm.writecomment("<ifStatement>\n");
         //"if"
        next();
         //"("
        next();
        CompileExpression();
        vm.writeArithmetic(VMWriter.Command.NOT);
         //")"
        next();
         //"{"
        next();
        vm.writeIf("IFFALSE" + labelCounter);
        compileStatements();
        next();
         //"}"
        next();
        vm.writeGoto("IFTRUE" + labelCounter);
        if (token.equals("else")){
             //"else"
            next();
             //"{"
            next();
            vm.writeLabel("IFFALSE"+labelCounter);
            compileStatements();
            vm.writeLabel("IFTRUE" + labelCounter);
            next();
            //"}"
        } else {
            vm.writeLabel("IFFALSE"+labelCounter);
            vm.writeLabel("IFTRUE" + labelCounter);
            unNext();
        }
        generalLabelCounter++;
        vm.writecomment("</ifStatement>\n");

    }
    public void compileWhile()throws IOException{
        int labelCounter = generalLabelCounter++;
        //Compiles a while statement
        vm.writecomment("<whileStatement>\n");
         //"while"
        next();
         //"("
        next();
        vm.writeLabel("WHILETRUE" + labelCounter);
        CompileExpression();
        vm.writeArithmetic(VMWriter.Command.NOT);
        vm.writeIf("WHILEFALSE"+labelCounter);
//        next();
         //")"
        next();
         //"{"
        next();
        if(token.equals("}")){
            vm.writeGoto("WHILETRUE" + labelCounter);
            vm.writeLabel("WHILEFALSE" + labelCounter);
            return;
        } else {
            compileStatements();
        }
        vm.writeGoto("WHILETRUE" + labelCounter);
        vm.writeLabel("WHILEFALSE" + labelCounter);
        next();
        //"}"
        generalLabelCounter++;
        vm.writecomment("</whileStatement>\n");
    }
    public void compileDo()throws IOException{
        //Compiles a do statement
        vm.writecomment("<doStatement>\n");
        //"do"
        next();
        compileSubroutineCall();
        next();
         //";"
        vm.writePop(VMWriter.Segment.TEMP,0);
        vm.writecomment("</doStatement>\n");
    }
    public void compileReturn()throws IOException{
        //Compiles a returns statement
        vm.writecomment("<returnStatement>\n");
         //"return"
        next();
        if(!token.equals(";")){
            CompileExpression();
             //";"
        } else {
             //";"
            vm.writePush(VMWriter.Segment.CONSTANT, 0);
        }
        vm.writeReturn();

        vm.writecomment("</returnStatement>\n");
    }
    public void CompileExpression()throws IOException{
        //Compiles an expression
        vm.writecomment("<expression>\n");
        if(token.equals("printString")){
            System.out.println("stop");
        }
        CompileTerm();

        if(token.equals("+")||token.equals("-")||token.equals("*")||token.equals("/")||token.equals("&amp;")||token.equals("|")||token.equals("&lt;")||token.equals("&gt;")||token.equals("=")){
             // op (+,-,",etc)
            String op = token;
            next();



            CompileExpression();
            vm.writeArithmetic(vm.getCommand(op));
        } else {
//            unNext();//undo the previous next
        }
        vm.writecomment("</expression>\n");
    }
    public void CompileTerm()throws IOException{
        //Compiles a rem. If the current token is an identifier, the routine must distinguish between a variablem a array entry, or a subroutine call.
        // A single look-ahead token, which may be one of "[", "(", or ".", suffices to distinguish between the possibilities. And other token is not part of this term and should not be advanced over.
        vm.writecomment("<term>\n");
            if (tokenType == TokenType.identifier || tokenType == TokenType.keyword) {
                next();
                if (token.equals("[")) {
                    expressionArray = true;
                    unNext();
                    //varName
                    String varName = token;
                    //TODO: vmwrite ARRAY
                    next();
                    //"["
                    next();
                    CompileExpression();
                    //"]"
                    vm.writePush(getSeg(varName), st.indexOf(varName));
                    vm.writeArithmetic(VMWriter.Command.ADD);
                    vm.writePop(VMWriter.Segment.POINTER, 1);
                    vm.writePush(VMWriter.Segment.THAT, 0);

                } else if (token.equals("(") || token.equals(".")) {
                    unNext();
                    compileSubroutineCall();
                } else {
                    unNext();
                    //varName
                    if (token.equals("false") || token.equals("null")) {
                        vm.writePush(VMWriter.Segment.CONSTANT, 0);
                    } else if (token.equals("true")) {
                        vm.writePush(VMWriter.Segment.CONSTANT, 1);
                        vm.writeArithmetic(VMWriter.Command.NEG);
                    } else if (token.equals("this")) {
                        vm.writePush(VMWriter.Segment.POINTER, 0);
                    } else {
                        vm.writePush(getSeg(token), st.indexOf(token));
                    }
                }
                next();
                vm.writecomment("</term>\n");
                return;
            }

            if (token.equals("(")) {
                //"("
                next();
                CompileExpression();
                //")"
                next();
                vm.writecomment("</term>\n");
                return;
            }
            if (token.equals("-") || token.equals("~")) {
                //unaryOp
                String op = token;
                next();
                CompileTerm();
                if (op.equals("-")) {
                    vm.writeArithmetic(VMWriter.Command.NEG);
                } else {
                    vm.writeArithmetic(VMWriter.Command.NOT);
                }
                vm.writecomment("</term>\n");
                return;
            }
            if (tokenType == TokenType.integerConstant || tokenType == TokenType.stringConstant || tokenType == TokenType.symbol) {

                switch (tokenType) {
                    case stringConstant:
                        vm.writePush(VMWriter.Segment.CONSTANT, token.length());
                        vm.writeCall("String.new", 1);
                        char[] ch = token.toCharArray();
                        for (int i = 0; i < token.length(); i++) {
                            int ascii = (int) ch[i];
                            vm.writePush(VMWriter.Segment.CONSTANT, ascii);
                            vm.writeCall("String.appendChar", 2);
                        }
                        break;
                    case integerConstant:
                        vm.writePush(VMWriter.Segment.CONSTANT, Integer.parseInt(token));
                        break;
                    case symbol:
                        vm.writeArithmetic(vm.getCommand(token));
                        break;

                }
                next();
                vm.writecomment("</term>\n");
                return;
            }
//        next();
        vm.writecomment("</term>\n");
    }
    public int CompileExpressionList()throws IOException{
        //Compiles a (possibly empty) comma-separated list of expressions
        int numberOfExpressions = 0;
        vm.writecomment("<expressionList>\n");
        if(token.equals(")")){
            vm.writecomment("</expressionList>\n");
            return numberOfExpressions;
        } else {
            CompileExpression();
            numberOfExpressions++;

            if (token.equals(",")) {
                while (token.equals(",")) {
                    //","
                    next();
                    CompileExpression();
                    numberOfExpressions++;
                }
            } else {
//            unNext();
            }
            vm.writecomment("</expressionList>\n");
        }
        return numberOfExpressions;
    }
    public void compileSubroutineCall()throws IOException{
        // Compiles subroutine call
        vm.writecomment("<subroutineCall>\n");
        boolean method;
        int numberOfExpressions = 0;
        next();
        if(token.equals("(")) {
            //Method call inside class
            unNext();
             //subroutineName
            String functionName = token;
            next();//"("
            next();
            vm.writePush(VMWriter.Segment.POINTER,0);
            numberOfExpressions = CompileExpressionList();
             //")"
            vm.writeCall(className+"."+functionName, numberOfExpressions +1);
        } else if(token.equals(".")) {
            //method call if String classname = a var name
            // constructor or function call is String className = a class name
            unNext();//className|varName
            String className = token;
            method = !(st.typeOf(className)==null);
            next();//"."
            next();//subroutineName
            String subroutineName = token;
            next();//"("
            next();  //")"
            if (method) {
                vm.writePush(getSeg(className), st.indexOf(className));
                numberOfExpressions=1;
            }
            numberOfExpressions = numberOfExpressions+CompileExpressionList();
            if (method) { // in case of method call, "className" is a variableName and should be replaced with an actual class name.
                vm.writeCall(st.typeOf(className) + "." + subroutineName, numberOfExpressions);
            } else {
                vm.writeCall(className + "." + subroutineName, numberOfExpressions);
            }

        } else {
            unNext();
        }
        vm.writecomment("</subroutineCall>\n");
    }
    public void close()throws IOException{
        vm.close();
    }

    
}
