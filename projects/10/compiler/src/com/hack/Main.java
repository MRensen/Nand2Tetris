package com.hack;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static String address;
    public static BufferedReader reader;
    public static Tokenizer tokenizer;
    public static CompilationEngine engine;
    private static File[] files;
    public static File file;
    public static String name;
    public static String outputAddress;
    public static String parent;


    public static void main(String[] args) {
        // write your code here

        String testAddress = "/home/mark/Documents/nand2tetris/projects/10/Square";
        String tempname;
        switch (args.length) {
            case 0:
                address = testAddress;
                core();
                break;
            case 1:
                address = args[0];
                core();
                break;
            case 2:
                throw new Error("usage: VMTranslator arg1");
        }
    }

    public static void core() {
        String tempname;

        if (address.endsWith(".jack")) { //if input is file
            try {
                reader = new BufferedReader(new FileReader(address));
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(address);
            name = file.getName();
            parent = file.getParent();
            outputAddress = parent + "/" + name.substring(0, name.lastIndexOf(".")) + ".xml";
            tokenizer = new Tokenizer();
            coreLoop();

        } else { // if input is directory
            file = new File(address);
            files = file.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jack");
                }
            });
            parent = address;
            for (File loopfile : files) {
                try {
                    reader = new BufferedReader(new FileReader(loopfile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                file = loopfile;
                name = file.getName();
                outputAddress = parent +"/" + name.substring(0, name.lastIndexOf(".")) + ".xml";
                tokenizer = new Tokenizer();
                coreLoop();
            }
        }
    }

    public static void coreLoop() {
        // the loop that runs it all
        while (tokenizer.hasMoreTokens()) {
            tokenizer.advance();
        }
        engine = new CompilationEngine(tokenizer.getTokenList());
        //engine.setTokenType(tokenizer.getType());
        //engine.setToken(tokenizer.getCurrentToken());
        try {
            reader.close();
            tokenizer.close();
            engine.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
