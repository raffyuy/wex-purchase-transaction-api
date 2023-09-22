package com.wex.purchasetransaction.service;

import com.wex.purchasetransaction.model.PurchaseTransaction;
import com.wex.purchasetransaction.repository.PurchaseTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class PurchaseTransactionServiceTest {

    @InjectMocks
    private PurchaseTransactionService service;

    @Mock
    private PurchaseTransactionRepository repository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createPurchaseTransaction() {
        PurchaseTransaction transaction = PurchaseTransaction.builder()
                .id(1L)
                .description("test")
                .transactionDate(LocalDate.now())
                .amount(new BigDecimal("12.34")).build();

        when(repository.save(any(PurchaseTransaction.class))).thenReturn(transaction);

        PurchaseTransaction saved = service.createPurchaseTransaction(transaction);

        assertEquals("test", saved.getDescription());
        assertEquals(new BigDecimal("12.34"), saved.getAmount());
        verify(repository, times(1)).save(transaction);
    }

    @Test
    void getPurchaseTransactionById() {
        PurchaseTransaction transaction = PurchaseTransaction.builder()
                .id(1111L)
                .description("test")
                .transactionDate(LocalDate.now())
                .amount(new BigDecimal("12.34")).build();

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(transaction));

        Optional<PurchaseTransaction> result = service.getPurchaseTransactionById(1L);

        assertTrue(result.isPresent());
        assertEquals("test", result.get().getDescription());
        assertEquals(new BigDecimal("12.34"), result.get().getAmount());
        verify(repository, times(1)).findById(1L);
    }

}