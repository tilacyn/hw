package com.tilacyn.lexicalAnalyzer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

public class LexicalAnalyzer {
    private char[] input;
    private ArrayList<Token> tokens = new ArrayList<>();

    private final int MAX_SIZE = 10000;

    // pointer on the input
    private int p = 0;

    // number of chars in input
    private int n;

    public LexicalAnalyzer(Reader reader) throws IOException {
        char[] buff = new char[MAX_SIZE];
        //noinspection ResultOfMethodCallIgnored
        n = reader.read(buff);
        input = new char[n];
        System.arraycopy(buff, 0, input, 0, n);
    }

    public void analyze() throws LexicalAnalyzeException {
        try {
            //noinspection StatementWithEmptyBody
            while (analyzeNext());
        } catch (EndOfStringException e) {
            tokens.add(Token.END);
            return;
        }
        throw new LexicalAnalyzeException("Expected token, found " + input[p] + " at point " + p);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    private boolean analyzeNext() throws EndOfStringException {
        //noinspection StatementWithEmptyBody
        while (skipBlank());
        char c = next();
        if (c == '(') {
            tokens.add(Token.LPAREN);
            go(1);
            return true;
        }
        if (c == ')') {
            tokens.add(Token.RPAREN);
            go(1);
            return true;
        }

        if (is(" and ")) {
            tokens.add(Token.AND);
            go(5);
            return true;
        }
        if (is(" or ")) {
            tokens.add(Token.OR);
            go(4);
            return true;
        }
        if (is("not ")) {
            tokens.add(Token.NOT);
            go(4);
            return true;
        }
        if (c >= 'a' && c <= 'z') {
            tokens.add(Token.VAR);
            go(1);
            return true;
        }
        return false;
    }

    private boolean enough(String s) {
        return p <= n - s.length();
    }

    private boolean is(String s) {
        if (enough(s)) {
            for (int i = 0; i < s.length(); i++) {
                if (input[p + i] != s.charAt(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private char next() throws EndOfStringException {
        if (p == n) throw new EndOfStringException();
        return input[p];
    }

    private void go(int k) {
        p += k;
    }

    private boolean skipBlank() throws EndOfStringException {
        char c = next();
        if (c == '\n' || c == '\t') {
            go (1);
            return true;
        }
        if (c == ' ') {
            if (!is(" and ") && !is(" or ")) {
                go(1);
                return true;
            }
        }
        return false;
    }

}
