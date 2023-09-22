package com.wex.purchasetransaction.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wex.purchasetransaction.exception.ConversionRateNotFoundException;
import com.wex.purchasetransaction.model.PurchaseTransaction;
import com.wex.purchasetransaction.service.CurrencyConversionService;
import com.wex.purchasetransaction.service.PurchaseTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class PurchaseTransactionController {

    @Autowired
    PurchaseTransactionService purchaseTransactionService;

    @Autowired
    CurrencyConversionService currencyConversionService;

    @PostMapping("/v1/purchasetransaction")
    public ResponseEntity<PurchaseTransaction> createPurchaseTransaction(@RequestBody @Valid PurchaseTransaction purchaseTransaction) {
        PurchaseTransaction transaction = purchaseTransactionService.createPurchaseTransaction(purchaseTransaction);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/v1/purchasetransaction/{id}")
    public ResponseEntity<PurchaseTransaction> getPurchaseTransactionById(@PathVariable Long id) {
        //perform validations
        PurchaseTransaction transaction = purchaseTransactionService.getPurchaseTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/v1/purchasetransaction/{id}/convertCurrency")
    public ResponseEntity<PurchaseTransaction> getPurchaseTransactionInCurrency(
            @PathVariable Long id,
            @RequestParam(required = false) String country) throws JsonProcessingException, ConversionRateNotFoundException {
        PurchaseTransaction transaction = purchaseTransactionService.getPurchaseTransactionById(id);
        transaction = currencyConversionService.convertFromUSD(transaction, country);
        return ResponseEntity.ok(transaction);
    }
}