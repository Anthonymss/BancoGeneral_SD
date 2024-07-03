package com.example.bank.controller;


import com.example.bank.model.entity.Auth;
import com.example.bank.model.entity.Cuenta;
import com.example.bank.service.BancoService;
import com.example.bank.service.ClienteService;
import com.example.bank.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/Login")

public class LoginController {
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private BancoService bancoService;

    @PostMapping()
    public ResponseEntity<?> login(@RequestBody Auth auth) {
        if(auth==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Envio de datos Incorrecto");
        }
        if(auth.getNumeroCuenta()!=null){
            Optional<Cuenta> cuenta = cuentaService.login(auth.getNumeroCuenta(), auth.getPassword());
            if(cuenta.isPresent()){
                Map<String,String> datos=new LinkedHashMap<>();
                datos.put("numeroCuenta",cuenta.get().getNumeroCuenta());
                datos.put("nombre",cuenta.get().getCliente().getNombre());
                datos.put("apellido",cuenta.get().getCliente().getApellido());
                datos.put("dni",cuenta.get().getCliente().getDni());
                datos.put("email",cuenta.get().getCliente().getEmail());
                datos.put("telefono",cuenta.get().getCliente().getTelefono());
                //datos.put("Banco",cuenta.get().getBanco().getNombre());
                return new ResponseEntity<>(datos,HttpStatus.OK);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Numero de cuenta o contraseña incorrectos");

        }else {
            Optional<Cuenta> cuenta=cuentaService.loginByDni(auth.getDni(),auth.getPassword());
            if(cuenta.isPresent()){
                Map<String,String> datos=new LinkedHashMap<>();
                datos.put("numeroCuenta",cuenta.get().getNumeroCuenta());
                datos.put("nombre",cuenta.get().getCliente().getNombre());
                datos.put("apellido",cuenta.get().getCliente().getApellido());
                datos.put("dni",cuenta.get().getCliente().getDni());
                datos.put("email",cuenta.get().getCliente().getEmail());
                datos.put("telefono",cuenta.get().getCliente().getTelefono());
                //datos.put("Banco",cuenta.get().getBanco().getNombre());
                return new ResponseEntity<>(datos,HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Envio de datos Incorrecto");
        }

    }

}
