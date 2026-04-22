package hr.fer.openapidemo.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Format greške")
public class ErrorResponse {

    @Schema(description = "HTTP status kod", example = "404")
    private Integer status;

    @Schema(description = "Poruka greške", example = "Korisnik nije pronađen")
    private String message;

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}