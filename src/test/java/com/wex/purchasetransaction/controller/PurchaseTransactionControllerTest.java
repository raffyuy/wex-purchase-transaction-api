package com.wex.purchasetransaction.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriTemplate;
import org.springframework.web.util.UriUtils;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class PurchaseTransactionControllerTest {

    public static final String CREATE_PURCHASE_TRANSACTION_URI = "/api/v1/purchasetransaction";
    public static final String ERROR_MESSAGE_JSON_PATH = "$.error.message";
    private MockMvc mockMvc;

    @InjectMocks
    private PurchaseTransactionController controller;

    @MockBean
    private PurchaseTransactionService purchaseTransactionService;

    @MockBean
    private CurrencyConversionService currencyConversionService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void createPurchaseTransaction_valid() throws Exception {
        PurchaseTransaction transaction = PurchaseTransaction.builder()
                .description("test")
                .transactionDate(LocalDate.now())
                .amount(new BigDecimal("12.34")).build();

        when(purchaseTransactionService.createPurchaseTransaction(any(PurchaseTransaction.class))).thenReturn(transaction);

        mockMvc.perform(post(CREATE_PURCHASE_TRANSACTION_URI)
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

        mockMvc.perform(post(CREATE_PURCHASE_TRANSACTION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transaction)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_MESSAGE_JSON_PATH)
                        .value("Validation errors found: [Description must not exceed 50 characters.]"));
    }

    @Test
    void createPurchaseTransaction_invalidTransactionDate() throws Exception {
        String input = "{\n" +
                "    \"description\": \"test\",\n" +
                "    \"transactionDate\": \"20220\",\n" +
                "    \"amount\": \"1.205\"\n" +
                "}";

        mockMvc.perform(post(CREATE_PURCHASE_TRANSACTION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_MESSAGE_JSON_PATH)
                        .value("Invalid input format. Please check if the date and number formats are correct."));
    }

    @Test
    void createPurchaseTransaction_invalidAmount() throws Exception {
        String input = "{\n" +
                "    \"description\": \"test\",\n" +
                "    \"transactionDate\": \"2022-02-20\",\n" +
                "    \"amount\": \"notanumber\"\n" +
                "}";

        mockMvc.perform(post(CREATE_PURCHASE_TRANSACTION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_MESSAGE_JSON_PATH)
                        .value("Invalid input format. Please check if the date and number formats are correct."));
    }

    @Autowired
    RestTemplate restTemplate;
    @Test
    void test() throws JsonProcessingException {
        String uri = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange?" +
                "fields=effective_date,country,exchange_rate" +
                "&filter=country:eq:{country},effective_date:lte:2020-03-22,effective_date:gt:2019-09-22&page[size]=1&sort=-effective_date";

        URI expanded = new UriTemplate(uri).expand("Antigua & Barbuda");
        ExchangeRateResponse forObject = restTemplate.getForObject(expanded, ExchangeRateResponse.class);
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        System.out.println(om.writeValueAsString(forObject));
    }

    @Test
    void getPurchaseTransactionById() throws Exception {
        PurchaseTransaction transaction = PurchaseTransaction.builder()
                .id(1L)
                .description("test")
                .transactionDate(LocalDate.now())
                .amount(new BigDecimal("12.34")).build();

        when(purchaseTransactionService.getPurchaseTransactionById(any(Long.class))).thenReturn(Optional.of(transaction));

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

        when(purchaseTransactionService.getPurchaseTransactionById(any(Long.class))).thenReturn(Optional.of(transaction));
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