package com.wex.purchasetransaction.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wex.purchasetransaction.exception.ExchangeRateNotFoundException;
import com.wex.purchasetransaction.exception.PurchaseTransactionNotFoundException;
import com.wex.purchasetransaction.model.PurchaseTransaction;
import com.wex.purchasetransaction.service.CurrencyConversionService;
import com.wex.purchasetransaction.service.PurchaseTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

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
    public ResponseEntity<PurchaseTransaction> getPurchaseTransactionById(@PathVariable Long id) throws PurchaseTransactionNotFoundException {
        //perform validations
        Optional<PurchaseTransaction> transaction = purchaseTransactionService.getPurchaseTransactionById(id);
        if (transaction.isPresent()) {
            return ResponseEntity.ok(transaction.get());
        } else {
            throw new PurchaseTransactionNotFoundException("Transaction not found");
        }
    }

    @GetMapping("/v1/purchasetransaction/{id}/convertCurrency")
    public ResponseEntity<PurchaseTransaction> getPurchaseTransactionInCurrency(
            @PathVariable Long id,
            @RequestParam(required = false) String country) throws JsonProcessingException, ExchangeRateNotFoundException, PurchaseTransactionNotFoundException {
        Optional<PurchaseTransaction> transaction = purchaseTransactionService.getPurchaseTransactionById(id);
        if (transaction.isPresent()) {
            PurchaseTransaction result = currencyConversionService.convertFromUSD(transaction.get(), country);
            return ResponseEntity.ok(result);
        } else {
            throw new PurchaseTransactionNotFoundException("Transaction not found");
        }
    }
}