package com.demo.wallet.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AssetsRecalculateTest {

    @Test
    public void updateAverageCostWhenFirstTickerEntry() {
        var userAccount = new UserAccount("elton@teste.com");
        var current = new Assets("BBAS3", BigDecimal.TEN, BigDecimal.valueOf(330), userAccount);

        var newAsset = new AssetsRecalculate(current, null, OperationType.BUY)
                .recalculate();

        assertEquals(new BigDecimal("33"), newAsset.getAveragePrice());
    }

    @Test
    public void updateAverageCostWhenPurchasedIsOperation() {
        var userAccount = new UserAccount("elton@teste.com");
        var current = new Assets("BBAS3", BigDecimal.TEN, BigDecimal.valueOf(330), userAccount);

        var persisted = new Assets("BBAS3", BigDecimal.TEN, BigDecimal.ZERO, userAccount);
        persisted.setTotalPrice(BigDecimal.valueOf(300));

        var newAsset = new AssetsRecalculate(current, persisted, OperationType.BUY)
                .recalculate();

        assertEquals(new BigDecimal("31.50"), newAsset.getAveragePrice());
    }

    @Test
    public void discountQuantityWhenTheOperationIsForSale() {
        var userAccount = new UserAccount("elton@teste.com");
        var current = new Assets("ITUB3", BigDecimal.TEN, BigDecimal.valueOf(220), userAccount);

        var persisted = new Assets("ITUB3", BigDecimal.valueOf(50), BigDecimal.ZERO, userAccount);
        persisted.setAveragePrice(BigDecimal.TEN);
        persisted.setTotalPrice(BigDecimal.valueOf(500));

        var newAsset = new AssetsRecalculate(current, persisted, OperationType.SALE)
                .recalculate();

        assertEquals(BigDecimal.TEN, newAsset.getAveragePrice());
        assertEquals(BigDecimal.valueOf(40), newAsset.getQuantity());
    }
}
