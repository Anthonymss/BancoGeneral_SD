package com.example.bank.controller;


import com.example.bank.model.dto.ClienteDTO;
import com.example.bank.model.entity.Cliente;
import com.example.bank.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<ClienteDTO> getAllClientes() {
        return clienteService.getAllClientes().stream().map(this::convertirADto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        Optional<Cliente> clienteOp=clienteService.getClienteById(id);
        if (clienteOp.isPresent()) {
            return ResponseEntity.ok(convertirADto(clienteOp.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Registrarte
    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            Cliente cliente = convertirAEntity(clienteDTO);
            clienteService.saveCliente(cliente);
            return new ResponseEntity<>("Usuario Resgistrado Correctamente", HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>("No se puedo registrar al Usuario", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        if(clienteService.getClienteById(id).isEmpty()) {
            return new ResponseEntity<>("No se puedo actualizar al Usuario ->no se encontro", HttpStatus.BAD_REQUEST);
        }
        try {
            Cliente cliente = convertirAEntity(clienteDTO);
            cliente.setId(id);
            clienteService.saveCliente(cliente);
            return new ResponseEntity<>("Usuario Actualizado Correctamente", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("No se puedo actualizar al Usuario", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>  deleteCliente(@PathVariable Long id) {
            Optional<Cliente> clienteop = clienteService.getClienteById(id);
            if (clienteop.isPresent()){
                clienteService.deleteCliente(id);
                return new ResponseEntity<>("Usuario Eliminado Correctamente", HttpStatus.OK);
            }else {
                return new ResponseEntity<>("No se puedo eliminar al Usuario ->no se encontro", HttpStatus.BAD_REQUEST);
            }
    }

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
}