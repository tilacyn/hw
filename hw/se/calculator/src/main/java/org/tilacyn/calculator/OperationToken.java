package org.tilacyn.calculator;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class OperationToken implements PriorityToken {
    private static HashMap<OperationType, Integer> priority = new HashMap<>();

    static {
        priority.put(OperationType.PLUS, 2);
        priority.put(OperationType.MINUS, 2);
        priority.put(OperationType.MULT, 3);
        priority.put(OperationType.DIVIDE, 3);
    }

    private final OperationType type;

    public OperationToken(OperationType type) {
        this.type = type;
    }

    public void accept(@NotNull TokenVisitor visitor) {
        visitor.visit(this);
    }

    public OperationType getOperationType() {
        return type;
    }

    public int getPriority() {
        return priority.get(type);
    }

    public enum OperationType {
        PLUS,
        MINUS,
        MULT,
        DIVIDE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationToken that = (OperationToken) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
