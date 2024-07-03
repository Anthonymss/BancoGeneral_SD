package com.example.bank.controller;

import com.example.bank.model.dto.TransaccionDTO;
import com.example.bank.model.entity.Cuenta;
import com.example.bank.model.entity.Transaccion;
import com.example.bank.model.entity.TransaccionExterna;
import com.example.bank.service.ClienteService;
import com.example.bank.service.CuentaService;
import com.example.bank.service.TransaccionExternaService;
import com.example.bank.service.TransaccionService;
import com.example.bank.util.TipoTransaccion;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@CrossOrigin("*")
@RestController
@RequestMapping("/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private CuentaService cuentaService;
    @Autowired
    private TransaccionExternaService transaccionExternaService;


    @GetMapping("/datos/{Ncuenta}")
    public ResponseEntity<?> getAllTransacciones(@PathVariable String Ncuenta) {
        Optional<Cuenta> cuentaOp=cuentaService.getCuentaByNumeroCuenta(Ncuenta);
        if(cuentaOp.isPresent()){
            Cuenta cuenta=cuentaOp.get();
            try {
                List<Transaccion> transacciones = transaccionService.obtenerTransaccionesPorCuenta(cuenta.getId());
                List<TransaccionExterna> transacciones2 = transaccionExternaService.obtenerTransaccionesPorCuenta(Ncuenta);
                List<TransaccionDTO> transaccionesDTO = transacciones.stream()
                        .map(this::convertirADTO)
                        .collect(Collectors.toList());
                transaccionesDTO.addAll(transacciones2.stream()
                                   .map(this::convertirADTOExterna)
                                   .collect(Collectors.toList()));
                    return new ResponseEntity<>(transaccionesDTO, HttpStatus.OK);
            }catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }else{
            return new ResponseEntity<>("Numero de cuenta Incorrecta",HttpStatus.NOT_FOUND);
        }
    }



    //sin uso aun
    @GetMapping
    public List<TransaccionDTO> getAllTransacciones() {
        return transaccionService.getAllTransacciones().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TransaccionDTO getTransaccionById(@PathVariable Long id) {
        Transaccion transaccion = transaccionService.getTransaccionById(id);
        return convertirADTO(transaccion);
    }
    @GetMapping("/listaByUser/{id}")
    public ResponseEntity<?> getAllTransaccionById(@PathVariable Long id) {
        Transaccion transaccion = transaccionService.getTransaccionById(id);
        return null;
    }


    @PostMapping("/operacion")
    @Transactional
    public ResponseEntity<?> createTransaccion(@RequestBody TransaccionDTO transaccionDTO) {
        try {
                Transaccion transaccion = convertirAEntity(transaccionDTO);
                Optional<Cuenta> cuentaOp = cuentaService.getCuentaByNumeroCuenta(transaccionDTO.getCuentaOrigenNumero());
                Cuenta cuentaOrigen = cuentaOp.get();
                Optional<Cuenta> cuentaOp2 = cuentaService.getCuentaByNumeroCuenta(transaccionDTO.getCuentaDestinoNumero());
                Cuenta cuentaDestino = cuentaOp2.get();
                BigDecimal monto = transaccionDTO.getMonto();


                switch (transaccionDTO.getTipo()) {
                    case TipoTransaccion.DEPOSITO:
                        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().add(monto));
                        transaccion.setCuentaDestino(cuentaOrigen);
                        transaccion.setBancoOrigen(cuentaOrigen.getBanco());
                        transaccion.setBancoDestino(cuentaOrigen.getBanco());
                        transaccion.asignarDeposito();
                        cuentaService.saveCuenta(cuentaOrigen);
                        transaccionService.saveTransaccion(transaccion);
                        break;

                    case TipoTransaccion.RETIRO:
                        if (cuentaOrigen.getSaldo().compareTo(monto) < 0) {
                            return new ResponseEntity<>("Saldo insuficiente", HttpStatus.BAD_REQUEST);
                        }
                        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
                        transaccion.setCuentaDestino(cuentaOrigen);
                        transaccion.setBancoOrigen(cuentaOrigen.getBanco());
                        transaccion.setBancoDestino(cuentaOrigen.getBanco());
                        transaccion.asignarRetiro();
                        cuentaService.saveCuenta(cuentaOrigen);
                        transaccionService.saveTransaccion(transaccion);
                        break;
                    case TipoTransaccion.TRANSFERENCIA:
                        if (cuentaOrigen.getSaldo().compareTo(monto) < 0) {
                            return new ResponseEntity<>("Saldo insuficiente", HttpStatus.BAD_REQUEST);
                        }
                        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
                        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));
                        transaccion.setCuentaOrigen(cuentaOrigen);
                        transaccion.setCuentaDestino(cuentaDestino);
                        transaccion.setBancoOrigen(cuentaOrigen.getBanco());
                        transaccion.setBancoDestino(cuentaDestino.getBanco());
                        cuentaService.saveCuenta(cuentaOrigen);
                        cuentaService.saveCuenta(cuentaDestino);
                        transaccionService.saveTransaccion(transaccion);

                        break;
                    default:
                        return new ResponseEntity<>("Operacion No soportada", HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(convertirADTO(transaccionService.saveTransaccion(transaccion)), HttpStatus.CREATED);

        }catch (Exception e){
            return new ResponseEntity<>("Se produjo un error", HttpStatus.BAD_REQUEST);
        }
    }

    //MODULOS DE TRANSACCIONES INTERBANCARIAS
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;
    @Transactional
    @PostMapping("/operacionInterbancaria/{banco}")
    public ResponseEntity<?> createTransaccionInterbancaria(@RequestBody TransaccionDTO transaccionDTO, @PathVariable String banco) {
        String serviceUrl = discoveryClient.getInstances(banco)
                .stream()
                .findFirst()
                .map(si -> si.getUri().toString())
                .orElseThrow(() -> new RuntimeException("Banco no esta disponible"));

        String cuentaOrigen = transaccionDTO.getCuentaOrigenNumero();
        Optional<Cuenta> cuentaOrigenOp = cuentaService.getCuentaByNumeroCuenta(cuentaOrigen);

        if (cuentaOrigenOp.isPresent()) {
            Cuenta cuentaO = cuentaOrigenOp.get();
            if (cuentaO.getSaldo().compareTo(transaccionDTO.getMonto()) < 0) {
                return new ResponseEntity<>("Saldo insuficiente", HttpStatus.BAD_REQUEST);
            }
            cuentaO.setSaldo(cuentaO.getSaldo().subtract(transaccionDTO.getMonto()));

            Instant t1 = Instant.now();
            transaccionDTO.setRequestTime(t1);

            try {
                // Enviar la solicitud de transacción al banco destino
                ResponseEntity<TransaccionDTO> response = restTemplate.postForEntity(
                        serviceUrl + "/transacciones/external",
                        transaccionDTO,
                        TransaccionDTO.class
                );

                if (response.getStatusCode() == HttpStatus.CREATED) {
                    // Marca de tiempo T4 (respuesta recibida)
                    Instant t4 = Instant.now();
                    TransaccionDTO responseBody = response.getBody();

                    // Marca de tiempo T3 del banco destino
                    Instant t3 = responseBody.getResponseTime();

                    // Calcula RTT (round-trip time)
                    Duration rtt = Duration.between(t1, t4);

                    // Tiempo de viaje unidireccional (estimado)
                    Duration oneWayDelay = rtt.dividedBy(2);

                    // Ajuste del tiempo T2 recibido del banco destino
                    Instant t2 = t3.minus(oneWayDelay);

                    // Crear y guardar la transacción con los tiempos ajustados
                    TransaccionExterna transaccionExterna = TransaccionExterna.builder()
                            .cuentaOrigen(transaccionDTO.getCuentaOrigenNumero())
                            .cuentaDestino(transaccionDTO.getCuentaDestinoNumero())
                            .monto(transaccionDTO.getMonto())
                            .tipoTransaccion(TipoTransaccion.TRANSACCION_INTERBANCARIA)
                            .bancoDestino(transaccionDTO.getDestinoBanco())
                            .bancoOrigen(transaccionDTO.getOrigenBanco())
                            .fecha(Date.from(t2)) // Almacena T2 como fecha ajustada
                            .build();
                    transaccionExternaService.saveTransaccion(transaccionExterna);

                    return new ResponseEntity<>("Transacción Interbancaria exitosa", HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>("Error al registrar la transacción en el banco destino", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                return new ResponseEntity<>("Error al comunicar con el banco destino: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("No existe la cuenta origen", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @PostMapping("/external")
    public ResponseEntity<?> recibirTransaccionExterna(@RequestBody TransaccionDTO transaccionDTO) {
        String cuentaDestino = transaccionDTO.getCuentaDestinoNumero();
        Optional<Cuenta> cuentaDestinoOp = cuentaService.getCuentaByNumeroCuenta(cuentaDestino);

        if (cuentaDestinoOp.isPresent()) {
            Cuenta cuentaD = cuentaDestinoOp.get();
            cuentaD.setSaldo(cuentaD.getSaldo().add(transaccionDTO.getMonto()));
            cuentaService.saveCuenta(cuentaD);
            // Marca de tiempo T3 (respuesta recibida)
            Instant t3 = Instant.now();
            // Crear y guardar la transacción con los tiempos
            TransaccionExterna transaccionExterna = TransaccionExterna.builder()
                    .cuentaOrigen(transaccionDTO.getCuentaOrigenNumero())
                    .cuentaDestino(transaccionDTO.getCuentaDestinoNumero())
                    .monto(transaccionDTO.getMonto())
                    .tipoTransaccion(TipoTransaccion.TRANSACCION_INTERBANCARIA)
                    .bancoOrigen(transaccionDTO.getOrigenBanco())
                    .bancoDestino(transaccionDTO.getDestinoBanco())
                    .fecha(Date.from(t3)) // Usamos la fecha para T3
                    .build();

            transaccionExternaService.saveTransaccion(transaccionExterna);
            // Devolver la transacción con la marca de tiempo T3 incluida
            transaccionDTO.setResponseTime(t3);
            return new ResponseEntity<>(transaccionDTO, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("No existe la cuenta de destino", HttpStatus.BAD_REQUEST);
        }
    }


    //FIN INTERBANCARIO


    @PutMapping("/{id}")
    public TransaccionDTO updateTransaccion(@PathVariable Long id, @RequestBody TransaccionDTO transaccionDTO) {
        Transaccion transaccion = convertirAEntity(transaccionDTO);
        transaccion.setId(id);
        return convertirADTO(transaccionService.saveTransaccion(transaccion));
    }

    @DeleteMapping("/{id}")
    public void deleteTransaccion(@PathVariable Long id) {
        transaccionService.deleteTransaccion(id);
    }
    private TransaccionDTO convertirADTO(Transaccion transaccion) {
        return TransaccionDTO.builder()
                .id(transaccion.getId())
                .tipo(transaccion.getTipo())
                .monto(transaccion.getMonto())
                .fecha(transaccion.getFecha())
                .cuentaOrigenNumero(transaccion.getCuentaOrigen().getNumeroCuenta())
                .cuentaDestinoNumero(transaccion.getCuentaDestino().getNumeroCuenta())
                .origenBanco(transaccion.getBancoOrigen().getNombre())
                .destinoBanco(transaccion.getBancoDestino().getNombre())
                .build();
    }

    private TransaccionDTO convertirADTOExterna(TransaccionExterna transaccionExterna) {
        return TransaccionDTO.builder()
                .id(transaccionExterna.getId())
                .tipo(transaccionExterna.getTipoTransaccion())
                .monto(transaccionExterna.getMonto())
                .fecha(transaccionExterna.getFecha())
                .cuentaOrigenNumero(transaccionExterna.getCuentaOrigen())
                .cuentaDestinoNumero(transaccionExterna.getCuentaDestino())
                .origenBanco(transaccionExterna.getBancoOrigen())
                .destinoBanco(transaccionExterna.getBancoDestino())
                .build();
    }
/*
    private TransaccionDTO convertirADto(Transaccion transaccion) {
        return TransaccionDTO.builder()
                .id(transaccion.getId())
                .tipo(transaccion.getTipo())
                .monto(transaccion.getMonto())
                .fecha(transaccion.getFecha())
                .cuentaOrigenNumero(transaccion.getCuentaOrigen().getNumeroCuenta())
                .cuentaDestinoNumero(transaccion.getCuentaDestino().getNumeroCuenta())
                .origenBanco(transaccion.getBancoOrigen().getNombre())
                .destinoBanco(transaccion.getBancoDestino().getNombre())
                /*
                .cuentaOrigenNumero(transaccion.getCuentaOrigenReferencia())
                .cuentaDestinoNumero(transaccion.getCuentaDestinoReferencia())
                .origenBanco(transaccion.getOrigenBancoReferencia())
                .destinoBanco(transaccion.getDestinoBancoReferencia())
                .id(transaccion.getId())
                .tipo(transaccion.getTipo())
                .monto(transaccion.getMonto())
                .fecha(transaccion.getFecha())
                .cuentaOrigenId(transaccion.getCuentaOrigen().getId())
                .cuentaDestinoId(transaccion.getCuentaDestino() != null ? transaccion.getCuentaDestino().getId() : null)
                .bancoOrigenId(transaccion.getBancoOrigen().getId())
                .bancoDestinoId(transaccion.getBancoDestino() != null ? transaccion.getBancoDestino().getId() : null)
                .build();
    }


    */
    //deposito,retiro
    private Transaccion convertirAEntity(TransaccionDTO transaccionDTO) {
        return Transaccion.builder()
                .tipo(transaccionDTO.getTipo())
                .monto(transaccionDTO.getMonto())
                .cuentaOrigen(cuentaService.getCuentaByNumeroCuenta(transaccionDTO.getCuentaOrigenNumero()).get())
                .cuentaDestino(cuentaService.getCuentaByNumeroCuenta(transaccionDTO.getCuentaDestinoNumero()).get())
                .build();
    }
    @Data
    class BancoResponse {
        private String message;
        private int statusCode;

    }
}
