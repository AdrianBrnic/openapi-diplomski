package hr.fer.openapidemo.exception;

import hr.fer.openapidemo.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex,
                                                         HttpServletRequest request) {
        String message = ex.getMessage();
        HttpStatus status;

        if (message != null && message.contains("nije pronađen")) {
            status = HttpStatus.NOT_FOUND;
        } else if (message != null && message.contains("već postoji")) {
            status = HttpStatus.CONFLICT;
        } else if (message != null && message.contains("Nedovoljno zaliha")) {
            status = HttpStatus.UNPROCESSABLE_ENTITY;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(buildError(status, message, request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                           HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Neispravan zahtjev");

        return ResponseEntity.badRequest().body(
                buildError(HttpStatus.BAD_REQUEST, message, request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                                                                HttpServletRequest request) {
        return ResponseEntity.badRequest().body(
                buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    private ErrorResponse buildError(HttpStatus status, String message,
                                      HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(OffsetDateTime.now());
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(message);
        error.setPath(request.getRequestURI());
        return error;
    }
}
