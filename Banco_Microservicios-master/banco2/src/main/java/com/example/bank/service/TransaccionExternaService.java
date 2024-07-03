package com.example.bank.service;

import com.example.bank.model.entity.Cliente;
import com.example.bank.model.entity.TransaccionExterna;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface TransaccionExternaService {
    List<TransaccionExterna> getAllTransacciones();
    Optional<TransaccionExterna> getTransaccionById(Long id);
    TransaccionExterna saveTransaccion(TransaccionExterna transaccionExterna);
    void deleteTransaccion(Long id);
    public List<TransaccionExterna> obtenerTransaccionesPorCuenta(String Ncuenta);
}
