package com.dasad.empresa.controller;

import com.dasad.empresa.api.ChatApi;
import com.dasad.empresa.model.ChatRequestDTO;
import com.dasad.empresa.model.ChatResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"/chat"})
@CrossOrigin(
        origins = {"http://localhost:4200", "http://localhost:8080", "http://localhost:8100"}
)
@Log4j2
public class ChatController implements ChatApi {

    @Value("${llama.url}")
    private String llamaUrl;

    @Autowired
    private RestTemplate restTemplate;

    public ChatController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('Administrador', 'Moderador', 'Usuário')")
    public ResponseEntity<ChatResponseDTO> generate(@RequestBody ChatRequestDTO chatRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Montar o corpo da requisição
        Map<String, Object> body = new HashMap<>();
        body.put("model", chatRequestDTO.getModel());
        body.put("prompt", chatRequestDTO.getPrompt());
        body.put("stream", false);

        // Criar a requisição
        HttpEntity<Map<String, Object>> httpRequest = new HttpEntity<>(body, headers);

        try {
            // Fazer a chamada ao serviço Ollama
            ResponseEntity<Map> response = restTemplate.exchange(
                    llamaUrl,
                    HttpMethod.POST,
                    httpRequest,
                    Map.class
            );

            log.info("Resposta do Llama: {}", response);

            // Verificar se a resposta é nula
            if (response.getBody() == null) {
                ChatResponseDTO errorResponse = new ChatResponseDTO();
                errorResponse.setResponse("Resposta do Llama é nula");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }

            // Montar a resposta
            var resposta = response.getBody();
            log.info("Resposta do Llama: {}", resposta);

            ChatResponseDTO chatResponseDTO = new ChatResponseDTO();
            chatResponseDTO.setResponse(resposta.get("response").toString());
            chatResponseDTO.setDone((Boolean) resposta.get("done"));
            chatResponseDTO.setCreatedAt(resposta.get("created_at").toString());
            chatResponseDTO.setModel(resposta.get("model").toString());
            return ResponseEntity.ok(chatResponseDTO);

        } catch (RestClientException e) {
            log.error("Erro ao chamar o serviço Llama", e);
            ChatResponseDTO errorResponse = new ChatResponseDTO();
            errorResponse.setResponse("Error processing response from Llama");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}