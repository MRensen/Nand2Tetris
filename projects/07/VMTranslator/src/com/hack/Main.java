package com.hack;

import javafx.scene.Parent;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
//    private static String[] names = new String[16];
//    private static String[] parents = new String[16];
//    private static String[] addresses = new String[16];
    public static File file = null;
//    private static int arrayIndex = 0;
    public static String name = null;
    public static String className = null;
    public static String parent = null;
    public static String address = null;
    private static File[] files = new File[16];
    public static Parser parser = null;
    public static CodeWriter codeWriter = null;

    public static void main(String[] args) throws IOException {
        String testAddress = "/home/mark/Documents/nand2tetris/projects/08/FunctionCalls/StaticsTest";
        String tempname;
        switch (args.length) {
            case 0:
                address = testAddress;
                core();
                codeWriter.close();
                break;
            case 1:
                address = args[0];
                core();
                codeWriter.close();
                break;
            case 2:
                throw new Error("usage: VMTranslator arg1");

        }

    }
    public static void core(){
        String tempname;
        if (address.endsWith(".vm")) {
            file = new File(address);
            tempname = file.getName();
            name = tempname.substring(0, tempname.lastIndexOf("."));
            className = name;
            parent = file.getParent();
            parser = new Parser();
            codeWriter = new CodeWriter();
//            codeWriter.writeInit();
            parse();
        } else {
            file = new File(address);
            files = file.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".vm");
                }
            });
            parent = address;
            name = file.getName();
            codeWriter = new CodeWriter();
                codeWriter.writeInit();
            for (File loopfile : files) {
                file = loopfile;
                className = file.getName().substring(0, file.getName().lastIndexOf("."));
                parser = new Parser();
                parse();
            }
        }
    }

    public static void parse () {

        // this is the core programme
           // System.out.println(name);
            while (parser.hasMoreCommands()) {
                parser.advance();
                //arithmetic command
                try{
                switch(parser.commandType()) {
                    // arithmetic
                    case C_ARITHMETIC:
                    codeWriter.writeArithmetic(parser.arg1());
                    break;
                //push/pop command
                    case C_POP:
                    case C_PUSH:
                    codeWriter.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
                    break;
                    case C_CALL:
                        codeWriter.writeCall(parser.arg1(), parser.arg2());
                        break;
                    case C_RETURN:
                        codeWriter.writeReturn();
                        break;
                    case C_FUNCTION:
                        codeWriter.writeFunction(parser.arg1(), parser.arg2());
                        break;
                    case C_IF:
                        codeWriter.writeIf(parser.arg1());
                        break;
                    case C_GOTO:
                        codeWriter.writeGoto(parser.arg1());
                        break;
                    case C_LABEL:
                        codeWriter.writeLabel(parser.arg1());
                        break;
                }
                }catch(NullPointerException e) {
                    System.out.println(parser.line);
                }
            }
    }

    public static int length(){
        int counter = 0;
        for(File f: files){
            if(f != null){
                counter++;
            }
        }
        return counter;
    }

//    public static void getnames(){
//        int i = arrayIndex;
//        name = names[i];
//        parent = parents[i];
//        address = addresses[i];
//    }
}
