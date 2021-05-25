package com.demo.wallet.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;

public class AssetsRecalculate {

    private static final Logger log =
            LoggerFactory.getLogger(Assets.class);
    private final Assets current;
    private final Assets persisted;

    public AssetsRecalculate(Assets current, Assets persisted){
        this.current = current;
        this.persisted = persisted;
    }

    public Assets recalculate() {
        if (persisted == null) {
            current.setAveragePrice(current.getAmount().divide(current.getQuantity(), RoundingMode.HALF_UP));
            current.setTotalPrice(current.getAmount());
            return current;
        }

        log.info("Recalculando ativo {}", this);
        if (OperationType.BUY.equals(current.getOperationType())) {
            persisted.setQuantity(persisted.getQuantity().add(current.getQuantity()));
            persisted.setTotalPrice(persisted.getTotalPrice().add(current.getAmount()));
            persisted.setAveragePrice(persisted.getTotalPrice().divide(persisted.getQuantity(), RoundingMode.HALF_UP));
        } else {
            persisted.setQuantity(persisted.getQuantity().subtract(current.getQuantity()));
            persisted.setTotalPrice(persisted.getQuantity().multiply(persisted.getAveragePrice()));
        }
        log.info("Ativo recalculado {}", this);
        return persisted;
    }

}
