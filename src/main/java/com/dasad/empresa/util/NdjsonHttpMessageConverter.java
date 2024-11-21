package com.dasad.empresa.util;

import com.dasad.empresa.model.ChatResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NdjsonHttpMessageConverter extends AbstractHttpMessageConverter<List<ChatResponseDTO>> {

    private final ObjectMapper objectMapper;

    public NdjsonHttpMessageConverter(ObjectMapper objectMapper) {
        super(new MediaType("application", "x-ndjson"));
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    protected List<ChatResponseDTO> readInternal(Class<? extends List<ChatResponseDTO>> clazz, HttpInputMessage inputMessage) throws IOException {
        List<ChatResponseDTO> responses = new ArrayList<>();
        String[] lines = new String(inputMessage.getBody().readAllBytes()).split("\n");
        for (String line : lines) {
            responses.add(objectMapper.readValue(line, ChatResponseDTO.class));
        }
        return responses;
    }

    @Override
    protected void writeInternal(List<ChatResponseDTO> chatResponseDTOS, HttpOutputMessage outputMessage) {
        throw new UnsupportedOperationException("Not implemented");
    }
}