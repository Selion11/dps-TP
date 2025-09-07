package edu.itba.converter.exchange;
import edu.itba.converter.exchange.models.HttpResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HttpResponseTest {

    @Test
    void testHttpResponseFieldsAndToString() {
        //GIVEN
        int status = 200;
        String body = "{\"key\":\"value\"}";
        HttpResponse response = new HttpResponse(status, body);

        //WHEN
        String obtainedString = response.toString();

        //THEN
        assertEquals("HttpResponse{status=200, body='{\"key\":\"value\"}'}", obtainedString);
    }
}
