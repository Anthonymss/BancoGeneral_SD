package com.example.bank.service.impl;


import com.example.bank.model.entity.Banco;
import com.example.bank.repository.BancoRepository;
import com.example.bank.service.BancoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BancoServiceImpl implements BancoService {

    @Autowired
    private BancoRepository bancoRepository;

    @Override
    public List<Banco> getAllBancos() {
        return bancoRepository.findAll();
    }

    @Override
    public Optional<Banco> getBancoById(Long id) {
        return bancoRepository.findById(id);
    }

    @Override
    public Banco saveBanco(Banco banco) {
        return bancoRepository.save(banco);
    }

    @Override
    public void deleteBanco(Long id) {
        bancoRepository.deleteById(id);
    }
}