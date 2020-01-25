package org.tilacyn.calculator;

public interface Token {
    void accept(TokenVisitor visitor);
}
