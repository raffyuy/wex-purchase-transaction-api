package com.wex.purchasetransaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wex.purchasetransaction.model.ExchangeRateEntry;
import com.wex.purchasetransaction.model.ExchangeRateResponse;
import com.wex.purchasetransaction.model.PurchaseTransaction;
import com.wex.purchasetransaction.service.CurrencyConversionService;
import com.wex.purchasetransaction.service.PurchaseTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class PurchaseTransactionControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PurchaseTransactionController controller;

    @Mock
    private PurchaseTransactionService purchaseTransactionService;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createPurchaseTransaction_valid() throws Exception {
        PurchaseTransaction transaction = PurchaseTransaction.builder()
                .description("test")
                .transactionDate(LocalDate.now())
                .amount(new BigDecimal("12.34")).build();

        when(purchaseTransactionService.createPurchaseTransaction(any(PurchaseTransaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/api/v1/purchasetransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.amount").value("12.34"));
    }

    @Test
    void createPurchaseTransaction_invalidDescriptionTooLong() throws Exception {
        PurchaseTransaction transaction = PurchaseTransaction.builder()
                .description("123456789012345678901234567890123456789012345678901")
                .transactionDate(LocalDate.now())
                .amount(new BigDecimal("12.34")).build();

        when(purchaseTransactionService.createPurchaseTransaction(any(PurchaseTransaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/api/v1/purchasetransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.amount").value("12.34"));
    }

    @Test
    void getPurchaseTransactionById() throws Exception {
        PurchaseTransaction transaction = PurchaseTransaction.builder()
                .id(1L)
                .description("test")
                .transactionDate(LocalDate.now())
                .amount(new BigDecimal("12.34")).build();

        when(purchaseTransactionService.getPurchaseTransactionById(any(Long.class))).thenReturn(transaction);

        mockMvc.perform(get("/api/v1/purchasetransaction/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.amount").value("12.34"));
    }

    @Test
    void getPurchaseTransactionInCurrency() throws Exception {
        PurchaseTransaction transaction = PurchaseTransaction.builder()
                .id(1L)
                .description("test")
                .transactionDate(LocalDate.now())
                .amount(new BigDecimal("12.34")).build();
        PurchaseTransaction result = PurchaseTransaction.builder()
                .id(1L)
                .description("test")
                .transactionDate(LocalDate.now())
                .amount(new BigDecimal("12.34"))
                .convertedAmount(new BigDecimal("24.68"))
                .exchangeRate(BigDecimal.valueOf(2))
                .build();

        when(purchaseTransactionService.getPurchaseTransactionById(any(Long.class))).thenReturn(transaction);
        when(currencyConversionService.convertFromUSD(any(PurchaseTransaction.class), anyString())).thenReturn(result);

        String test = mockMvc.perform(get("/api/v1/purchasetransaction/1111/convertCurrency?country=Australia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("test"))
                .andExpect(jsonPath("$.amount").value("12.34"))
                .andExpect(jsonPath("$.exchangeRate").value("2"))
                .andExpect(jsonPath("$.convertedAmount").value("24.68"))
                .andReturn().getResponse().getContentAsString();

        System.out.println(test);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}