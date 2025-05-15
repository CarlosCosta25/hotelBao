package br.edu.ifmg.hotelBao.exceptions;

public class DataBaseException extends RuntimeException {
    public DataBaseException(String msg){
        super(msg);
    }

    public DataBaseException(){
        super();
    }
}
