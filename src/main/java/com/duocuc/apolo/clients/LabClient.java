package com.duocuc.apolo.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.duocuc.apolo.dto.PacientCreateRequest;

import reactor.core.publisher.Mono;



@Component
public class LabClient {

    private final WebClient webClient;
    @Value("${lab.service.url}")
    private String labServiceUrl;
    
    public LabClient(WebClient.Builder builder) {
        // Ajusta el puerto al real del microservicio LAB
        this.webClient = builder.baseUrl(labServiceUrl).build();
    }

    public void createPacient(PacientCreateRequest request) {
        webClient.post()
            .uri("/pacients") // ahora sí
            .bodyValue(request)
            .retrieve()
            .toBodilessEntity()          // <-- evita problemas con el body
            .onErrorResume(e -> {
                System.out.println("Error creando paciente en LAB: " + e.getMessage());
                return Mono.empty();      // no revienta el flujo
            })
            .block();
    }
}
