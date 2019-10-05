package com.hack;

import java.util.LinkedHashMap;

public class SymbolTable {
    private LinkedHashMap<String, String> className;
    private LinkedHashMap<String, String> classType;
    private LinkedHashMap<String, Kind> classKind;
    private LinkedHashMap<String, Integer> classNumber;
    private LinkedHashMap<String, String> subName;
    private LinkedHashMap<String, String> subType;
    private LinkedHashMap<String, Kind> subKind;
    private LinkedHashMap<String, Integer> subNumber;

    public SymbolTable(){
        classKind = new LinkedHashMap(); // STATIC, FIELD
        className = new LinkedHashMap();
        classNumber = new LinkedHashMap();
        classType = new LinkedHashMap();
        subName = new LinkedHashMap();
        subType = new LinkedHashMap();
        subKind = new LinkedHashMap(); // ARG, LOCAL
        subNumber = new LinkedHashMap();


    }

    public void startSubroutine(){
        subName = new LinkedHashMap();
        subType = new LinkedHashMap();
        subKind = new LinkedHashMap();
        subNumber = new LinkedHashMap();
    }

    public void define (String name, String type, Kind kind){
        // if subroutine scope
        if(kind == Kind.ARG || kind == Kind.LOCAL){
            subNumber.put(name,varCount(kind));
            subName.put(name, name);
            subKind.put(name, kind);
            subType.put(name,type);
        } else {  //if class scope
            classNumber.put(name, varCount(kind));
            classType.put(name, type);
            className.put(name, name);
            classKind.put(name, kind);
        }

    }

    public int varCount(Kind kind){
        int varCount = 0;
        // if subroutine scope
        if(kind == Kind.ARG || kind == Kind.LOCAL){
            for(Kind k : subKind.values()){
                if(k == kind){
                    varCount++;
                }
            }
        } else {  //if class scope
            for(Kind k : classKind.values()){
                if(k == kind){
                    varCount++;
                }
            }
        }
        return varCount;
    }

    public Kind kindOf(String name){
        if(subKind.containsKey(name)){
            return subKind.get(name);
        } else if(classKind.containsKey(name)){
            return classKind.get(name);
        } else {
            return Kind.None;
        }
    }

    public String typeOf(String name){
        if(subType.containsKey(name)){
            return subType.get(name);
        } else if(classType.containsKey(name)){
            return classType.get(name);
        } else {
            return null;
        }
    }

    public int indexOf(String name){
        if(subNumber.containsKey(name)){
            return subNumber.get(name);
        } else if(classNumber.containsKey(name)){
            return classNumber.get(name);
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "className=" + className +
                ", className=" + classType +
                ", classKind=" + classKind +
                ", classNumber=" + classNumber +
                ", subName=" + subName +
                ", subType=" + subType +
                ", subKind=" + subKind +
                ", subNumber=" + subNumber +
                '}';
    }
}
