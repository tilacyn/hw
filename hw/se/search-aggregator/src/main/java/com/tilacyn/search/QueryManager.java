package com.tilacyn.search;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class QueryManager {
    private ActorSystem system = ActorSystem.create("actor-system");
    private int masterActorNumber = 0;

    @Getter
    private Map<String, Map<String, SearchResponse>> queryResponseMap = new HashMap<>();

    public ActorRef processQuery(String query) {
        ActorRef masterActor = system.actorOf(Props.create(MasterActor.class, queryResponseMap),
                String.format("master-actor-%d", masterActorNumber++));
        masterActor.tell(query, ActorRef.noSender());
        return masterActor;
    }

    public void shutdown() {
        system.terminate();
    }


}
