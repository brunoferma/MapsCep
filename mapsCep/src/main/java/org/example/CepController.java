package org.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/cep")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CepController {

    private final Config config; // Dependência da classe Config

    // Construtor que recebe a classe Config
    public CepController(Config config) {
        this.config = config;
    }

    @GetMapping("/{cep}")
    public ResponseEntity<Endereco> buscarCep(@PathVariable String cep) {
        try {
            // Chama o serviço para buscar o endereço pelo cep.
            String json = CepService.buscarEndereco(cep);
            Endereco endereco = Endereco.fromJson(json);

            // Monta a string com os dados do endereço completo
            String enderecoCompleto = String.format("%s, %s, %s, %s",
                    endereco.getLogradouro(),
                    endereco.getBairro(),
                    endereco.getLocalidade(),
                    endereco.getUf());

            // Busca as coordenadas do endereço utilizando a chave da API
            Endereco enderecoCoordenadas = CepService.buscarCoordenadas(enderecoCompleto,
                    config.getGoogleApiKey()); // Utilizando a chave da variável de ambiente

            // Se as coordenadas forem encontradas, adiciona ao objeto de Endereco
            if (enderecoCoordenadas != null) {
                endereco.setLatitude(enderecoCoordenadas.getLatitude());
                endereco.setLongitude(enderecoCoordenadas.getLongitude());
            }

            // Retorna o endereço com todos os dados, incluindo coordenadas
            return ResponseEntity.ok(endereco);
        } catch (IOException e) {
            // Se ocorrer um erro de IO, retorna HTTP 500
            return ResponseEntity.status(500).build();
        }
    }
}