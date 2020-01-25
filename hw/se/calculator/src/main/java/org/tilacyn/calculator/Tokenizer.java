package org.tilacyn.calculator;

import org.tilacyn.calculator.OperationToken.OperationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tokenizer {
    private static Map<Character, OperationType> operationTypeMap = new HashMap<>();

    static {
        operationTypeMap.put('+', OperationType.PLUS);
        operationTypeMap.put('*', OperationType.MULT);
        operationTypeMap.put('/', OperationType.DIVIDE);
        operationTypeMap.put('-', OperationType.MINUS);
    }

    private String input = "";

    private int ptr = 0;

    private List<Token> tokens = new ArrayList<>();

    public List<Token> tokenize(String input) {
        this.input = input.replaceAll(" ", "");

        while (true) {
            State nextState = resolveNextState();
            if (nextState == null) {
                tokens.add(new EndToken());
                return tokens;
            }
            nextState.processInput();
        }
    }

    public State resolveNextState() {
        if (ptr >= input.length()) {
            return null;
        }
        if (Character.isDigit(input.charAt(ptr))) {
            return new NumberReadingState();
        } else if (input.charAt(ptr) == '(' || input.charAt(ptr) == ')') {
            return new BraceReadingState();
        } else if (operationTypeMap.containsKey(input.charAt(ptr))) {
            return new OperationReadingState();
        }

        throw new IllegalStateException("unexpected token");
    }

    private interface State {
        void processInput();
    }

    public class NumberReadingState implements State {
        int number = 0;

        @Override
        public void processInput() {
            while (ptr < input.length() && Character.isDigit(input.charAt(ptr))) {
                number *= 10;
                number += input.charAt(ptr) - '0';
                ptr++;
            }
            tokens.add(new NumberToken(number));
        }
    }

    public class OperationReadingState implements State {

        @Override
        public void processInput() {
            tokens.add(new OperationToken(operationTypeMap.get(input.charAt(ptr))));
            ptr++;
        }
    }

    public class BraceReadingState implements State {

        @Override
        public void processInput() {
            BraceToken braceToken = new BraceToken(input.charAt(ptr) == '(');
            tokens.add(braceToken);
            ptr++;
        }
    }
}
