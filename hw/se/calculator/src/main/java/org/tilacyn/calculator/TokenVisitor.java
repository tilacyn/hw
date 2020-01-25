package org.tilacyn.calculator;

public interface TokenVisitor {
    void visit(BraceToken token);
    void visit(OperationToken token);
    void visit(NumberToken token);
    void visit(EndToken token);
}
