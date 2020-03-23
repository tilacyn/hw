package com.tilacyn.search;

import akka.actor.UntypedActor;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;


import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ChildActor extends UntypedActor {

    private final SearchEngine searchEngine;

    public ChildActor(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        CloseableHttpClient client = HttpClients.createDefault();
        URIBuilder builder = new URIBuilder(String.format("http://localhost:8080/%s", searchEngine))
                .addParameter("query", o.toString());
        HttpGet get = new HttpGet(builder.build());
        HttpResponse response = client.execute(get);
        InputStream inputStream = response.getEntity().getContent();
        System.out.println("request sent");
        String responseString = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
//        System.out.println(responseString);
        JSONArray jsonArray = new JSONArray(responseString);
        getContext().parent().tell(SearchResponse.fromJSON(jsonArray, searchEngine), self());
    }
}
