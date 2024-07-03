package com.example.bank.service.impl;

import com.example.bank.model.entity.Cuenta;
import com.example.bank.model.entity.TransaccionExterna;
import com.example.bank.repository.CuentaRepository;
import com.example.bank.repository.TransaccionExternaRepository;
import com.example.bank.service.TransaccionExternaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TransaccionExternaServiceImpl implements TransaccionExternaService {
    @Autowired
    private TransaccionExternaRepository transaccionExternaRepository;
    @Autowired
    private CuentaRepository cuentaRepository;

    @Override
    public List<TransaccionExterna> getAllTransacciones() {
        return transaccionExternaRepository.findAll();
    }

    @Override
    public Optional<TransaccionExterna> getTransaccionById(Long id) {
        return transaccionExternaRepository.findById(id);
    }

    @Override
    public TransaccionExterna saveTransaccion(TransaccionExterna transaccionExterna) {
        return transaccionExternaRepository.save(transaccionExterna);
    }

    @Override
    public void deleteTransaccion(Long id) {
        transaccionExternaRepository.deleteById(id);
    }

    @Override
    public List<TransaccionExterna> obtenerTransaccionesPorCuenta(String Ncuenta) {
        Optional<Cuenta> cuentaOptional = cuentaRepository.findByNumeroCuenta(Ncuenta);
        if (cuentaOptional.isEmpty()) {
            throw new RuntimeException("Cuenta no encontrada: " + Ncuenta);
        }
        return transaccionExternaRepository.findAll().stream()
                .filter(transaccion -> transaccion.getCuentaOrigen().equals(Ncuenta) ||
                        transaccion.getCuentaDestino().equals(Ncuenta))
                .collect(Collectors.toList());
    }
}
