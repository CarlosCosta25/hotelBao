package br.edu.ifmg.hotelBao.resources.exceptions;

import br.edu.ifmg.hotelBao.exceptions.DataBaseException;
import br.edu.ifmg.hotelBao.exceptions.ResourceNotFoud;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
@ControllerAdvice
public class ResourceExceptionListener {

    @ExceptionHandler(ResourceNotFoud.class)
    public ResponseEntity<StandartError> resorceNotFound(ResourceNotFoud ex, HttpServletRequest request){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new StandartError(
                        Instant.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "Resource not found",
                        request.getRequestURI(),
                        ex.getMessage()
                ));

    }
    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandartError> dataBaseExcepion(DataBaseException ex, HttpServletRequest request){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new StandartError(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "database exception",
                        request.getRequestURI(),
                        ex.getMessage()
                ));

    }

}