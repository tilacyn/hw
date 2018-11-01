import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;



public class MerkeTreeTest {
    byte[][] blocks = new byte[100][8];

    private class Node{
        byte[] hash;
        Node l, r, p;
        Node(Node p) {
            this.p = p;
        }
    }


    Node head = new Node(null);

    {

    }


    void fillblocks() {
        for (int i = 0; i < 8; i++) {
            new Random().nextBytes(blocks[i]);
        }
    }

    String countNeighbour() {

    }

    @Test
    public void test1() {
        MerkeTree.h = 3;
        MerkeTree.neighbours = new String[3];
        fillblocks();

    }

}