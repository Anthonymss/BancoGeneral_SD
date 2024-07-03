package com.example.bank.service.impl;


import com.example.bank.model.entity.Transaccion;
import com.example.bank.repository.TransaccionRepository;
import com.example.bank.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Override
    public List<Transaccion> getAllTransacciones() {
        return transaccionRepository.findAll();
    }

    @Override
    public Transaccion getTransaccionById(Long id) {
        return transaccionRepository.findById(id).orElse(null);
    }

    @Override
    public Transaccion saveTransaccion(Transaccion transaccion) {
        return transaccionRepository.save(transaccion);
    }

    @Override
    public void deleteTransaccion(Long id) {
        transaccionRepository.deleteById(id);
    }

    @Override
    public List<Transaccion> obtenerTransaccionesPorCuenta(Long cuentaId) {
        return transaccionRepository.findByCuentaOrigenIdOrCuentaDestinoId(cuentaId, cuentaId);
    }
}
