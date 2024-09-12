package org.example;

import com.google.gson.Gson;
import lombok.Data;

@Data
public class Endereco {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private double latitude;
    private double longitude;

    public static Endereco fromJson(String json) {
        return new Gson().fromJson(json, Endereco.class);
    }
}