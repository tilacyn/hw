package com.tilacyn.lexicalAnalyzer;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class LexicalAnalyzerTest {
    @Test
    public void test() throws IOException, LexicalAnalyzeException {
        test("vv", new Token[]{Token.VAR, Token.VAR, Token.END});
        test("v and ", new Token[]{Token.VAR, Token.AND, Token.END});
        test("a and a", new Token[]{Token.VAR, Token.AND, Token.VAR, Token.END});
        test("ava    a  v      a   ", new Token[]{Token.VAR, Token.VAR, Token.VAR, Token.VAR, Token.VAR, Token.VAR, Token.END});
        test("  )   (   ", new Token[]{Token.RPAREN, Token.LPAREN, Token.END});
        test("(cke)", new Token[]{Token.LPAREN, Token.VAR, Token.VAR, Token.VAR, Token.RPAREN, Token.END});
        test(" and  or not ", new Token[]{Token.AND, Token.OR, Token.NOT, Token.END});
        test("(a and b) or    (not c or d) and a", new Token[]{Token.LPAREN, Token.VAR, Token.AND, Token.VAR, Token.RPAREN, Token.OR,
                Token.LPAREN, Token.NOT, Token.VAR, Token.OR, Token.VAR, Token.RPAREN, Token.AND, Token.VAR, Token.END});
    }

    private void test(String s, Token[] expected) throws IOException, LexicalAnalyzeException {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(new StringReader(s));
        lexicalAnalyzer.analyze();
        ArrayList<Token> tokens = lexicalAnalyzer.getTokens();
        assertArrayEquals(expected, tokens.toArray());
    }



}