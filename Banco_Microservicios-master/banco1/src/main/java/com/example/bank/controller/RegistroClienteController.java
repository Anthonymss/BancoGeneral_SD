package com.example.bank.controller;


import com.example.bank.model.dto.BancoDTO;
import com.example.bank.model.dto.ClienteDTO;
import com.example.bank.model.dto.CuentaDTO;
import com.example.bank.model.entity.RegistroCuentaCliente;
import com.example.bank.model.entity.Banco;
import com.example.bank.model.entity.Cliente;
import com.example.bank.model.entity.Cuenta;
import com.example.bank.service.BancoService;
import com.example.bank.service.ClienteService;
import com.example.bank.service.CuentaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
@CrossOrigin("*")
@RestController
@RequestMapping("/Register")
public class RegistroClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private BancoService bancoService;

    @PostMapping("/firstRegister")
    @Transactional
    public ResponseEntity<?> FirstRegister(@RequestBody RegistroCuentaCliente registroCuentaCliente) {
        Cuenta cuenta=null;
       try {
           Optional<Banco> bancoOp =bancoService.getBancoById(registroCuentaCliente.getIdBanco());
           if (bancoOp.isEmpty()) {
               return new ResponseEntity<>("El banco no existe", HttpStatus.BAD_REQUEST);
           }
           Banco banco = bancoOp.get();
           Cliente cliente = Cliente.builder()
                   .nombre(registroCuentaCliente.getNombre())
                   .apellido(registroCuentaCliente.getApellido())
                   .email(registroCuentaCliente.getEmail())
                   .telefono(registroCuentaCliente.getTelefono())
                   .dni(registroCuentaCliente.getDni())
                   .build();
           clienteService.saveCliente(cliente);

           cuenta = Cuenta.builder()
                   .numeroCuenta(generarNumeroCuentaUnico(registroCuentaCliente.getIdBanco()))
                   .saldo(new BigDecimal("0.0"))
                   .cliente(cliente)
                   .banco(banco)
                   .tipoCuenta("CUENTA_CORRIENTE")
                   .password(registroCuentaCliente.getPassword())
                   .build();

           cuentaService.saveCuenta(cuenta);

           //ClienteDTO responseDTO = convertirADto(cliente);
           return new ResponseEntity<>("Cuenta creada correctamente\nSu numero de cuenta: "+cuenta.getNumeroCuenta()+"\nNO LO COMPARTA CON NADIE!!!", HttpStatus.CREATED);
       }catch (Exception e) {
           return new ResponseEntity<>("Se produjo un error -> DNI and EMAIL son UNICOS!!!",HttpStatus.BAD_REQUEST);
       }
    }

    @PostMapping("/crearCuenta")
    @Transactional
    public ResponseEntity<?> crearCuenta(@RequestBody CuentaDTO cuentaDTO) {
        try {
            Cuenta cuenta=null;
            try {
                String ncuenta=generarNumeroCuentaUnico(1L);
                Cliente c=clienteService.getClienteById(cuentaDTO.getClienteId()).get();
                cuenta= Cuenta.builder()
                        .banco(bancoService.getBancoById(1L).get())
                        .cliente(c)
                        .tipoCuenta(cuentaDTO.getTipocuenta())
                        .saldo(new BigDecimal("0.00"))
                        .password(cuentaDTO.getPassword())
                        .numeroCuenta( ncuenta)
                        .build();
                cuentaService.saveCuenta(cuenta);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new  ResponseEntity<>("Cuenta creada correctamente->Su numero de cuenta: \n"+cuenta.getNumeroCuenta(), HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/crearCliente")
    public ClienteDTO createCliente(@RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = convertirAEntity(clienteDTO);
        return convertirADto(clienteService.saveCliente(cliente));
    }


    //metodos privados...
    private ClienteDTO convertirADto(Cliente cliente) {
        return ClienteDTO.builder()
                .dni(cliente.getDni())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .build();
    }

    private Cliente convertirAEntity(ClienteDTO clienteDTO) {
        return Cliente.builder()
                .dni(clienteDTO.getDni())
                .nombre(clienteDTO.getNombre())
                .apellido(clienteDTO.getApellido())
                .email(clienteDTO.getEmail())
                .telefono(clienteDTO.getTelefono())
                .build();
    }
    private String generarNumeroCuentaUnico(Long idBanco) {
        String numeroAleatorio = UUID.randomUUID().toString().substring(0, 8);
        return "E"+idBanco.toString() + numeroAleatorio;
    }
    private BancoDTO convertir_A_Dto(Banco banco) {
        BancoDTO bancoDTO = BancoDTO.builder().
                id(banco.getId()).
                nombre(banco.getNombre()).
                pais(banco.getPais()).
                build();
        return bancoDTO;
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
