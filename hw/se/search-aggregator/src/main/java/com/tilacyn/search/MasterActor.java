package com.tilacyn.search;


import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import scala.concurrent.duration.Duration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MasterActor extends UntypedActor {
    private static int number = 0;

    private Map<String, Map<String, SearchResponse>> queryResponseMap;
    private Map<String, SearchResponse> responseMap = new HashMap<>();

    public MasterActor(Map<String, Map<String, SearchResponse>> queryResponseMap) {
        getContext().setReceiveTimeout(Duration.fromNanos(0.5 * 1_000_000_000));
        this.queryResponseMap = queryResponseMap;
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof SearchResponse) {
            handleSearchResponse((SearchResponse) message);
        }

        if (message instanceof String) {
            handleQuery((String) message);
        }

        if (message instanceof ReceiveTimeout) {
            stop();
        }
    }

    private void handleQuery(String message) {
        this.queryResponseMap.put(message, responseMap);
        System.out.println(String.format("Processing message: %s", message));
        Arrays.stream(SearchEngine.values())
                .map(this::createChildActor)
                .forEach(child -> child.tell(message, self()));
    }

    private void handleSearchResponse(SearchResponse searchResponse) {
        this.responseMap.put(searchResponse.getEngine().toString(), searchResponse);
        if (responseMap.keySet().size() == 3) {
            stop();
        }
    }

    private void stop() {
        printResult();
        getContext().stop(self());
    }

    private void printResult() {
        responseMap.forEach(
                (engine, response) -> {
                    System.out.println(String.format("Result from Engine: %s", engine));
                    response.getResults().forEach(
                            result -> System.out.println(result.getLink())
                    );
                }
        );
    }

    private ActorRef createChildActor(SearchEngine engine) {
        String name = String.format("child_%s_%d", engine, number++);
        return getContext().actorOf(Props.create(ChildActor.class, engine), name);
    }
}
