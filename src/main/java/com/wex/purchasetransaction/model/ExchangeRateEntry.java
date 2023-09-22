package com.wex.purchasetransaction.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;


public record ExchangeRateEntry(String country,
                                @JsonProperty("effective_date") LocalDate effectiveDate,
                                @JsonProperty("exchange_rate") BigDecimal exchangeRate) {
}