package com.demo.wallet.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
@Table(name = "ASSETS", uniqueConstraints = {
        @UniqueConstraint(
                name = "UNQ_TICKER_USER_ACCOUNT",
                columnNames = {"TICKER", "ID_USER_ACCOUNT"}
        )
})
public class Assets {

    private static final Logger log =
            LoggerFactory.getLogger(Assets.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "OPERATION_TYPE", length = 4)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

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

    public Assets(@NonNull OperationType operationType,
                  @NonNull String ticker,
                  @NonNull BigDecimal quantity,
                  @NonNull BigDecimal amount,
                  @NonNull UserAccount userAccount) {
        this.operationType = operationType;
        this.ticker = ticker;
        this.quantity = quantity;
        this.amount = amount;
        this.userAccount = userAccount;
    }

    private Assets(@NonNull Assets assets) {
        this(assets.getId(), assets.getOperationType(),
                assets.getTicker(), assets.getQuantity(),
                assets.getAveragePrice(), assets.getTotalPrice(),
                assets.getUserAccount(), assets.getAmount(),
                assets.getVersion()
        );
    }

    private Assets(@NonNull Long id, @NonNull OperationType operationType,
                   @NonNull String ticker, @NonNull BigDecimal quantity,
                   @NonNull BigDecimal averagePrice, @NonNull BigDecimal totalPrice,
                   @NonNull UserAccount userAccount, @NonNull BigDecimal amount, @NonNull Long version) {
        this.id = id;
        this.operationType = operationType;
        this.ticker = ticker;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.totalPrice = totalPrice;
        this.userAccount = userAccount;
        this.amount = amount;
        this.version = version;
    }

    @Version
    private Long version;

    public Long getId() {
        return id;
    }

    public OperationType getOperationType() {
        return operationType;
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

    @PrePersist
    private void prePersist() {
        Objects.requireNonNull(quantity);
        Objects.requireNonNull(amount);
        totalPrice = totalPrice!= null
                ? totalPrice.add(amount)
                : amount;
    }

    public Assets recalculate(Assets oldAssets) {
        if (oldAssets == null) {
            this.averagePrice = this.amount.divide(this.quantity, RoundingMode.HALF_UP);
            return new Assets(this);
        }

        log.info("Recalculando ativo {}", this);
        if (OperationType.BUY.equals(operationType)) {
            this.quantity = this.quantity.add(oldAssets.getQuantity());
            this.amount = this.amount.add(oldAssets.getAmount());
            this.averagePrice = this.amount.divide(oldAssets.getAmount(), RoundingMode.HALF_UP);
        } else {
            this.quantity = this.quantity.subtract(oldAssets.getQuantity());
            this.amount = this.quantity.multiply(averagePrice);
        }
        log.info("Ativo recalculado {}", this);
        return new Assets(this);
    }

}
