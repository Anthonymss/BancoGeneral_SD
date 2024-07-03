package com.example.bank.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Auth {
    private String numeroCuenta;
    private String dni;
    private String password;
}
