package com.example.bank.controller;

import com.example.bank.model.dto.BancoDTO;
import com.example.bank.model.entity.Banco;
import com.example.bank.service.BancoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
@CrossOrigin("*")
@RestController
@RequestMapping("/banco")
public class BancoController {

    @Autowired
    private BancoService bancoService;


    @GetMapping("/{id}")
    public ResponseEntity<?> getBancoById(@PathVariable Long id) {
        Optional<Banco> bancoOp=bancoService.getBancoById(id);
        if (bancoOp.isPresent()) {
            return new ResponseEntity<>(convertir_A_Dto(bancoOp.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se encontro la info",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> infoBanco() {
        try {
            Banco banco = bancoService.getBancoById(1L).get();
            Map<String,String> map=new LinkedHashMap<>();
            map.put("id",banco.getId().toString());
            map.put("nombre",banco.getNombre());
            map.put("pais",banco.getPais());

            return new ResponseEntity<>(map,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("No se encontro la info",HttpStatus.NOT_FOUND);
        }

    }

    /*
    @PostMapping
    public BancoDTO createBanco(@RequestBody BancoDTO bancoDTO) {
        Banco banco = convertir_A_Entity(bancoDTO);
        return convertir_A_Dto(bancoService.saveBanco(banco));
    }

     */

    @PutMapping("/{id}")
    public BancoDTO updateBanco(@PathVariable Long id, @RequestBody BancoDTO bancoDTO) {
        Banco banco = convertir_A_Entity(bancoDTO);
        banco.setId(id);
        return convertir_A_Dto(bancoService.saveBanco(banco));
    }
/*
    @DeleteMapping("/{id}")
    public void deleteBanco(@PathVariable Long id) {
        bancoService.deleteBanco(id);
    }


 */
    private BancoDTO convertir_A_Dto(Banco banco) {
        BancoDTO bancoDTO = BancoDTO.builder().
                id(banco.getId()).
                nombre(banco.getNombre()).
                pais(banco.getPais()).
                build();
        return bancoDTO;
    }

    private Banco convertir_A_Entity(BancoDTO bancoDTO) {
        Banco banco = Banco.builder()
                .nombre(bancoDTO.getNombre())
                .pais(bancoDTO.getPais())
                .build();
        return banco;
    }
}