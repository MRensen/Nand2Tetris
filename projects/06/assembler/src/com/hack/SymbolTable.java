package com.hack;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Integer> map;
    public SymbolTable(){
        String[][] pairs ={
                {"SP", "0"},
                {"LCL", "1"},
                {"ARG", "2"},
                {"THIS", "3"},
                {"THAT", "4"},
                {"SCREEN", "16384"},
                {"KBD", "24576"},
                {"R0", "0"},
                {"R1", "1"},
                {"R2", "2"},
                {"R3", "3"},
                {"R4", "4"},
                {"R5", "5"},
                {"R6", "6"},
                {"R7", "7"},
                {"R8", "8"},
                {"R9", "9"},
                {"R10", "10"},
                {"R11", "11"},
                {"R12", "12"},
                {"R13", "13"},
                {"R14", "14"},
                {"R15", "15"}

        };
        map = new HashMap<String, Integer>();
        for (String[] pair : pairs) {
            map.put(pair[0], Integer.parseInt(pair[1]));
        }
    }

    public void addEntry (String symbol, int address){
        map.put(symbol, address);
    }

    public Boolean contains(String symbol){
        return map.containsKey(symbol);
    }

    public int getAddress(String symbol){
        return map.get(symbol);
    }
}
