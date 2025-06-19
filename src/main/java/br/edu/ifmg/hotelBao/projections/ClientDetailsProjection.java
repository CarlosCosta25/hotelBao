package br.edu.ifmg.hotelBao.projections;

public interface ClientDetailsProjection {
    String getLogin();
    String getPassword();
    Long getRoleId();
    String getAuthority();
}
