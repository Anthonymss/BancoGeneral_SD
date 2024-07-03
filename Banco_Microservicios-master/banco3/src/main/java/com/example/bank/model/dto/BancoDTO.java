package com.example.bank.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BancoDTO {
    private Long id;
    private String nombre;
    private String pais;
}