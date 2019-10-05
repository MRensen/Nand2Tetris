package com.hack;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;

public class Main {

    //Usage: Call this java file in the command line with 1 arg
    //       - The absolute address of the .asm file

    public static void main(String[] args) {
        // get .asm file from args
        String absoluteAsm = null;
        //TODO: remove static file, make dynamic
        File asm =   new File("/home/mark/Documents/nand2tetris/projects/06/rect/Rect.asm");
        String fileName = null;
        String parent = null;
        Parser parser;
        Code code;
        SymbolTable st = new SymbolTable();
        if (args.length > 0) {
            //TODO: uncomment
            //asm = new File(args[0]);
        }


        fileName = asm.getName();
        parent = asm.getParent();
        BufferedWriter bw = createWriter(parent+".hack");
        System.out.println(parent+".hack");

        //first sweep to identify all the labels
        parser = new Parser(asm);
        while(parser.hasMoreCommands()){
            parser.advance();
            if(parser.commandType() == Commands.L_COMMAND ){
                st.addEntry (parser.symbol(), parser.getCounter()+1);
            }
        }


        //read the file with parser
        parser = new Parser(asm);
        code = new Code(st);
        while(parser.hasMoreCommands()){
            parser.advance();
            if(parser.commandType() == Commands.L_COMMAND ){
                continue;
            }
            if(parser.commandType() == Commands.A_COMMAND){
                String binary = null;
                binary = code.symbol(parser.symbol());
                try {
                    bw.write(binary);
                    bw.newLine();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(parser.commandType() == Commands.C_COMMAND){
                String dest = code.dest(parser.dest());
                String comp = code.comp(parser.comp());
                String jump = code.jump(parser.jump());
                try {
                    bw.write("111" + comp + dest + jump);
                    bw.newLine();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }

        }
        try {
            bw.flush();
            bw.close();
        } catch(IOException e){e.printStackTrace();}

    }

    public static BufferedWriter createWriter(String file){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
        }catch (IOException e){
            e.printStackTrace();
        }
        return bw;

    }

//    public static void read(File asm){
//        BufferedReader br = null;
//
//        // "line" is the current line of code from the asm file
//        String line = null;
//
//        try {
//            br = new BufferedReader(new FileReader(asm));
//            while ((line = br.readLine()) != null){
//
//            }
//
//        } catch(IOException e){
//            e.printStackTrace();
//        }finally {
//            if(br != null){
//                try {
//                    br.close();
//                } catch(IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }
}
