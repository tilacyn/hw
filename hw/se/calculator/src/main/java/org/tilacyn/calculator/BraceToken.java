package org.tilacyn.calculator;

import java.util.Objects;

public class BraceToken implements PriorityToken {
    private static final int PRIORITY = 1;

    private final BraceSide side;

    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    public BraceToken(boolean isOpen) {
        this.side = isOpen ? BraceSide.L : BraceSide.R;
    }

    public boolean isOpen() {
        return side == BraceSide.L;
    }

    public boolean isClose() {
        return side == BraceSide.R;
    }

    public int getPriority() {
        return PRIORITY;
    }

    private enum BraceSide {
        L, R
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BraceToken that = (BraceToken) o;
        return side == that.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(side);
    }
}
