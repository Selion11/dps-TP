package edu.itba.converter.exchange;

import edu.itba.converter.exchange.implementations.UnirestHttpClient;
import edu.itba.converter.exchange.models.HttpResponse;
import kong.unirest.core.GetRequest;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UnirestHttpClientTest {


    @Test
    void testGetReturnsExpectedHttpResponse() {
        // GIVEN
        String url = "http://test.com";
        Map<String, Object> queryParams = Map.of("q", "test");
        Map<String, String> headers = Map.of("Authorization", "Bearer token");

        GetRequest mockRequest = mock(GetRequest.class);
        kong.unirest.core.HttpResponse<String> mockResponse = mock(kong.unirest.core.HttpResponse.class);

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn("{\"result\":\"ok\"}");
        when(mockRequest.queryString(queryParams)).thenReturn(mockRequest);
        when(mockRequest.headers(headers)).thenReturn(mockRequest);
        when(mockRequest.asString()).thenReturn(mockResponse);
        HttpResponse response;
        // WHEN
        try (MockedStatic<Unirest> unirestMock = mockStatic(Unirest.class)) {
            unirestMock.when(() -> Unirest.get(url)).thenReturn(mockRequest);

            UnirestHttpClient client = new UnirestHttpClient();
            response = client.get(url, queryParams, headers);
        }

        // THEN
        assertEquals(200, response.status());
        assertEquals("{\"result\":\"ok\"}", response.body());

    }

    @Test
    void testGetReturnsInternalServerErrorOnException() {
        //GIVEN
        String url = "http://test.com";
        Map<String, Object> queryParams = Map.of();
        Map<String, String> headers = Map.of();
        HttpResponse response;

        //WHEN
        try (MockedStatic<Unirest> unirestMock = mockStatic(Unirest.class)) {

            unirestMock.when(() -> Unirest.get(url)).thenThrow(new RuntimeException("Network error"));

            UnirestHttpClient client = new UnirestHttpClient();
            response = client.get(url, queryParams, headers);
        }

        //THEN
        assertEquals(500, response.status());
        assertEquals("Internal Server Error", response.body());
    }
}
