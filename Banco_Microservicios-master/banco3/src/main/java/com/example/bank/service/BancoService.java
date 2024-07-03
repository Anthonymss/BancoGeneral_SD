package com.example.bank.service;

import com.example.bank.model.entity.Banco;
import java.util.List;
import java.util.Optional;

public interface BancoService {
    List<Banco> getAllBancos();
    Optional<Banco> getBancoById(Long id);
    Banco saveBanco(Banco banco);
    void deleteBanco(Long id);
}