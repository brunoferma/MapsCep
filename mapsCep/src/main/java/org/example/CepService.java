package org.example;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;

public class CepService {
    private static final String BASE_VIACEP_URL = "https://viacep.com.br/ws/";
    private static final String BASE_GOOGLE_GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    public static String buscarEndereco(String cep) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_VIACEP_URL + cep + "/json/")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code: " + response);
            }
            return response.body().string();
        }
    }

    public static Endereco buscarCoordenadas(String enderecoCompleto, String apiKey) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String url = BASE_GOOGLE_GEOCODING_URL + URLEncoder.encode(enderecoCompleto, "UTF-8") + "&key=" + apiKey;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code: " + response);
            }

            Gson gson = new Gson();
            GoogleGeoResponse geoResponse = gson.fromJson(response.body().string(), GoogleGeoResponse.class);

            if (geoResponse != null && geoResponse.getResults().length > 0) {
                var location = geoResponse.getResults()[0].getGeometry().getLocation();
                Endereco endereco = new Endereco();
                endereco.setLatitude(location.getLat());
                endereco.setLongitude(location.getLng());
                return endereco;
            } else {
                System.out.println("Nenhum resultado encontrado para o endereço: " + enderecoCompleto);
                return null;
            }
        }
    }
}