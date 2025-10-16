//package br.com.cinema.cinemasystem.controller;
//
//
//import com.cinemasystemspring.dto.CreatePurchaseRequestDTO;
//import com.cinemasystemspring.dto.PurchaseDTO;
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
//    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody CreatePurchaseRequestDTO requestDTO){
//        PurchaseDTO newPurchaseDTO = purchaseService.createPurchase(requestDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(newPurchaseDTO);
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