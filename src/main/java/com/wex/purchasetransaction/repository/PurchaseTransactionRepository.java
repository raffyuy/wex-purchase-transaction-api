package com.wex.purchasetransaction.repository;

import com.wex.purchasetransaction.model.PurchaseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseTransactionRepository extends JpaRepository<PurchaseTransaction, Long> {

}
