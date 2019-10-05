package com.hack;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
    private Scanner vm;
    public String line;
    private String[] argStringArray;

    // initialize with string containing address of .vm-file
    public Parser(){
        try {
            vm = new Scanner(Main.file);
        } catch(IOException e){e.printStackTrace();}
    }

    //check if there are more commands in the .vm-file
    public Boolean hasMoreCommands() {
        return vm.hasNextLine();
    }

    public void advance(){
        String checkLine = vm.nextLine().trim();
        while(checkLine == null ||
                checkLine.equals("") ||
                checkLine.substring(0,2).equals("//")
        ){
            if(vm.hasNextLine()) {
                checkLine = vm.nextLine().trim();
            }
        }
        //finally
        line = checkLine;
        argparser();
    }

    public Commands commandType(){
        //arithmetic
        if(argStringArray[0].equals("add")||
                argStringArray[0].equals("sub")||
                argStringArray[0].equals("neg")||
                argStringArray[0].equals("eq")||
                argStringArray[0].equals("gt")||
                argStringArray[0].equals("lt")||
                argStringArray[0].equals("and")||
                argStringArray[0].equals("or")||
                argStringArray[0].equals("not")){
            return Commands.C_ARITHMETIC;
        }
        //pop
        if(argStringArray[0].equals("pop")){
            return Commands.C_POP;
        }
        //push
        if(argStringArray[0].equals("push")){
            return Commands.C_PUSH;
        }
        //label
        if(argStringArray[0].equals("label")){
            return Commands.C_LABEL;
        }
        //goto
        if(argStringArray[0].equals("goto")){
            return Commands.C_GOTO;
        }
        //if
        if(argStringArray[0].equals("if-goto")){
            return Commands.C_IF;
        }
        //function
        if(argStringArray[0].equals("function")){
            return Commands.C_FUNCTION;
        }
        //return
        if(argStringArray[0].equals("return")){
            return Commands.C_RETURN;
        }
        //call
        if(argStringArray[0].equals("call")){
            return Commands.C_CALL;
        }
        return null;
    }

    private void argparser(){
        String templine = line;
        argStringArray = templine.split("\\s");
    }

    public String arg1(){
        if (commandType().equals(Commands.C_ARITHMETIC)) {
            return argStringArray[0];
        }
        if(!commandType().equals(Commands.C_RETURN)){
            return argStringArray[1];
        }
        return null;
//        StringBuilder sb = new StringBuilder();
//        String tempLine = line;
//        if(commandType().equals(Commands.C_ARITHMETIC)){
//            return line;
//        }
//        if(!commandType().equals(Commands.C_RETURN)){
//            // drop letters until (first) blank space
//            while(!tempLine.substring(0,1).equals(" ")){
//                tempLine = tempLine.substring(1);
//            }
//            //skip the blank space
//            tempLine = tempLine.substring(1);
//            // add letters to sb until (next) blank space or
//            while(!(tempLine.length() == 0)&&
//                    !tempLine.substring(0,1).equals(" ")){
//                sb.append(tempLine.substring(0,1));
//                tempLine = tempLine.substring(1);
//            }
//        }
//        return sb.toString();
    }

    public int arg2(){
        Commands ct = commandType();
        if(ct.equals(Commands.C_PUSH) ||
                ct.equals(Commands.C_POP)||
                ct.equals(Commands.C_FUNCTION)||
                ct.equals(Commands.C_CALL)) {
            return Integer.parseInt(argStringArray[2].trim());
        }
        return 0;
//        String tempLine = line;
//        Commands ct = commandType();
//        StringBuilder sb = new StringBuilder();
//
//        if(ct.equals(Commands.C_PUSH) ||
//                ct.equals(Commands.C_POP)||
//                ct.equals(Commands.C_FUNCTION)||
//                ct.equals(Commands.C_CALL)) {
//            // drop letters until first blank space
//            while(!tempLine.substring(0, 1).equals(" ")) {
//                tempLine = tempLine.substring(1);
//            }
//            //skip the blank space
//            tempLine = tempLine.substring(1);
//            // drop letters until second blank space
//            while(!tempLine.substring(0, 1).equals(" ")) {
//                tempLine = tempLine.substring(1);
//            }
//            //skip the blank space
//            tempLine = tempLine.substring(1);
//
//            while(!tempLine.isEmpty()){
//                sb.append(tempLine.substring(0,1));
//                tempLine = tempLine.substring(1);
//            }
//            return Integer.parseInt(sb.toString());
//        }
//        return 0;
    }
}
