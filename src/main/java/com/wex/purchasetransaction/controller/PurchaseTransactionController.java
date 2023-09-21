package com.wex.purchasetransaction.controller;

import com.wex.purchasetransaction.model.PurchaseTransaction;
import com.wex.purchasetransaction.repository.PurchaseTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class PurchaseTransactionController {

    @Autowired
    PurchaseTransactionRepository repository;

    @PostMapping("/v1/purchasetransaction")
    public ResponseEntity<PurchaseTransaction> createPurchaseTransaction(@RequestBody PurchaseTransaction purchaseTransaction) {
        //perform validations

        PurchaseTransaction result = repository.save(purchaseTransaction);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/v1/purchasetransaction/{id}")
    public ResponseEntity<PurchaseTransaction> getPurchaseTransactionInCurrency(@RequestParam(required = false) String currency) {
        return null;
    }
}