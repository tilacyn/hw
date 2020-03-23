package com.tilacyn.search;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


public class StubServerWrapper {
    private WireMockServer server = new WireMockServer();
    private boolean withDelay;

    private Map<SearchEngine, Integer> delayMap = new HashMap<>();

    public StubServerWrapper(boolean withDelay) {
        this.withDelay = withDelay;
    }

    public StubServerWrapper(int delay) {
        Arrays.stream(SearchEngine.values()).forEach(engine -> delayMap.put(engine, delay));
        this.withDelay = true;
    }

    public StubServerWrapper(int yandexDelay, int googleDelay, int bingDelay) {
        delayMap.put(SearchEngine.YANDEX, yandexDelay);
        delayMap.put(SearchEngine.GOOGLE, googleDelay);
        delayMap.put(SearchEngine.BING, bingDelay);
        this.withDelay = true;
    }

    public void start() {
        server.start();
        configureFor("localhost", 8080);

        Arrays.asList(SearchEngine.values()).forEach(
                searchEngine -> stubFor(get(urlMatching(String.format("/%s.*", searchEngine)))
                        .willReturn(resolveDelay(aResponse().withBody(readFileToString(String.format("src/main/resources/%s.json", searchEngine))), searchEngine)))
        );
    }

    private ResponseDefinitionBuilder resolveDelay(ResponseDefinitionBuilder builder, SearchEngine searchEngine) {
        if (withDelay) {
            return builder.withFixedDelay(delayMap.get(searchEngine) * 1000);
        } else {
            return builder;
        }
    }

    private String readFileToString(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void stop() {
        server.stop();
    }

}
