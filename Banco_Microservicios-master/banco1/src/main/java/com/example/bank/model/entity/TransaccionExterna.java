package com.example.bank.model.entity;

import com.example.bank.util.TipoTransaccion;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionExterna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cuentaOrigen;

    @Column(nullable = false)
    private String cuentaDestino;

    @Column(nullable = false, length = 100)
    private String bancoOrigen;

    @Column(nullable = false, length = 100)
    private String bancoDestino;

    @Column(nullable = false)
    private BigDecimal monto;

    private TipoTransaccion tipoTransaccion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fecha;

    @PrePersist
    protected void onCreate() {
        fecha = new Date();
    }
}