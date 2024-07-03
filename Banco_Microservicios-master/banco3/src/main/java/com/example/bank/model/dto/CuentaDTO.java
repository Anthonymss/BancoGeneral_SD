package com.example.bank.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CuentaDTO {
    private Long id;
    private String numeroCuenta;
    private BigDecimal saldo;
    private Long clienteId;
    private Long bancoId;
    private String password;
    private String tipocuenta;
}
