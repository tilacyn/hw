package com.tilacyn.lexicalAnalyzer;

public enum Token {
    AND, OR, NOT, VAR, LPAREN, RPAREN, END;

    public void print() {
        String s = "";
        switch (this) {
            case AND : s = "AND";
            break;
            case OR : s = "OR";
            break;
            case LPAREN: s = "LPAREN";
            break;
            case RPAREN: s = "RPAREN";
            break;
            case NOT : s = "NOT";
            break;
            case VAR : s = "VAR";
            break;
        }
        System.out.print(s);
    }
}
