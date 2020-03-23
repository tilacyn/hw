package com.tilacyn.search;

import akka.actor.ActorRef;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class SearchTest {

    @Test
    public void testNoDelay() {
        test(new StubServerWrapper(false), 15);
    }

    @Test
    public void testBigDelay() {
        test(new StubServerWrapper(5), 0);
    }

    @Test
    public void testDifferentDelays() {
        test(new StubServerWrapper(5, 0, 5), 5);
        test(new StubServerWrapper(5, 0, 0), 10);
    }

    private void test(StubServerWrapper stubServer, int expected) {
        stubServer.start();
        QueryManager queryManager = new QueryManager();
        ActorRef ref = queryManager.processQuery("a");
        while (!ref.isTerminated()) {
        }
        assertEquals(expected,
                queryManager.getQueryResponseMap().get("a").values().stream()
                .map(SearchResponse::getResults)
                .mapToLong(List::size)
                .sum());
        stubServer.stop();
        queryManager.shutdown();
    }


}