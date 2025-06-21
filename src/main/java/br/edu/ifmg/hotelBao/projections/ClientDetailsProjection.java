package br.edu.ifmg.hotelBao.projections;


public interface ClientDetailsProjection {
    String getUsername();
    String getPassword();
    Long getRoleId();
    String getAuthority();
}

