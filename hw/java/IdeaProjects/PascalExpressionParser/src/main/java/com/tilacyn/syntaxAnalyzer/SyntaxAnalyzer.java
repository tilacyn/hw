package com.tilacyn.syntaxAnalyzer;

import com.tilacyn.lexicalAnalyzer.Token;

import java.util.ArrayList;

/**
 * Parser
 * Parses a given string s if it matches the format of Pascal boolean expressions
 *
 * Example of usage if you are given a string s to parse:
 *
 * LexicalAnalyzer la = new LexicalAnalyzer(s);
 * la.analyze();
 * SyntaxAnalyzer parser = new SyntaxAnalyzer(la.getTokens());
 * parser.parse();
 * parser.print();
 */
public class SyntaxAnalyzer {
    private Node head = new Node("E");

    private ArrayList<Token> tokens;

    // pointer at the current token
    private int p = 0;

    /**
     * initializes new SyntaxAnalyzer with the given token array
     * @param tokens token array
     */
    public SyntaxAnalyzer(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    private Token next() {
        return tokens.get(p);
    }

    /**
     * parses the token array
     * @return true if parse successfully done, throws exception otherwise
     * @throws ParseException in case the token array doesn't match the format of pascal boolean formulas
     */
    public boolean parse() throws ParseException {
        parse(head);
        if (next() == Token.END) {
            return true;
        }
        throw new ParseException("Expected end of string, found " + next().name());
    }

    private void parse(Node node) throws ParseException {
        String s = node.getQ();
        switch (s) {
            case "E" :
                node.add(new Node("T"));
                node.add(new Node("E'"));
                break;
            case "T" :
                if (next() == Token.LPAREN) {
                    node.add(new Node("L"));
                    node.add(new Node("E"));
                    node.add(new Node("R"));
                    break;
                }
                if (next() == Token.NOT) {
                    node.add(new Node("N"));
                    node.add(new Node("E"));
                    break;
                }
                if (next() == Token.VAR) {
                    node.add(new Node("V"));
                    break;
                }
                exit("Expected (, not or a variable, found " + next().name());
            case "E'":
                if (next() == Token.AND) {
                    node.add(new Node("A"));
                    node.add(new Node("E"));
                    break;
                }
                if (next() == Token.OR) {
                    node.add(new Node("O"));
                    node.add(new Node("E"));
                    break;
                }
                break;
            case "A":
                if (next() == Token.AND) {
                    p++;
                    break;
                }
                exit("Expected and, found " + next().name());
            case "O":
                if (next() == Token.OR) {
                    p++;
                    break;
                }
                exit("Expected or, found " + next().name());
            case "N":
                if (next() == Token.NOT) {
                    p++;
                    break;
                }
                exit("Expected not, found " + next().name());
            case "L":
                if (next() == Token.LPAREN) {
                    p++;
                    break;
                }
                exit("Expected (, found " + next().name());
            case "R":
                if (next() == Token.RPAREN) {
                    p++;
                    break;
                }
                exit("Expected ), found " + next().name());
            case "V":
                if (next() == Token.VAR) {
                    p++;
                    break;
                }
                exit("Expected a variable, found " + next().name());
        }
        for (Node child : node.getChildren()) {
            parse(child);
        }
    }

    /**
     * prints the parse tree in format node.print = node (node.child1.print()) ... (node.childN.print())
     */
    public void print() {
        head.print();
        System.out.println();
    }

    private void exit(String message) throws ParseException {
        print();
        throw new ParseException(message);
    }


}


