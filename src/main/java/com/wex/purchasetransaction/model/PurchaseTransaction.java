package com.wex.purchasetransaction.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "purchase_transaction")
public class PurchaseTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    @NotBlank(message = "description is mandatory.")
    @Size(max = 50, message = "Description must not exceed 50 characters.")
    private String description;

    @Column(name = "transaction_date")
    @NotNull(message = "transactionDate is mandatory.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate transactionDate;

    @Column(name = "amount", precision = 10, scale = 2)
    @NotNull(message = "amount is mandatory.")
    private BigDecimal amount;


    /**
     * Transient fields for API response purposes
     */
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal convertedAmount;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal exchangeRate;

}
