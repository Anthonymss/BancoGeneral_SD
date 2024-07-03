package com.example.bank.service.impl;

import com.example.bank.model.entity.Cuenta;
import com.example.bank.repository.ClienteRepository;
import com.example.bank.repository.CuentaRepository;
import com.example.bank.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaServiceImpl implements CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cuenta> getAllCuentas() {
        return cuentaRepository.findAll();
    }

    @Override
    public Cuenta getCuentaById(Long id) {
        return cuentaRepository.findById(id).orElse(null);
    }

    @Override
    public Cuenta saveCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    @Override
    public void deleteCuenta(Long id) {
        cuentaRepository.deleteById(id);
    }
    @Override
    public List<Cuenta> getCuentasByClienteDni(String dni) {
        return cuentaRepository.findByClienteDni(dni);
    }
    @Override
    public Optional<Cuenta> getCuentaByNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta);
    }

    @Override
    public Optional<Cuenta> login(String numeroCuenta, String password) {
        return cuentaRepository.findByNumeroCuentaAndPassword(numeroCuenta, password);
    }

    @Override
    public Optional<Cuenta> loginByDni(String dni , String password) {
        return cuentaRepository.findByDniAndPassword(dni, password);
    }

}
