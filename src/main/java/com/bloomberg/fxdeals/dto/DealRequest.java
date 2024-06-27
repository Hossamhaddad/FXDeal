package com.bloomberg.fxdeals.dto;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class DealRequest {
    @NotNull(message = "Deal Amount is required")
    @NotBlank(message = "Deal ID is required")
    private String dealUniqueId;
    @NotBlank(message = "Currency code is required")
    @Size(min = 3, max = 3, message = "It should be a 3-letter code")
    private String fromCurrency;
    @NotBlank(message = "Currency code is required")
    @Size(min = 3, max = 3, message = "It should be a 3-letter code")
    private String toCurrency;
    @NotBlank(message = "Deal Timestamp is required")
    private String dealTimestamp;
    @NotNull(message = "Deal Amount is required")
    @Positive(message = "Deal Amount must be a positive number")
    private BigDecimal dealAmount;

    public DealRequest (){


    }

    public DealRequest(String dealUniqueId, String fromCurrency, String toCurrency, String dealTimestamp, BigDecimal dealAmount) {
        this.dealUniqueId = dealUniqueId;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.dealTimestamp = dealTimestamp;
        this.dealAmount = dealAmount;
    }

    public String getDealUniqueId() {
        return dealUniqueId;
    }

    public void setDealUniqueId(String dealUniqueId) {
        this.dealUniqueId = dealUniqueId;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getDealTimestamp() {
        return dealTimestamp;
    }

    public void setDealTimestamp(String dealTimestamp) {
        this.dealTimestamp = dealTimestamp;
    }

    public BigDecimal getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(BigDecimal dealAmount) {
        this.dealAmount = dealAmount;
    }
}
