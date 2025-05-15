package br.edu.ifmg.hotelBao.exceptions;

public class ResourceNotFoud extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoud(String message) {
        super(message);
    }

    public ResourceNotFoud() {
        super();
    }
}

