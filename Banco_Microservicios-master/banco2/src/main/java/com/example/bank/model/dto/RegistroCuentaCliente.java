package com.example.bank.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistroCuentaCliente {
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Long idBanco;
    private String password;
}
