package br.edu.ifmg.hotelBao.projections;

import java.math.BigDecimal;

public interface StayReportProjection {
    Long getClientId();
    String getClientName();
    BigDecimal getTotalValue();
    Long getStayCount();
}