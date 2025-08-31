package edu.itba.converter.exchange.httpclient;

import edu.itba.converter.exchange.HttpClient;
import edu.itba.converter.exchange.HttpResponse;
import kong.unirest.core.Unirest;

import java.util.Map;

public class UnirestHttpClient implements HttpClient {

	@Override
	public HttpResponse get(String url, Map<String, Object> queryParams, Map<String, String> headers) {
		try {
			final var response = Unirest.get(url).queryString(queryParams).headers(headers).asString();
			return new HttpResponse(response.getStatus(), response.getBody());
		} catch (final Exception e) {
			System.err.println("Error: " + e.getMessage());
			return new HttpResponse(500, "Internal Server Error");
		}
	}
}
