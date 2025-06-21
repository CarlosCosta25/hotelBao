package br.edu.ifmg.hotelBao.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CheckinException extends RuntimeException {
    public CheckinException(String message) {
        super(message);
    }
}