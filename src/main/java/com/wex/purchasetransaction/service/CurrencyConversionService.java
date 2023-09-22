package com.wex.purchasetransaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wex.purchasetransaction.exception.ExchangeRateNotFoundException;
import com.wex.purchasetransaction.model.ExchangeRateResponse;
import com.wex.purchasetransaction.model.PurchaseTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;

@Service
public class CurrencyConversionService {

    @Value("${app.data.conversion-api.endpoint}")
    private String conversionApiEndpoint;

    @Autowired
    private RestTemplate restTemplate;

    public PurchaseTransaction convertFromUSD(PurchaseTransaction transaction, String targetCountry) throws RestClientException, JsonProcessingException, ExchangeRateNotFoundException, UnsupportedEncodingException {
        LocalDate sixMonthsAgo = transaction.getTransactionDate().minus(Period.ofMonths(6));

        //can better build this string, but for simplicity and readability, I will keep it this way
        String apiUrl = conversionApiEndpoint +
                "?fields=effective_date,country,exchange_rate" +
                "&filter=country:eq:{country}" +
                    ",effective_date:lte:{transactionDate}" +
                    ",effective_date:gt:{sixMonthsAgo}" +
                "&page[size]=1" +
                "&sort=-effective_date";

        URI uri = new UriTemplate(apiUrl).expand(targetCountry, transaction.getTransactionDate(), sixMonthsAgo);
        ExchangeRateResponse response = restTemplate.getForObject(uri, ExchangeRateResponse.class);

        if (response != null && response.data().length != 0) {
            BigDecimal exchangeRate = response.data()[0].exchangeRate();
            BigDecimal convertedAmount = transaction.getAmount().multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
            transaction.setExchangeRate(exchangeRate);
            transaction.setConvertedAmount(convertedAmount);
            return transaction;
        } else {
            // Handle the case where no exchange rate is available
            throw new ExchangeRateNotFoundException("Conversion rate not found for the specified country and transaction date.");
        }
    }
}
