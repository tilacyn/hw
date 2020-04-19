package com.tilacyn.trader.office.rest;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;


@AllArgsConstructor
public class EmulatorRestService {
    private int port;
    private String ip;
    private static final String urlTemplate = "http://%s:%d/%s";


    @SneakyThrows
    public Double getQuote(String symbol) {
        URIBuilder builder = new URIBuilder(String.format(urlTemplate, ip, port, "quote"))
                    .addParameter("symbol", symbol);
        HttpGet get = new HttpGet(builder.build());
        return request(get, Double::valueOf);
    }

    @SneakyThrows
    public double buy(String symbol, int qty) {
        URIBuilder builder = new URIBuilder(String.format(urlTemplate, ip, port, "buy"))
                .addParameter("symbol", symbol)
                .addParameter("qty", String.valueOf(qty));
        HttpPut put = new HttpPut(builder.build());
        return request(put, Double::valueOf);
    }

    @SneakyThrows
    public double sell(String symbol, int qty) {
        URIBuilder builder = new URIBuilder(String.format(urlTemplate, ip, port, "sell"))
                .addParameter("symbol", symbol)
                .addParameter("qty", String.valueOf(qty));
        HttpPut put = new HttpPut(builder.build());
        return request(put, Double::valueOf);
    }

    public <T> T request(HttpRequestBase requestBase, Function<String, T> fromStringConverter) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpResponse response = client.execute(requestBase);
            InputStream inputStream = response.getEntity().getContent();
            System.out.println("request sent");
            String responseString = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
            return fromStringConverter.apply(responseString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
