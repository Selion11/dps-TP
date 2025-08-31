package edu.itba.converter.exchange;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HttpClient {

	HttpResponse get(String url, Map<String, Object> queryParams, Map<String, String> headers);

	//TODO
	//.conf para los links de la api y la API KEY
	//Metodo get historico
	//Meteodo get cotizacion
	//Metodo get transformacion 1 a 1
}
