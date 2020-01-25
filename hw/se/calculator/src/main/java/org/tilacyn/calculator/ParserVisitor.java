package org.tilacyn.calculator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ParserVisitor implements TokenVisitor {
    private Stack<PriorityToken> operations = new Stack<>();
    private List<Token> polishSequence = new ArrayList<>();

    public void visit(@NotNull BraceToken token) {
        if (token.isOpen()) {
            operations.push(token);
            return;
        }
        while (!operations.isEmpty() && operations.peek().getPriority() != token.getPriority()) {
            polishSequence.add(operations.pop());
        }
        operations.pop();
    }

    public void visit(@NotNull OperationToken token) {
        while (!operations.isEmpty() && operations.peek().getPriority() >= token.getPriority()) {
            polishSequence.add(operations.pop());
        }
        operations.push(token);
    }

    public void visit(NumberToken token) {
        polishSequence.add(token);
    }

    @Override
    public void visit(EndToken token) {
//        if (operations.size() > 1) {
//            throw new IllegalStateException("Bad token input sequence");
//        }
        while (!operations.isEmpty()) {
            polishSequence.add(operations.pop());
        }
        polishSequence.add(new EndToken());
    }

    public List<Token> getPolishSequence() {
        return polishSequence;
    }
}
