package org.tilacyn.calculator;

import org.tilacyn.calculator.OperationToken.OperationType;

import java.util.HashMap;
import java.util.Stack;
import java.util.function.BiFunction;

public class CalcVisitor implements TokenVisitor {
    private Stack<Integer> numbers = new Stack<>();

    private Integer result = null;

    private static final HashMap<OperationType, BiFunction<Integer, Integer, Integer>> MATH_OPERATIONS = new HashMap<>();

    static {
        MATH_OPERATIONS.put(OperationType.PLUS, Integer::sum);
        MATH_OPERATIONS.put(OperationType.MINUS, (a, b) -> b - a);
        MATH_OPERATIONS.put(OperationType.MULT, (a, b) -> a * b);
        MATH_OPERATIONS.put(OperationType.DIVIDE, (a, b) -> b / a);
    }

    @Override
    public void visit(BraceToken token) {
        throw new IllegalStateException("Unexpected brace in input sequence");
    }

    @Override
    public void visit(OperationToken token) {
        int first = numbers.pop();
        int second = numbers.pop();
        numbers.push(MATH_OPERATIONS.get(token.getOperationType()).apply(first, second));
    }

    @Override
    public void visit(NumberToken token) {
        numbers.push(token.getNumber());
    }

    @Override
    public void visit(EndToken token) {
        result = numbers.pop();
    }

    public Integer getResult() {
        return result;
    }
}
