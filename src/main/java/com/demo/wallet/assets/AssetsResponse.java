package com.demo.wallet.assets;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public class AssetsResponse {
    private String ticker;
    private BigDecimal quantity;
    private BigDecimal averagePrice;
    private BigDecimal totalPrice;
    private String userAccount;

    @Deprecated
    public AssetsResponse() {
    }

    public AssetsResponse(@NonNull String ticker, @NonNull BigDecimal quantity,
                          @NonNull BigDecimal averagePrice, @NonNull BigDecimal totalPrice,
                          @NonNull String userAccount) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.totalPrice = totalPrice;
        this.userAccount = userAccount;
    }

    public String getTicker() {
        return ticker;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getUserAccount() {
        return userAccount;
    }
}
