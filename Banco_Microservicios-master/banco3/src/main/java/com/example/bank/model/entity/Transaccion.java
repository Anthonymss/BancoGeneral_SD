package com.example.bank.model.entity;

import com.example.bank.util.TipoTransaccion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
/*
    private String cuentaOrigenBanco;
    private String cuentaOrigenNumero;
    private String cuentaDestinoBanco;
    private String cuentaDestinoNumero;
 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransaccion tipo;

    @Column(nullable = false)
    private BigDecimal monto;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fecha;

    //private String cuentaOrigenReferencia;
    @ManyToOne
    @JoinColumn(name = "cuenta_origen_id")
    private Cuenta cuentaOrigen;

    //private String cuentaDestinoReferencia;
    @ManyToOne
    @JoinColumn(name = "cuenta_destino_id")
    private Cuenta cuentaDestino;

    //private String OrigenBancoReferencia;
    @ManyToOne
    @JoinColumn(name = "banco_origen_id", nullable = false)
    private Banco bancoOrigen;

    //private String DestinoBancoReferencia;
    @ManyToOne
    @JoinColumn(name = "banco_destino_id")
    private Banco bancoDestino;

    @PrePersist
    protected void onCreate() {
        fecha = new Date();
    }
    public void asignarDeposito() {
        cuentaDestino = cuentaOrigen;
        this.bancoDestino =this.bancoOrigen= cuentaOrigen.getBanco();
        //this.cuentaOrigenReferencia=this.cuentaDestinoReferencia =cuentaOrigen.getNumeroCuenta();
        //this.OrigenBancoReferencia =this.DestinoBancoReferencia =cuentaOrigen.getBanco().getNombre();

        //se requier id del banco origen, si este deposito se hace desde otro banco
        //desde la sesion que este el id del banco y que se envie este como origen
    }

    public void asignarRetiro() {
        cuentaDestino = cuentaOrigen;
        this.bancoOrigen =this.bancoDestino= cuentaOrigen.getBanco();
        //this.cuentaOrigenReferencia=this.cuentaDestinoReferencia =cuentaOrigen.getNumeroCuenta();
        //this.OrigenBancoReferencia =this.DestinoBancoReferencia =cuentaOrigen.getBanco().getNombre();
        //se requier id del banco destino, si este retiro se hace desde otro banco
    }

    public void asignarTransferencia() {
        this.bancoOrigen = cuentaOrigen.getBanco();
        this.bancoDestino = cuentaDestino.getBanco();
        //se asigna segun las cuentas
    }


}
