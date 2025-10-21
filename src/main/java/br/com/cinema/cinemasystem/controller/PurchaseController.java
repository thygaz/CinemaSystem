//package br.com.cinema.cinemasystem.controller;
//
//
//import br.com.cinema.cinemasystem.model.Purchase;
//import br.com.cinema.cinemasystem.service.PurchaseService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/purchases")
//public class PurchaseController {
//
//    @Autowired
//    private PurchaseService purchaseService;
//
//    @PostMapping
//    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase){
//        Purchase newPurchase = purchaseService.createPurchase(purchase);
//        return ResponseEntity.status(HttpStatus.CREATED).body(newPurchase);
//    }
//
//    @GetMapping("/{purchaseId}")
//    public ResponseEntity<PurchaseDTO> getPurchaseById(@RequestBody Long purchaseId){
//        PurchaseDTO purchaseDTO = purchaseService.findPurchaseById(purchaseId);
//        return ResponseEntity.ok(purchaseDTO);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<PurchaseDTO>> getPurchasesByUser(@RequestParam Long userId) {
//        List<PurchaseDTO> purchases = purchaseService.findPurchasesByUserId(userId);
//        return ResponseEntity.ok(purchases);
//    }
//
//}