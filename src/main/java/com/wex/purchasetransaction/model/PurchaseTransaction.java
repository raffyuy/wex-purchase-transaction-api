package com.wex.purchasetransaction.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Date;

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
    @NotNull
    @Size(max = 50, message = "Description must not exceed 50 characters")
    private String description;

    @Column(name = "transaction_date")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date transactionDate;

    @Column(name = "amount")
    private BigDecimal amount;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal convertedAmount;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String convertedCurrency;
}
