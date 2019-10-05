package com.hack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
    private Scanner asm = null;
    private String line = null;
    private int counter = -1; //programme starts with incrementing the counter from first instruction onward. Counter must be 0 for first instruction, so counter is therefor initialized at 0-1

    public Parser(File file) {
        try {
            asm = new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean hasMoreCommands() {
//        System.out.println(asm.lines().count());
        return (asm.hasNextLine());
    }

    public void advance() {
        line = asm.nextLine().trim();
        while(line == null ||
                line.equals("") ||
                line.substring(0,2).equals("//")
                ){
            if(asm.hasNextLine()) {
                line = asm.nextLine().trim();
            }
        }
        if(!(commandType().equals(Commands.L_COMMAND))){
            counter++;
        }
    }

    public Commands commandType() {
        //a-instruction
        if (line.substring(0, 1).equals("@")) {
            return Commands.A_COMMAND;
        }
        //l-instruction a.k.a. symbol
        if (line.substring(0, 1).equals("(")) {
            return Commands.L_COMMAND;
        }
        //c-instruction
        return Commands.C_COMMAND;
    }

    public String symbol() {
        if(commandType().equals(Commands.A_COMMAND)) {
            //a-command
            return line.substring(1);
        }
        if(commandType().equals(Commands.L_COMMAND)) {
            //l-command
            int length = line.length() - 1; //forlast char in string (last = ")")
            return line.substring(1, length);
        }
        return null;
    }

    public String dest() {
        if (commandType().equals(Commands.C_COMMAND)) {
            StringBuilder sb = new StringBuilder();
            String templine = line;
            while (templine.length()>0 && templine.charAt(0) != '=') {
                sb.append(templine.charAt(0));
                templine = templine.substring(1);
            }
            if(sb.length()>0 && !(sb.toString().equals(line))) {
                return sb.toString();
            } else {
                return null;
            }
        }
        return null;
    }

    public String comp() {
        if (commandType().equals(Commands.C_COMMAND)) {
            StringBuilder sb = new StringBuilder();
            String templine = line;
            //skip chars until you encounter "="
            while (templine.length()>0 && templine.charAt(0) != '=') {
                templine = templine.substring(1);
            }
            // skip "="
            if(templine.length() > 0) {
                templine = templine.substring(1);
            } else {
                templine = line;
            }
            //keep the next chars until you encounter ";"
            while(templine.length() > 0 && templine.charAt(0) != ';') {
                sb.append(templine.charAt(0));
                templine = templine.substring(1);
            }
            if(sb.length()>0) {
                return sb.toString();
            } else {
                return null;
            }        }
        return null;
    }

    public String jump() {
        if (commandType().equals(Commands.C_COMMAND)) {
            StringBuilder sb = new StringBuilder();
            String templine = line;
            //skip first char until you encounter ";"
            while(templine.length() > 0 && templine.charAt(0) != ';') {
                templine = templine.substring(1);
            }
            //skip ";"
            if(templine.length()>0) {
                templine = templine.substring(1);
            }
            //keep the next chars
            while(templine.length() > 0) {
                sb.append(templine.charAt(0));
                templine = templine.substring(1);
            }
            if(sb.length() > 0) {
                return sb.toString();
            } else {
                return null;
            }
        }
        return null;
    }

    public int getCounter(){
        return counter;
    }

}



//    public static String parse(String line){
//        // parse the asm line into its components.
//
//        String binary = null;
//        // a-instructions:
//        if(line.substring(0,1).equals('@')){
//            String value = line.substring(1); // get everything after the "@"
//            int number = Integer.parseInt(value); // turn to int
//            binary = Integer.toBinaryString(number); //turn int to binary
//            while(binary.length() < 16){ // make sure the binary string is 15 characters long
//                StringBuilder sb = new StringBuilder("0");
//                sb.append(binary);
//                binary = sb.toString();
//            }
//        }
//        //c-instructions:
//        else {
//
//        }
//
//    }

