package br.com.cinema.cinemasystem.controller;

import br.com.cinema.cinemasystem.dto.purchase.PurchaseDTO;
import br.com.cinema.cinemasystem.dto.purchase.PurchaseRequestDTO;
import br.com.cinema.cinemasystem.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody PurchaseRequestDTO requestDTO) {
        PurchaseDTO newPurchase = purchaseService.createPurchase(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPurchase);
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseDTO> getPurchaseById(@PathVariable Long purchaseId) {
        PurchaseDTO purchaseDTO = purchaseService.findPurchaseById(purchaseId);
        return ResponseEntity.ok(purchaseDTO);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseDTO>> getPurchasesByUser(@RequestParam Long userId) {
        List<PurchaseDTO> purchases = purchaseService.findPurchasesByUserId(userId);
        return ResponseEntity.ok(purchases);
    }
}