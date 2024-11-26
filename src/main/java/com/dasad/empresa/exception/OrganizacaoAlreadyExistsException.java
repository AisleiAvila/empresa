package com.dasad.empresa.exception;

public class OrganizacaoAlreadyExistsException extends RuntimeException {
    public OrganizacaoAlreadyExistsException(String message) {
        super(message);
    }
}