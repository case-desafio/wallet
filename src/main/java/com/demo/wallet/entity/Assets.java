package com.demo.wallet.entity;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ASSETS", uniqueConstraints = {
        @UniqueConstraint(
                name = "UNQ_TICKER_USER_ACCOUNT",
                columnNames = {"TICKER", "ID_USER_ACCOUNT"}
        )
})
public class Assets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TICKER", nullable = false, length = 10)
    private String ticker;

    @Column(name = "QUANTITY", nullable = false)
    private BigDecimal quantity;

    @Column(name = "AVERAGE_PRICE", nullable = false, precision = 15, scale = 2)
    private BigDecimal averagePrice;

    @Column(name = "TOTAL_PRICE", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_USER_ACCOUNT")
    private UserAccount userAccount;

    private transient BigDecimal amount;

    @Deprecated
    public Assets() {
    }

    public Assets(@NonNull String ticker,
                  @NonNull BigDecimal quantity,
                  @NonNull BigDecimal amount,
                  @NonNull UserAccount userAccount) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.amount = amount;
        this.userAccount = userAccount;
    }

    @Version
    private Long version;

    public Long getId() {
        return id;
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

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public Long getVersion() {
        return version;
    }

    public String getUserAccountEmail() {
        if (userAccount != null) {
            return userAccount.getMail();
        }
        return null;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Assets{" +
                "id=" + id +
                ", ticker='" + ticker +
                ", quantity=" + quantity +
                ", averagePrice=" + averagePrice +
                ", totalPrice=" + totalPrice +
                ", userAccount=" + userAccount +
                '}';
    }

}
