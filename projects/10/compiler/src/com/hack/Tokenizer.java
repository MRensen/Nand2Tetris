package com.hack;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tokenizer {
//    private FileWriter writer;
    private char currentchar;
    private String currentToken;
    private TokenType type;
    private List<Map.Entry<String, TokenType>> tokenList = new ArrayList<>();

    public Tokenizer(){
//        try {
//            writer = new FileWriter(Main.outputAddress);
//            writer.write("<tokens>\n");
//        } catch(IOException e){e.printStackTrace();}
    }

    public boolean hasMoreTokens() {
        int intread = 0;
        try{
            intread = Main.reader.read();
        } catch(IOException e){e.printStackTrace(); return false;}

        if(intread == -1){
            return false;
        }
        currentchar = (char) intread;
        return true;
    }

    public void advance(){
        String readAhead = readAhead();
        //Gets the next token from the input and makes it the current token. This method should be called only if hasMoreTokens is true. Initially there is no current token.
        try {
            if(currentchar == '/'){
                if(readAhead.equals("/") ){
                    Main.reader.readLine();
                    return;
                }
                if (readAhead.equals("*")){
                    String line = Main.reader.readLine();
                    while(!line.contains("*/")){
                        line = Main.reader.readLine();
                    }
                    return;
                }
            }

            currentToken = buildToken().trim();

            if (currentToken.isEmpty()) {
                return;
            }
//            System.out.println(currentToken);
            type = tokenType();

//            switch (type) {
//                case keyword:
//                    writer.write("<" + type.toString().toLowerCase() + ">" + currentToken + "</" + type.toString().toLowerCase() + ">\n");
//                    break;
//                case symbol:
//                    switch (currentToken) {
//                        case "<":
//                            writer.write("<" + type.toString().toLowerCase() + ">" + "&lt;" + "</" + type.toString().toLowerCase() + ">\n");
//                            break;
//                        case ">":
//                            writer.write("<" + type.toString().toLowerCase() + ">" + "&gt;" + "</" + type.toString().toLowerCase() + ">\n");
//                            break;
//                        case "&":
//                            writer.write("<" + type.toString().toLowerCase() + ">" + "&amp;" + "</" + type.toString().toLowerCase() + ">\n");
//                            break;
//                        case "\'":
//                            writer.write("<" + type.toString().toLowerCase() + ">" + "&quot;" + "</" + type.toString().toLowerCase() + ">\n");
//                            break;
//                        default:
//                            writer.write("<" + type.toString().toLowerCase() + ">" + symbol() + "</" + type.toString().toLowerCase() + ">\n");
//                            break;
//                    }
//                    break;
//                case integerConstant:
//                    writer.write("<" + "integerConstant" + ">" + intVal() + "</" + "integerConstant" + ">\n");
//                    break;
//                case identifier:
//                    writer.write("<" + type.toString().toLowerCase() + ">" + identifier() + "</" + type.toString().toLowerCase() + ">\n");
//                    break;
//                case stringConstant:
//                    writer.write("<" + "stringConstant" + ">" + stringVal() + "</" + "stringConstant" + ">\n");
//                    break;
//                default:
//                    break;
//            }
//            writer.flush();

            if(type == TokenType.symbol) {
                switch (currentToken) {
                    case "<":
                        currentToken = "&lt;" ;
                        break;
                    case ">":
                        currentToken = "&gt;" ;
                        break;
                    case "&":
                        currentToken = "&amp;";
                        break;
                    case "\'":
                        currentToken = "&quot;";
                        break;
                }
            }
            if (type == TokenType.stringConstant){
                currentToken = currentToken.substring(1, (currentToken.length()-1));
            }
            tokenList.add(createEntry(currentToken,type));

        }catch (Exception e){e.printStackTrace();}
    }

    private String readAhead(){
        char line = ' ';
        try {
            Main.reader.mark(1000);
            line = (char) Main.reader.read();
            Main.reader.reset();
        }catch(IOException e){e.printStackTrace();}
        return String.valueOf(line);
    }

    private String buildToken() {
        StringBuilder sb = new StringBuilder();
        String readAhead = readAhead();
        boolean cont = true;
        boolean stringconst = false;
        boolean identifier = false;
        while(cont ) {
            switch (currentchar) {
                case ' ':
                case '\n':
                case'\r':
                    sb.append(currentchar);
                    return sb.toString();
                case '{':
                case '}':
                case '(':
                case ')':
                case '[':
                case ']':
                case '.':
                case ',':
                case ';':
                case '+':
                case '-':
                case '*':
                case '/':
                case '&':
                case '|':
                case '<':
                case '>':
                case '=':
                case '~':
                    if(stringconst || identifier){
                        return sb.toString();
                    }
                    return String.valueOf(currentchar);
                case '"':
                    sb.append(currentchar);
                    while(!readAhead.equals("\"")) {
                        readAhead = readAhead();
                        hasMoreTokens();
                        sb.append(currentchar); // if the xml file contains '"', remove this line.
                    }
                    return sb.toString();
                default:
                    while(! String.valueOf(currentchar).isEmpty()) {
                        sb.append(currentchar);
                        readAhead = readAhead();
                        if (readAhead.equals("{" ) || readAhead.equals( "}" ) || readAhead.equals( "(" ) || readAhead.equals( ")" ) || readAhead.equals( "[" ) || readAhead.equals( "]" ) || readAhead.equals( "." ) || readAhead.equals( "," ) || readAhead.equals( ";" ) || readAhead.equals( "+" ) || readAhead.equals( "-" ) || readAhead.equals( "*" ) || readAhead.equals(
                                "/" ) || readAhead.equals( "&" ) || readAhead.equals( "|" ) || readAhead.equals( "<" ) || readAhead.equals( ">" ) || readAhead.equals( "=" ) || readAhead.equals( "~")|| readAhead.equals(" ") || readAhead.equals("\n")){
                            return sb.toString();
                        }
                        hasMoreTokens();
                    }
                    break;
            }
        }
        return sb.toString();
    }

    public TokenType tokenType() {
        if (currentToken.equals("{") || currentToken.equals("}") || currentToken.equals("(") || currentToken.equals(")") || currentToken.equals("[") || currentToken.equals("]") || currentToken.equals(".") || currentToken.equals(",") || currentToken.equals(";") || currentToken.equals("+") || currentToken.equals("-") || currentToken.equals("*") || currentToken.equals(
                "/") || currentToken.equals("&") || currentToken.equals("|") || currentToken.equals("<") || currentToken.equals(">") || currentToken.equals("=") || currentToken.equals("~")) {

            type = TokenType.symbol;
            return TokenType.symbol;
        }

        try {
            int check = Integer.parseInt(currentToken);
            if (check >= 0 && check <= 32767) {
                type = TokenType.integerConstant;
                return TokenType.integerConstant;
            }
        } catch (NumberFormatException e) {
        }

        for (Keyword key : Keyword.values()) {
            if (key.toString().equals(currentToken.toUpperCase())) {
                type = TokenType.keyword;
                return TokenType.keyword;
            }
        }

        if (currentToken.startsWith("\"") && currentToken.endsWith("\"")) {
            type = TokenType.stringConstant;
            return TokenType.stringConstant;
        }

        if (!currentToken.startsWith("[0-9]") && !currentToken.isEmpty()) {
            type = TokenType.identifier;
            return TokenType.identifier;
        }


        //TODO: add StringConstant and identifier

        //Returns the type of the current token, as a constant.
        return null;
    }

    public Keyword keyWord(){
        //Returns the keyword which is the current token, as a constant. This method should be called only if tokenType is keyword
        return Keyword.valueOf(currentToken.toUpperCase());
    }

    public char symbol(){
        //Returns the character which is the current token. Should be called only if tokenType is Symbol.
             return currentchar;
    }

    public String identifier(){
        //Returns the identifier which is the current token. Should be called only if tokenType is identifier.
        return currentToken;
    }

    public int intVal(){
        // Returns the integer value of the current token. Should be called only if tokenType is integerConstant.
            return Integer.parseInt(currentToken);

    }

    public String stringVal(){
        //Returns the string value of the current token, without the two enclosing double quotes. Should be called only if tokenType is stringConstant.
        return currentToken.substring(1, (currentToken.length()-1));
    }

    public void close() throws IOException{
//            writer.write("</tokens>");
//            writer.flush();
//            writer.close();
    }

    public String getCurrentToken() {
        return currentToken;
    }

    public TokenType getType() {
        return type;
    }

    public List<Map.Entry<String, TokenType>> getTokenList(){
        return tokenList;
    }

    private Map.Entry<String,TokenType> createEntry(String entry1, TokenType entry2){
        Map.Entry<String,TokenType> m =new AbstractMap.SimpleEntry<>(entry1,entry2);
        return m;
    }
}
