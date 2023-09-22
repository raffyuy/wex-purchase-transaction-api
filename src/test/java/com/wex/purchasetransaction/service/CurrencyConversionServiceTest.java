package com.wex.purchasetransaction.service;

import com.wex.purchasetransaction.exception.ExchangeRateNotFoundException;
import com.wex.purchasetransaction.model.ExchangeRateEntry;
import com.wex.purchasetransaction.model.ExchangeRateResponse;
import com.wex.purchasetransaction.model.PurchaseTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
@ExtendWith(MockitoExtension.class)
class CurrencyConversionServiceTest {

    @InjectMocks
    private CurrencyConversionService service;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConvertFromUSD() throws Exception {
        PurchaseTransaction transaction = PurchaseTransaction.builder().id(1111L).description("test")
                .transactionDate(LocalDate.now()).amount(new BigDecimal("12.34")).build();
        ExchangeRateEntry entry = new ExchangeRateEntry("Australia", LocalDate.now(), BigDecimal.valueOf(2));
        ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse(new ExchangeRateEntry[] {entry});

        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(exchangeRateResponse);
        PurchaseTransaction result = service.convertFromUSD(transaction, "Australia");

        verify(restTemplate, times(1)).getForObject(anyString(), eq(ExchangeRateResponse.class));
        assertEquals(new BigDecimal("24.68"), result.getConvertedAmount());
        assertEquals(new BigDecimal("2"), result.getExchangeRate());

    }


    @Test
    public void testConvertFromUSD_throwExceptionWhenNoExchangeRateFound() throws Exception {
        PurchaseTransaction transaction = PurchaseTransaction.builder().id(1111L).description("test")
                .transactionDate(LocalDate.now()).amount(new BigDecimal("12.34")).build();
        ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse(new ExchangeRateEntry[] {});

        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(exchangeRateResponse);

        assertThrows(ExchangeRateNotFoundException.class, () -> service.convertFromUSD(transaction, "Australia"));
    }
}