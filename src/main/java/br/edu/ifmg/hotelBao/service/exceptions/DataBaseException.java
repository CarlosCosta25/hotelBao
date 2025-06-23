package br.edu.ifmg.hotelBao.service.exceptions;

public class DataBaseException extends RuntimeException {
    public DataBaseException(String msg){
        super(msg);
    }

    public DataBaseException(){
        super();
    }

    public DataBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
