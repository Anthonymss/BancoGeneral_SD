package com.example.bank.controller;


import com.example.bank.model.dto.CuentaDTO;
import com.example.bank.model.entity.Cliente;
import com.example.bank.model.entity.Cuenta;
import com.example.bank.service.BancoService;
import com.example.bank.service.ClienteService;
import com.example.bank.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
@CrossOrigin("*")
@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private BancoService bancoService;
    @GetMapping
    public List<CuentaDTO> getAllCuentas() {
        return cuentaService.getAllCuentas().stream().map(this::convertirADto).collect(Collectors.toList());
    }

    @GetMapping("/{Ncuenta}")
    public ResponseEntity<?> getCuentaById(@PathVariable String Ncuenta) {
        Optional<Cuenta> cuentaOp=cuentaService.getCuentaByNumeroCuenta(Ncuenta);
        if(cuentaOp.isPresent()){
                Map<String,String> datos=new LinkedHashMap<>();
                datos.put("id",cuentaOp.get().getId().toString());
                datos.put("numeroCuenta",cuentaOp.get().getNumeroCuenta());
                datos.put("saldo",cuentaOp.get().getSaldo().toString());
                datos.put("ID",cuentaOp.get().getCliente().getId().toString());
                datos.put("Nombre",cuentaOp.get().getCliente().getNombre());
                datos.put("Apellido",cuentaOp.get().getCliente().getApellido());
                datos.put("Dni",cuentaOp.get().getCliente().getDni());
                datos.put("Email",cuentaOp.get().getCliente().getEmail());
                datos.put("Telefono",cuentaOp.get().getCliente().getTelefono());

                return new ResponseEntity<>(datos,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/infocuentas/{dni}")
    public ResponseEntity<?> getCuentasByDNI(@PathVariable String dni) {
        Optional<Cliente> optionalCliente = clienteService.findClienteByDni(dni);

        if (!optionalCliente.isPresent()) {
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }

        Cliente cliente = optionalCliente.get();
        List<Cuenta> cuentas = cuentaService.getAllCuentas().stream()
                .filter(c -> c.getCliente().getId().equals(cliente.getId()))
                .collect(Collectors.toList());

        List<Map<String, String>> datosList = new ArrayList<>();

        for (Cuenta c : cuentas) {
            Map<String, String> datos = new LinkedHashMap<>();
            datos.put("tipo_cuenta", c.getTipoCuenta());
            datos.put("id", c.getId().toString());
            datos.put("numeroCuenta", c.getNumeroCuenta());
            datos.put("saldo", c.getSaldo().toString());
            datosList.add(datos);
        }

        return new ResponseEntity<>(datosList, HttpStatus.OK);
    }


    @PutMapping("/{Ncuenta}")
    public ResponseEntity<?> updateCuenta(@PathVariable String Ncuenta, @RequestBody CuentaDTO cuentaDTO) {
        Optional<Cuenta> cuentaOp=cuentaService.getCuentaByNumeroCuenta(Ncuenta);
        if(cuentaOp.isPresent()){

            Cuenta cuenta = convertirAEntity(cuentaDTO);
            cuenta.setId(cuentaOp.get().getId());
            cuentaService.saveCuenta(cuenta);
            return new ResponseEntity<>("Actualizado Correctamente",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("No se encontro y otro error",HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{Ncuenta}")
    public ResponseEntity<?> deleteCuenta(@PathVariable String Ncuenta) {
        Optional<Cuenta> cuentaop=cuentaService.getCuentaByNumeroCuenta(Ncuenta);
        if(cuentaop.isPresent()){
            cuentaService.deleteCuenta(cuentaop.get().getId());
            return new ResponseEntity<>("Eliminado Correctamente",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("No se pudo eliminar",HttpStatus.NOT_FOUND);
        }
    }

    private CuentaDTO convertirADto(Cuenta cuenta) {
        return CuentaDTO.builder()
                .id(cuenta.getId())
                .numeroCuenta(cuenta.getNumeroCuenta())
                .saldo(cuenta.getSaldo())
                .clienteId(cuenta.getCliente().getId())
                .bancoId(cuenta.getBanco().getId())
                .build();
    }

    private Cuenta convertirAEntity(CuentaDTO cuentaDTO) {
        return Cuenta.builder()
                .numeroCuenta(cuentaDTO.getNumeroCuenta())
                .saldo(cuentaDTO.getSaldo())
                .password(cuentaDTO.getPassword())
                .cliente(clienteService.getClienteById(cuentaDTO.getClienteId()).get())
                .banco(bancoService.getBancoById(cuentaDTO.getBancoId()).get())
                .build();
    }
}