package com.hack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VMWriter {
    private FileWriter fw;
    public enum Segment{
        CONSTANT, ARGUMENT, LOCAL, STATIC, THIS, THAT, POINTER, TEMP
    }

    public enum Command{
        ADD, SUB, NEG, EQ, GT, LT, AND, OR, NOT, DIV, MULT
    }

    public Command getCommand(String symbol){
        switch(symbol){
            case "+":
                return Command.ADD;
            case "-":
                return Command.SUB;
            case "=":
                return Command.EQ;
            case ">":
            case "&gt;":
                return Command.GT;
            case "<":
            case "&lt;":
                return Command.LT;
            case "&":
            case "&amp;":
                return Command.AND;
            case "|":
                return Command.OR;
            case"/":
                return Command.DIV;
            case "*":
                return Command.MULT;
        }
        System.out.println("should not get here, symbol = " + symbol);
        return null;
    }

    public VMWriter(File file) {
        try {
            fw = new FileWriter(file);
        } catch(IOException e){e.printStackTrace();}
    }



    public void writePush(Segment seg, int index){

        try{
            fw.write("push " + seg.toString().toLowerCase() + " " +  index + "\n");
            fw.flush();
        } catch (Exception e){e.printStackTrace();
            System.out.println(Main.engine.token);}
    }

    public void writePop(Segment seg, int index){

        try{
            fw.write("pop " + seg.toString().toLowerCase() + " " +  index + "\n");
            fw.flush();
        } catch (IOException e){e.printStackTrace();}
    }

    public void writeArithmetic(Command command){

        try{
            if(command == Command.DIV){
                writeCall("Math.divide", 2);
            } else if(command == Command.MULT){
                writeCall("Math.multiply", 2);
            } else {
                fw.write(command.toString().toLowerCase() + "\n");
            }
            fw.flush();
        } catch (IOException e){e.printStackTrace();}
    }

    public void writeLabel(String label){

        try{
            fw.write("label " + label + "\n");
            fw.flush();
        } catch (IOException e){e.printStackTrace();}
    }

    public void writeGoto(String label){

        try{
            fw.write("goto " + label + "\n");
            fw.flush();
        } catch (IOException e){e.printStackTrace();}
    }

    public void writeIf(String label){

        try{
            fw.write("if-goto " + label + "\n");
            fw.flush();
        } catch (IOException e){e.printStackTrace();}
    }

    public void writeCall(String name, int nArgs){

        try{
            fw.write("call " + name + " " + nArgs + "\n");
            fw.flush();
        } catch (IOException e){e.printStackTrace();}
    }

    public void writeFunction(String name, int nArgs){

        try{
            fw.write("function " + name + " " + nArgs + "\n");
            fw.flush();
        } catch (IOException e){e.printStackTrace();}
    }

    public void writeReturn(){

        try{
            fw.write("return\n");
            fw.flush();
        } catch (IOException e){e.printStackTrace();}
    }

    public void writecomment(String comment){
        try{
            fw.write("//"+ comment);
            fw.flush();
        } catch(IOException e){e.printStackTrace();}
    }



    public void close(){
        try {
            fw.flush();
            fw.close();
        } catch (IOException e){e.printStackTrace();}
    }




}
