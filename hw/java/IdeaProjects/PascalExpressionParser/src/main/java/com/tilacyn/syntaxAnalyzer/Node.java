package com.tilacyn.syntaxAnalyzer;

import java.util.ArrayList;

class Node {
    private String q;

    private ArrayList<Node> children = new ArrayList<>();

    Node(String q) {
        this.q = q;
    }

    void add(Node node) {
        this.children.add(node);
    }

    String getQ() {
        return q;
    }

    ArrayList<Node> getChildren() {
        return children;
    }

    void print() {
        System.out.print(q);
        for (Node child : children) {
            System.out.print(" (");
            child.print();
            System.out.print(") ");
        }
    }
}
