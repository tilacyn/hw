package org.tilacyn.calculator;

import org.junit.Test;
import org.tilacyn.calculator.OperationToken.OperationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class CommonTest {

    private HashMap<List<Token>, List<Token>> tokenizerSimpleTests = new HashMap<>();
    private HashMap<List<Token>, List<Token>> tokenizerBraceTests = new HashMap<>();

    private HashMap<List<Token>, List<Token>> parserSimpleTests = new HashMap<>();
    private HashMap<List<Token>, List<Token>> parserBraceTests = new HashMap<>();

    private HashMap<List<Token>, Integer> calcSimpleTests = new HashMap<>();
    private HashMap<List<Token>, Integer> calcBraceTests = new HashMap<>();

    {
        Token[] simple1 = new Token[] {n(3), plus(), n(4), end()};
        Token[] simple1Polish = new Token[] {n(3), n(4), plus(), end()};
        Token[] simple2 = new Token[] {n(3), plus(), n(4), mult(), n(5), end()};
        Token[] simple2Polish = new Token[] {n(3), n(4), n(5), mult(), plus(), end()};


        parserSimpleTests.put(asList(simple1), asList(simple1Polish));
        calcSimpleTests.put(asList(simple1Polish), 7);

        parserSimpleTests.put(asList(simple2), asList(simple2Polish));
        calcSimpleTests.put(asList(simple2Polish), 23);

        Token[] brace1 = new Token[] {l(), n(3), plus(), n(4), r(), mult(),
                l(), n(24), minus(), n(20), r(),  end()};
        Token[] brace1Polish = new Token[] {n(3), n(4), plus(), n(24), n(20), minus(), mult(), end()};
//        Token[] brace2 = new Token[] {n(3), plus(), n(4), mult(), n(5), end()};
//        Token[] simple2Polish = new Token[] {n(3), n(4), n(5), mult(), plus(), end()};

        parserBraceTests.put(asList(brace1), asList(brace1Polish));
        calcBraceTests.put(asList(brace1Polish), 28);
    }



    @Test
    public void parserSimpleTest() {
        parserTest(parserSimpleTests);
    }

    @Test
    public void parserBraceTest() {
        parserTest(parserBraceTests);
    }

    @Test
    public void calcSimpleTest() {
        calcTest(calcSimpleTests);
    }

    @Test
    public void calcBraceTest() {
        calcTest(calcBraceTests);
    }

    @Test
    public void integrationTest() {
        integrationTest("(3 + 4)", 7);
        integrationTest("(3 + 4) * ( 4 + 3)", 49);
        integrationTest("200 - 100", 100);
        integrationTest("(200 - 5) / 19", 10);
        integrationTest("81 / 3 / 3/3/3", 1);
        integrationTest("(((2 + 2) * 2 + 2) * 2 + 2) * 2", 44);
    }

    private void integrationTest(String input, int expected) {
        Tokenizer tokenizer = new Tokenizer();
        ParserVisitor parserVisitor = new ParserVisitor();
        PrinterVisitor printerVisitor = new PrinterVisitor();
        CalcVisitor calcVisitor = new CalcVisitor();

        List<Token> tokens = tokenizer.tokenize(input);
        tokens.forEach(token -> token.accept(parserVisitor));
        tokens.forEach(token -> token.accept(printerVisitor));

        List<Token> polish = parserVisitor.getPolishSequence();
        polish.forEach(token -> token.accept(printerVisitor));
        polish.forEach(token -> token.accept(calcVisitor));

        assertEquals(expected, (int) calcVisitor.getResult());
    }

    private void parserTest(HashMap<List<Token>, List<Token>> map) {
        for (List<Token> input : map.keySet()) {
            ParserVisitor parserVisitor = new ParserVisitor();
            input.forEach(token -> token.accept(parserVisitor));
            List<Token> expected = map.get(input);
            assertEquals(expected, parserVisitor.getPolishSequence());
        }
    }

    private void calcTest(HashMap<List<Token>, Integer> map) {
        for (List<Token> polish : map.keySet()) {
            CalcVisitor calcVisitor = new CalcVisitor();
            polish.forEach(token -> token.accept(calcVisitor));
            Integer expected = map.get(polish);
            assertEquals(expected, calcVisitor.getResult());
        }
    }


    private static NumberToken n(int n) {
        return new NumberToken(n);
    }

    private static OperationToken plus() {
        return new OperationToken(OperationType.PLUS);
    }

    private static OperationToken minus() {
        return new OperationToken(OperationType.MINUS);
    }

    private static OperationToken mult() {
        return new OperationToken(OperationType.MULT);
    }

    private static OperationToken div() {
        return new OperationToken(OperationType.DIVIDE);
    }

    private static BraceToken l() {
        return new BraceToken(true);
    }

    private static BraceToken r() {
        return new BraceToken(false);
    }

    private static EndToken end() {
        return new EndToken();
    }

    private static List<Token> asList(Token... tokens) {
        return new ArrayList<>(Arrays.asList(tokens));
    }

}