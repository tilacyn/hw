package msqueue;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

public class MSQueue implements Queue {
    private AtomicReference<Node> head = new AtomicReference<>();
    private AtomicReference<Node> tail = new AtomicReference<>();

    public MSQueue() {
        Node dummy = new Node(0);
        this.head.set(dummy);
        this.tail.set(dummy);
    }

    @Override
    public void enqueue(int x) {
        Node newTail = new Node(x);

        while (true) {
            Node curTail = tail.get();
            Node curNext = curTail.next.get();

            if (tail.get() == curTail) {
                if (curNext == null) {
                    if (curTail.next.compareAndSet(null, newTail)) {
                        tail.compareAndSet(curTail, newTail);
                        return;
                    }
                } else {
                    tail.compareAndSet(curTail, curNext);
                }
            }

        }

    }

    @Override
    public int dequeue() {
        while (true) {
            Node curHead = head.get();
            Node curTail = tail.get();
            Node headNext = curHead.next.get();
            if (curTail == curHead) {
                if (headNext == null) {
                    throw new NoSuchElementException();
                } else {
                    tail.compareAndSet(curTail, headNext);
                }
            } else {
                int result = headNext.x;
                if (head.compareAndSet(curHead, headNext)) {
                    return result;
                }
            }
        }
    }

    @Override
    public int peek() {
        while (true) {
            Node curHead = head.get();
            Node curTail = tail.get();
            Node headNext = curHead.next.get();

            if (curTail == curHead) {
                if (headNext == null) {
                    throw new NoSuchElementException();
                } else {
                    tail.compareAndSet(curTail, headNext);
                }
            } else if (head.compareAndSet(curHead, curHead)) {
                return curHead.next.get().x;
            }
        }
    }

    private class Node {
        final int x;
        AtomicReference<Node> next = new AtomicReference<>();

        Node(int x) {
            this.x = x;
        }
    }
}