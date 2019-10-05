package com.hack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class CodeWriter {

    private int callCount = 0;
    private FileWriter fw;
    private int counter = 0;
    private String parent = Main.parent;
    private String fileName = Main.name;

    public CodeWriter(){
        try {
            fw = new FileWriter(parent + "/" + fileName +".asm"); //l-3, because the last 3 characters of name are ".vm"
        }catch(IOException e){e.printStackTrace();}
    }

    public void writeInit(){
        //TODO
        try{
            fw.write("//init\n" +
                    "@256\n" +
                    "D=A\n" +
                    "@SP\n" +
                    "M=D\n");
            writeCall("Sys.init",0);
            fw.flush();

        }catch(IOException e){e.printStackTrace();}
    }

    public void writeLabel(String label){
        try{
            fw.write("//label"+label+"\n" +
                    "("+label+")\n");
        }catch(IOException e){e.printStackTrace();}
    }

    public void writeGoto(String label){
        try{
            fw.write("//goto"+label+"\n" +
                    "@"+label+"\n" +
                    "0;JMP\n");
        }catch(IOException e){e.printStackTrace();}
    }

    public void writeIf(String label){
        try{
            fw.write("//ifgoto"+label+"\n" +
                    "@SP\n" +
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
//                    "A=A-1\n" +
                    "@"+label+"\n" +
                    "D;JNE\n");

        }catch(IOException e){e.printStackTrace();}
    }

    public void writeCall(String functionName, int numArgs){
        callCount++;
        //TODO
        try{
            fw.write("//call"+functionName+" "+numArgs+"\n" +
                    "@"+functionName+"$ret."+callCount+"\n" + //push retAddrLabel
                    "D=A\n" +
                    "@SP\n" +
                   // "M=M-1\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n" +
                    "@LCL\n" + // push LCL
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n" +
                    "@ARG\n" + //push ARG
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n" +
                    "@THIS\n" + //push THIS
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n" +
                    "@THAT\n" + // push THAT
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n" +
                    "@"+(5+numArgs)+"\n" + // ARG = SP - 5 - nArgs
                    "D=A\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "D=A-D\n" +
                    "@ARG\n" +
                    "M=D\n" +
                    "@SP\n" + // LCL = SP
                    "D=M\n" +
                    "@LCL\n" +
                    "M=D\n");
            writeGoto(functionName);
            fw.write("("+functionName+"$ret."+callCount+")\n");
        }catch(IOException e){e.printStackTrace();}
    }

    public void writeReturn(){
        //TODO
        try{
            fw.write("//return\n" +
                    "@LCL\n" + //endFrame=LCL
                    "D=M\n" +
                    "@endFrame\n" +
                    "M=D\n" +
                    "@5\n" + //retAddr = *(endFrame-5)
                    "A=D-A\n" +
                    "D=M\n" +
                    "@retAddr\n" +
                    "M=D\n" +
                    "@SP\n" + //*ARG = pop()
                    "M=M-1\n" +
                    "A=M\n" +
                    "D=M\n" +
                    "@ARG\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@ARG\n" + //SP= ARG+1
                    "D=M+1\n" +
                    "@SP\n" +
                    "M=D\n" +
                    "@endFrame\n" + //THAT = *(endFrame-1)
                    "A=M-1\n" +
                    "D=M\n" +
                    "@THAT\n" +
                    "M=D\n" +
                    "@2\n" + //THIS = *(endFrame-2)
                    "D=A\n" +
                    "@endFrame\n" +
                    "A=M-D\n" +
                    "D=M\n" +
                    "@THIS\n" +
                    "M=D\n" +
                    "@3\n" + //ARG = *(endFrame-3)
                    "D=A\n" +
                    "@endFrame\n" +
                    "A=M-D\n" +
                    "D=M\n" +
                    "@ARG\n" +
                    "M=D\n" +
                    "@4\n" + //LCL = *(endFrame-4)
                    "D=A\n" +
                    "@endFrame\n" +
                    "A=M-D\n" +
                    "D=M\n" +
                    "@LCL\n" +
                    "M=D\n" +
                    "@retAddr\n" +
                    "A=M\n" +
                    "0;JMP\n");
        }catch(IOException e){e.printStackTrace();}
    }

    public void writeFunction(String functionName, int numLocals){
        //TODO
        try{
            fw.write("//function "+functionName+" "+numLocals+"\n"+
                    "("+functionName+")\n");
            for(int i = 0; i<numLocals; i++){
                fw.write("@SP\n" +
                        "A=M\n" +
                        "M=0\n" +
                        "@SP\n" +
                        "M=M+1\n");
            }
        }catch(IOException e){e.printStackTrace();}
    }



    public void writeArithmetic(String command) {
        counter++;
        try {
            fw.write("//"+command+"\n");
            // (y = last element on stack, x = second to last element on stack)
            //(true = -1, false = 0)
            //add x+y
            //sub x-y
            //neg -y
            //eq x==y
            //gt x>y
            //lt x<y
            //and x and y
            //or x or y
            //not not y

            switch (command) {
                case "add":
                    fw.write("@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "M=D+M\n" +
                            "@SP\n" +
                            "M=M+1\n");
                    break;
                case "sub":
                    fw.write("@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "M=M-D\n" +
                            "@SP\n" +
                            "M=M+1\n");
                    break;
                case "neg":
                    fw.write("@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "M=-M\n" +
                            "@SP\n" +
                            "M=M+1\n");
                    break;
                case "eq":
                    fw.write("@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n"+
                            "D=M-D\n" +
                            "@FALSE"+counter+"\n"+
                            "D;JNE\n" +
                            "@SP\n" +
                            "A=M\n" +
                            "M=-1\n" +
                            "@SP\n" +
                            "M=M+1\n" +
                            "@END"+counter+"\n" +
                            "0;JMP\n" +
                            "(FALSE"+counter+")\n" +
                            "@SP\n" +
                            "A=M\n" +
                            "M=0\n" +
                            "@SP\n" +
                            "M=M+1\n" +
                            "(END"+counter+")\n" );
                    break;
                case "gt":
                    fw.write("@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" + //D=y
                            "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M-D\n" + //M=x
                            "@TRUE"+counter+"\n" +
                            "D;JGT\n" +
                            "@SP\n" +
                            "A=M\n" +
                            "M=0\n" +
                            "@SP\n" +
                            "M=M+1\n" +//false: x is not gt y
                            "@END"+counter+"\n" +
                            "0;JMP\n" +
                            "(TRUE"+counter+")\n" +
                            "@SP\n" +
                            "A=M\n" +
                            "M=-1\n" +
                            "@SP\n" +
                            "M=M+1\n" +
                            "(END"+counter+")\n");
                    break;
                case "lt":
                    fw.write("@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" + //D=y
                            "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M-D\n" + //M=x
                            "@TRUE"+counter+"\n" +
                            "D;JLT\n" +
                            "@SP\n" +
                            "A=M\n" +
                            "M=0\n" +
                            "@SP\n" +
                            "M=M+1\n" +//false: x is not gt y
                            "@END"+counter+"\n" +
                            "0;JMP\n" +
                            "(TRUE"+counter+")\n" +
                            "@SP\n" +
                            "A=M\n" +
                            "M=-1\n" +
                            "@SP\n" +
                            "M=M+1\n" +
                            "(END"+counter+")\n");
                    break;
                case "and":
                    fw.write("@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "M=D&M\n" +
                            "@SP\n" +
                            "M=M+1\n");
                    break;
                case "or":
                    fw.write("@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "D=M\n" +
                            "@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "M=D|M\n" +
                            "@SP\n" +
                            "M=M+1\n");
                    break;
                case "not":
                    fw.write("@SP\n" +
                            "M=M-1\n" +
                            "A=M\n" +
                            "M=!M\n" +
                            "@SP\n" +
                            "M=M+1\n");
                    break;
            }

            fw.flush();
        } catch(IOException e){e.printStackTrace();}
    }

    public void writePushPop(Commands command, String segment, int i) {
        counter++;
        try {
            fw.write("//push/pop "+command+" "+segment+" "+i+"\n");
            // push: addr = segment + i, *SP=*addr, SP++
            // pop: addr = segment + i, SP--, *addr=*SP
            // push local i
            // pop local i
            // push argument i
            // pop argument i
            // push this i
            // pop this i
            // push that i
            // pop this i

            // push constant i -> *SP=i, SP++

            // push static i
            // pop static i

            // pointer 0 == THIS, pointer 1 == THAT
            // push pointer i -> *SP = THIS/THAT, SP++
            // pop pointer i -> SP--, THIS/THAT = *SP

            // push temp i -> addr = 5 + i, *SP = *addr, SP++
            // pop temp i -> addr = 5 + i, SP--, *addr = *SP

            if (command.equals(Commands.C_POP)) {

                switch (segment) {
                    case "local":
                        fw.write("@LCL\n" +
                                "D=M\n" +
                                "@"+i+"\n" +
                                "D=D+A\n" +
                                "@addr\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M-1\n" +
                                "A=M\n" +
                                "D=M\n" +
                                "@addr\n" +
                                "A=M\n" +
                                "M=D\n");
                        break;
                    case "argument":
                        fw.write("@ARG\n" +
                                "D=M\n" +
                                "@"+i+"\n" +
                                "D=D+A\n" +
                                "@addr\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M-1\n" +
                                "A=M\n" +
                                "D=M\n" +
                                "@addr\n" +
                                "A=M\n" +
                                "M=D\n");
                        break;
                    case "this":
                        fw.write("@THIS\n" +
                                "D=M\n" +
                                "@"+i+"\n" +
                                "D=D+A\n" +
                                "@addr\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M-1\n" +
                                "A=M\n" +
                                "D=M\n" +
                                "@addr\n" +
                                "A=M\n" +
                                "M=D\n");
                        break;
                    case "that":
                        fw.write("@THAT\n" +
                                "D=M\n" +
                                "@"+i+"\n" +
                                "D=D+A\n" +
                                "@addr\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M-1\n" +
                                "A=M\n" +
                                "D=M\n" +
                                "@addr\n" +
                                "A=M\n" +
                                "M=D\n");
                        break;
                    case "constant":
                        System.out.println("cannot pop constant");
                        break;
                    case "static":
                        fw.write("@SP\n" +
                                "M=M-1\n" +
                                "A=M\n" +
                                "D=M\n" +
                                "@"+Main.className+"."+i+"\n" +
                                "M=D\n");
                        break;
                    case "pointer":
                        if(i==0){ //this pop
                            fw.write("@SP\n" +
                                    "M=M-1\n" +
                                    "A=M\n" +
                                    "D=M\n" +
                                    "@THIS\n" +
                                    "M=D\n");
                        }
                        if(i==1){ //that pop
                            fw.write("@SP\n" +
                                    "M=M-1\n" +
                                    "A=M\n" +
                                    "D=M\n" +
                                    "@THAT\n" +
                                    "M=D\n");
                        }
                        break;
                    case "temp":
                        fw.write("@"+i+"\n" +
                                "D=A\n" +
                                "@5\n" +
                                "D=D+A\n" +
                                "@addr\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M-1\n" +
                                "A=M\n" +
                                "D=M\n" +
                                "@addr\n" +
                                "A=M\n" +
                                "M=D\n");
                }
            }
            if (command.equals(Commands.C_PUSH)) {
                switch (segment) {
                    case "local":
                        fw.write("@LCL\n" +
                                "D=M\n" +
                                "@"+i+"\n" +
                                "A=D+A\n" +
                                "D=M\n" +
//                                "@addr\n" +
//                                "M=D\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1\n");
                        break;
                    case "argument":
                        fw.write("@ARG\n" +
                                "D=M\n" +
                                "@"+i+"\n" +
                                "A=D+A\n" +
                                "D=M\n" +
//                                "@addr\n" +
//                                "M=D\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1\n");
                        break;
                    case "this":
                        fw.write("@THIS\n" +
                                "D=M\n" +
                                "@"+i+"\n" +
                                "A=D+A\n" +
                                "D=M\n" +
//                                "@addr\n" +
//                                "M=D\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1\n");
                        break;
                    case "that":
                        fw.write("@THAT\n" +
                                "D=M\n" +
                                "@"+i+"\n" +
                                "A=D+A\n" +
                                "D=M\n" +
//                                "@addr\n" +
//                                "M=D\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1\n");
//                            "D=M\n" +
//                            "@"+i+"\n" +
//                            "D=D+A\n" +
//                                "@addr\n" +
//                                "M=D\n" +
//                            "@SP\n" +
//                            "A=M\n" +
//                            "M=D\n" +
//                            "@SP\n" +
//                            "M=M+1\n" +
//                                "@addr\n" +
//                                "A=M\n" +
//                                "M=D\n");
                        break;
                    case "constant":
                        fw.write("@"+i+"\n" +
                                "D=A\n"+
                                "@SP\n" +
                                "A=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1\n");
                        break;
                    case "static":
                        fw.write("@"+Main.className+"."+i+"\n" +
                                "D=M\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1\n");
                        break;
                    case "pointer":
                        if(i==0){//this push
                            fw.write("@THIS\n" +
                                    "D=M\n" +
                                    "@SP\n" +
                                    "A=M\n" +
                                    "M=D\n" +
                                    "@SP\n" +
                                    "M=M+1\n");
                        }
                        if(i==1){//that push
                            fw.write("@THAT\n" +
                                    "D=M\n" +
                                    "@SP\n" +
                                    "A=M\n" +
                                    "M=D\n" +
                                    "@SP\n" +
                                    "M=M+1\n");
                        }
                        break;
                    case "temp":
                        fw.write("@"+i+"\n" +
                                "D=A\n" +
                                "@5\n" +
                                "A=D+A\n" +
                                "D=M\n" +
                                "@SP\n" +
                                "A=M\n" +
                                "M=D\n" +
                                "@SP\n" +
                                "M=M+1\n");
                }
            }

            fw.flush();
        }catch(IOException e){e.printStackTrace();}

    }

    public void close(){
        try {
            fw.flush();
            fw.close();
        } catch(IOException e){e.printStackTrace();}
    }


}
