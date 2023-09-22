package com.wex.purchasetransaction.service;

import com.wex.purchasetransaction.model.PurchaseTransaction;
import com.wex.purchasetransaction.repository.PurchaseTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class PurchaseTransactionService {

    @Autowired
    private PurchaseTransactionRepository repository;

    public PurchaseTransaction createPurchaseTransaction(PurchaseTransaction transaction) {
        transaction.setAmount(transaction.getAmount().setScale(2, RoundingMode.HALF_UP));
        return repository.save(transaction);
    }

    public PurchaseTransaction getPurchaseTransactionById(Long id) {
        Optional<PurchaseTransaction> result = repository.findById(id);
        return result.orElse(null);
    }

}
