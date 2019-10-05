package com.hack;

import java.util.HashMap;
import java.util.Map;

public class Code {
    private SymbolTable symbolTable;
    private Map<String, String> destMap;
    private Map<String, String> compMap;
    private Map<String, String> jumpMap;
    private int n=16;
    public Code(SymbolTable st){
        this.symbolTable = st;

        String[][] destpairs = {
                {null, "000"},
                {"M", "001"},
                {"D", "010"},
                {"A", "100"},
                {"MD", "011"},
                {"AM", "101"},
                {"AD", "110"},
                {"AMD", "111"},
        };
        destMap = loopPairs(destpairs);

        String[][] comppairs = {
                {"0", "0101010"}, {"1", "0111111"}, {"-1", "0111010"}, {"D", "0001100"}, {"A", "0110000"}, {"!D", "0001101"}, {"!A", "0110001"}, {"-D", "0001111"}, {"-A", "0110011"}, {"D+1", "0011111"},
                {"A+1", "0110111"}, {"D-1", "0001110"}, {"A-1", "0110010"}, {"D+A", "0000010"}, {"D-A", "0010011"}, {"A-D", "0000111"}, {"D&A", "0000000"}, {"D|A", "0010101"}, {"M", "1110000"},
                {"!M", "1110001"}, {"-M", "1110011"}, {"M+1", "1110111"}, {"M-1", "1110010"}, {"D+M", "1000010"}, {"D-M", "1010011"}, {"M-D", "1000111"}, {"D&M", "1000000"}, {"D|M", "1010101"}
        };
        compMap = loopPairs(comppairs);

        String[][] jumppairs = {
                {null, "000"},
                {"JGT", "001"},
                {"JEQ", "010"},
                {"JGE", "011"},
                {"JLT", "100"},
                {"JNE", "101"},
                {"JLE", "110"},
                {"JMP", "111"}
        };
        jumpMap = loopPairs(jumppairs);
    }

    private HashMap<String, String> loopPairs(String[][] pairs){
        HashMap<String, String> map = new HashMap<String, String>();
        for (String[] pair : pairs) {
            map.put(pair[0], pair[1]);
        }
        return map;

    }

    public String dest(String mnem){
        return destMap.get(mnem);
    }

    public String comp(String mnem){
        return compMap.get(mnem);
    }

    public String jump(String mnem){
        return jumpMap.get(mnem);
    }

    //TODO: does not work with Symbols before symbol table in implemented
    public String symbol(String symbol){
        String binary;
        int value;
        try{
            value = Integer.parseInt(symbol);
        }catch(NumberFormatException e){
            if(symbolTable.contains(symbol)){
                value=symbolTable.getAddress(symbol);
            } else {
                symbolTable.addEntry(symbol, n );
                value = n;
                n++;
            }
        }

        binary = Integer.toBinaryString(value);

        while(binary.length() < 16){
            String temp = "0" + binary;
            binary = temp;
        }
        return binary;
    }
}
