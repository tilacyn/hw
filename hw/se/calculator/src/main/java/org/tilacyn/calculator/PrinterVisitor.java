package org.tilacyn.calculator;

import org.tilacyn.calculator.OperationToken.OperationType;

import java.util.HashMap;

public class PrinterVisitor implements TokenVisitor {
    private static final HashMap<OperationType, String> operationFormatMap = new HashMap<>();

    static {
        operationFormatMap.put(OperationType.PLUS, "+");
        operationFormatMap.put(OperationType.MINUS, "-");
        operationFormatMap.put(OperationType.MULT, "*");
        operationFormatMap.put(OperationType.DIVIDE, "/");
    }

    @Override
    public void visit(BraceToken token) {
        String output = token.isOpen() ? "(" : ")";
        System.out.print(output);
    }

    @Override
    public void visit(OperationToken token) {
        System.out.print(operationFormatMap.get(token.getOperationType()) + " ");
    }

    @Override
    public void visit(NumberToken token) {
        System.out.print(token.getNumber() + " ");
    }

    @Override
    public void visit(EndToken token) {
        System.out.println();
    }
}
