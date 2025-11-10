package br.com.cinema.cinemasystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relação 1:1 com Purchase.
     * O pagamento está vinculado a uma compra específica.
     * @MapsId indica que o ID do pagamento é o mesmo ID da compra.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String paymentMethod; // Ex: CREDIT_CARD, PIX, CASH

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; // APPROVED, DECLINED, PENDING

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(unique = true)
    private String transactionId; // ID simulado de transação (mock de gateway)
}
