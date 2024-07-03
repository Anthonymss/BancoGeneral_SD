package com.example.bank.model.dto;

import com.example.bank.util.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TransaccionDTO {
    private Long id;
    private TipoTransaccion tipo;
    private BigDecimal monto;
    private Date fecha;
    private String cuentaOrigenNumero;
    private String cuentaDestinoNumero;
    private String origenBanco;
    private String destinoBanco;
    private Instant requestTime;
    private Instant responseTime;
}
