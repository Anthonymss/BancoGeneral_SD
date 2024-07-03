package com.example.bank.repository;

import com.example.bank.model.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findByClienteDni(String dni);
    Optional<Cuenta> findByNumeroCuentaAndPassword(String numeroCuenta, String password);
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    @Query("SELECT c FROM Cuenta c WHERE c.cliente.dni = :dni AND c.password = :password")
    Optional<Cuenta> findByDniAndPassword(@Param("dni") String dni, @Param("password") String password);
}
