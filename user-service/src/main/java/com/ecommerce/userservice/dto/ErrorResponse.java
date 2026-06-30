package com.ecommerce.userservice.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa a resposta de erro, incluindo código, mensagem, tipo de erro, timestamp e caminho da requisição.")
public class ErrorResponse {

    @Schema(description = "Código de status HTTP do erro", example = "404")
    private int code;

    @Schema(description = "Mensagem detalhada do erro", example = "Usuário não encontrado")
    private String message;

    @Schema(description = "Tipo de erro", example = "Not Found")
    private String error;

    @Schema(description = "Timestamp do erro no formato UTC", example = "2023-10-01 12:34:56")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime timestamp;

    @Schema(description = "Caminho da requisição que gerou o erro", example = "/natural-persons/register")
    private String path;
    
    public ErrorResponse() {
    }

    public ErrorResponse(int code, String message, String error, LocalDateTime timestamp, String path) {
        this.code = code;
        this.message = message;
        this.error = error;
        this.timestamp = timestamp;
        this.path = path;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    
}
