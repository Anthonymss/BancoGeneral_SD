package com.example.bank.service;

import com.example.bank.model.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<Cliente> getAllClientes();
    Optional<Cliente> getClienteById(Long id);
    Cliente saveCliente(Cliente cliente);
    void deleteCliente(Long id);
    Optional<Cliente> findClienteByDni(String dni);
}