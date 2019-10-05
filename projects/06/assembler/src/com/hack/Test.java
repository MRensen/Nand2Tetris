package com.hack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        String[][] pairs = {
                {"0", "0101010"}, {"1", "0111111"}, {"-1", "0111010"}, {"D", "0001100"}, {"A", "0110000"}, {"!D", "0001101"}, {"!A", "0110001"}, {"-D", "0001111"}, {"-A", "0110011"}, {"D+1", "0011111"},
                {"A+1", "0110111"}, {"D-1", "0001110"}, {"A-1", "0110010"}, {"D+A", "0000010"}, {"D-A", "0010011"}, {"A-D", "0000111"}, {"D&A", "0000000"}, {"D|A", "0010101"}, {"M", "1110000"},
                {"!M", "1110001"}, {"-M", "1110011"}, {"M+1", "1110111"}, {"M-1", "1110010"}, {"D+M", "1000010"}, {"D-M", "1010011"}, {"M-D", "1000111"}, {"D&M", "1000000"}, {"D|M", "1010101"}
        };
int i = 0;
        for(String[] pair : pairs){
            if(pair[1].length() != 7){
                System.out.println("wrong bitesize for " + pair[0]);
            } else {
                i++;
                System.out.println("no problem " + i);
            }
        }
    }
//    File file = null;
//
//    public ASMReader (File file){
//        this.file = file;
//    }
//
//    BufferedReader br = null;
//
//    // line
//    String line = null;
//
//    try {
//        br = new BufferedReader(new FileReader(file));
//        while ((line = br.readLine()) != null){
//
//        }
//
//    } catch(IOException e){
//        e.printStackTrace();
//    }finally {
//        if(br != null){
//            try {
//                br.close();
//            } catch(IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
