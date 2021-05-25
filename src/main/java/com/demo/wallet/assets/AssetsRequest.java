package com.demo.wallet.assets;

import com.demo.wallet.entity.OperationType;

import java.math.BigDecimal;

public class AssetsRequest {
    private OperationType operationType;
    private String ticker;
    private BigDecimal quantity;
    private BigDecimal amount;
    private String userAccountMail;

    public OperationType getOperationType() {
        return operationType;
    }

    public String getTicker() {
        return ticker;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getUserAccountMail() {
        return userAccountMail;
    }
}
