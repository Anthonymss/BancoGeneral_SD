package com.example.bank.service;

import com.example.bank.model.entity.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaService {
    List<Cuenta> getAllCuentas();
    Cuenta getCuentaById(Long id);
    Cuenta saveCuenta(Cuenta cuenta);
    void deleteCuenta(Long id);
    public List<Cuenta> getCuentasByClienteDni(String dni);
    public Optional<Cuenta> getCuentaByNumeroCuenta(String numeroCuenta);
    public Optional<Cuenta> login(String numeroCuenta, String password);
    public Optional<Cuenta> loginByDni(String dni, String password);
}