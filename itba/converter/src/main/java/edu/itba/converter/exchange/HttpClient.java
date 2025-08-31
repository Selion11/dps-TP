package edu.itba.converter.exchange;

import java.util.Map;

public interface HttpClient {

	HttpResponse get(String url, Map<String, Object> queryParams, Map<String, String> headers);
}
