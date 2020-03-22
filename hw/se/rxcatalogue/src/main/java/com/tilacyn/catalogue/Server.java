package com.tilacyn.catalogue;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServer;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import rx.Observable;

import java.util.Arrays;
import java.util.List;

public class Server {
    private CatalogueManager catalogueManager;

    public Server(CatalogueManager catalogueManager) {
        this.catalogueManager = catalogueManager;
    }

    public void start() {
        HttpServer.newServer(8080)
                .start((request, response) -> {
                    Observable<String> responseObservable = from(request).handle();
                    return response.writeString(responseObservable);

                }).awaitShutdown();
    }

    private Request from(HttpServerRequest<ByteBuf> request) {
        String path = request.getDecodedPath().substring(1);
        List<String> params = Arrays.asList(path.split("/"));

        if (params.get(0).equals("register")) {
            return new RegisterRequest(params.get(1), params.get(2));
        }
        if (params.get(0).equals("list")) {
            return new GetGoodsRequest(params.get(1));
        }
        if (params.get(0).equals("add")) {
            return new AddGoodRequest(params.get(1), Double.parseDouble(params.get(2)));
        }
        throw new IllegalArgumentException();
    }


    private interface Request {
        Observable<String> handle();
    }

    @Data
    @AllArgsConstructor
    private class RegisterRequest implements Request {
        private String id;
        private String currency;

        @Override
        public Observable<String> handle() {
            return catalogueManager.addUser(id, currency).map(s -> "User has been successfully added!");
        }
    }

    @AllArgsConstructor
    private class GetGoodsRequest implements Request {
        private String id;

        @Override
        public Observable<String> handle() {
            return catalogueManager.observeGoods(id);
        }
    }

    @AllArgsConstructor
    private class AddGoodRequest implements Request {
        private String name;
        /**
         * all the prices added are in RUB
         */
        private double price;


        @Override
        public Observable<String> handle() {
            return catalogueManager.addGood(name, price).map(s -> "Good successfully added!");
        }
    }
}
