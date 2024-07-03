package com.example.bank.service;

import com.example.bank.model.entity.Transaccion;

import java.util.List;

public interface TransaccionService {
    List<Transaccion> getAllTransacciones();
    Transaccion getTransaccionById(Long id);
    Transaccion saveTransaccion(Transaccion transaccion);
    void deleteTransaccion(Long id);
    public List<Transaccion> obtenerTransaccionesPorCuenta(Long cuentaId);
}