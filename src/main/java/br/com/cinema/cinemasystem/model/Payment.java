//package br.com.cinema.cinemasystem.model;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "payments")
//public class Payment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    @JoinColumn(name = "purchases_id")
//    private Purchase purchase;
//
//    @Column(nullable = false)
//    private BigDecimal amount;
//
//    @Column(nullable = false)
//    private String paymentMethod;
//
//    @Column(nullable = false)
//    private String status; //e.g COMPLETE, PENDING, FAILED
//
//}