package com.tilacyn.search;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class SearchResponse {
    private List<SingleResponse> results;
    private SearchEngine engine;

    @AllArgsConstructor
    @Getter
    public static class SingleResponse {
        private String link;

        public static SingleResponse from(Object o) {
            return new SingleResponse(o.toString());
        }
    }

    public static SearchResponse fromJSON(JSONArray responseJSON, SearchEngine engine) throws JSONException {
        List<SingleResponse> results = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            results.add(SingleResponse.from(responseJSON.get(i)));
        }
        return new SearchResponse(results, engine);
    }
}
