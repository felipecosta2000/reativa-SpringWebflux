package com.felipe.costa.webflux.controller.exceptions;


import com.felipe.costa.webflux.service.exception.ObjectNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;



@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<Mono<StandardError>> duplicateKeyException(

            DuplicateKeyException ex, ServerHttpRequest request

    ) {

        return ResponseEntity.badRequest()

                .body(Mono.just(

                        StandardError.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .message(verifyDupKey(ex.getMessage()))
                                .path(request.getPath().toString())
                                .build()

                        ));

    }

    @ExceptionHandler(WebExchangeBindException.class)

    public ResponseEntity<Mono<ValidationError>> validationError(

            WebExchangeBindException ex, ServerHttpRequest request

    ){

        ValidationError error = new ValidationError(
                LocalDateTime.now(), request.getPath().toString(),
                HttpStatus.BAD_REQUEST.value(), "Validation error", "Erro on validation attributes"

        );

        for(FieldError x : ex.getBindingResult().getFieldErrors()){
            error.addError(x.getField(), x.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(Mono.just(error));

    }

    @ExceptionHandler(ObjectNotFoundException.class)
    ResponseEntity<Mono<StandardError>> objectNotFoundException(

            ObjectNotFoundException ex, ServerHttpRequest request

    ) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)

                .body(Mono.just(

                        StandardError.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.NOT_FOUND.value())
                                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .message(ex.getMessage())
                                .path(request.getPath().toString())
                                .build()

                ));

    }

    private String verifyDupKey(String message) {
        if(message.contains("email dup key")) {
            return "E-mail ja registrado";
        }
        return "Dup key exception";
    }


}
