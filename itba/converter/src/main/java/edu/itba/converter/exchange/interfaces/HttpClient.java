package edu.itba.converter.exchange.interfaces;

import edu.itba.converter.exchange.models.HttpResponse;

import java.util.Map;

public interface HttpClient {

    HttpResponse get(String url, Map<String, Object> queryParams, Map<String, String> headers);
}
