package com.tilacyn.syntaxAnalyzer;

import com.tilacyn.lexicalAnalyzer.LexicalAnalyzeException;
import com.tilacyn.lexicalAnalyzer.LexicalAnalyzer;
import com.tilacyn.lexicalAnalyzer.Token;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class SyntaxAnalyzerTest {

    @Test
    public void test() throws IOException, ParseException, LexicalAnalyzeException {
        test("v");
        test("not v");
        test("(v)");
        test("not v and g");
        test("not v or g");
        test("v or not g");
        test("v or g and h");
        test("not v or (g and h)");
        test("(v or g) and (not h)");
        test("v or g");
        test("not not not not not not not not g");
        test("v and not g");
        test("not v and g");
        test("(v and g) or (g or v)");
        test("(not v)     and g");
        test("(not v or not a)   and (not g and not (not v))");
        test("a and b and c and d and e and f and g and h");
        test("a and (b and (c and (d and (e and (f and (g and h))))))");
        test("a or b and c or d and e or f and g or h");
        test("a or (b and (c or (d and (e or (f and (g or h))))))");
        test("(((((a)))))");
        test("(((((a))))) and (((b and g)))");
        test("(a) and (g)");
        test("not a or (g)");

        StringBuilder s = new StringBuilder("v");
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                s.append(" and v");
            } else {
                s.append(" or v");
            }
            test(s.toString());
        }

    }


    @Test
    public void randomTest() throws ParseException, IOException, LexicalAnalyzeException {
        String str = "v";

        Random rand = new Random();

        for (int i = 0; i < 15; i++) {
            int n = rand.nextInt();
            System.out.println(str);
            if (n % 3 == 0) {
                str = "not " + str;
            } else if (n % 3 == 1) {
                str = "(" + str + ")" + " and " + "(" + str + ")";
            } else if (n % 3 == 2) {
                str = "(" + str + ")" + " or " + "(" + str + ")";
            }
            test(str);
        }
    }



    @Test
    public void testFail() throws IOException, LexicalAnalyzeException {
        testFail("v and g)");
        testFail("((v) and g");
        testFail("((v and g)");
        testFail("(v and g))");
        testFail(" or v");
        testFail(" and v");
        testFail(" or v and ");
        testFail("(v and g");
        testFail("v and ");
        testFail("v or ");
        testFail("not ");
        testFail("()");
        testFail(") v");
        testFail("v (");
        testFail("v and g or ");
        testFail("(v and not g");
        testFail("(not v and) g");
        testFail("(v and g) not (g or v)");
        testFail("not g not v");
        testFail("(not v or not a)  or and (not g and not (not v)))");
        testFail("(((((a))))) and (((b and g))))");
    }

    private void testFail(String s) throws IOException, LexicalAnalyzeException {
        SyntaxAnalyzer parser = new SyntaxAnalyzer(makeTokens(s));
        boolean exceptionOccured = false;
        try{
            parser.parse();
        } catch (ParseException e) {
            exceptionOccured = true;
        }
        if (!exceptionOccured) {
            fail();
        }
    }

    private void test(String s) throws IOException, ParseException, LexicalAnalyzeException {
        SyntaxAnalyzer parser = new SyntaxAnalyzer(makeTokens(s));
        assertTrue(parser.parse());
    }

    private ArrayList<Token> makeTokens(String s) throws IOException, LexicalAnalyzeException {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(new StringReader(s));
        lexicalAnalyzer.analyze();
        return lexicalAnalyzer.getTokens();
    }

}