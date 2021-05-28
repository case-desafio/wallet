package com.demo.wallet.assets.service;

import com.demo.wallet.assets.repository.AssetsRepository;
import com.demo.wallet.entity.Assets;
import com.demo.wallet.entity.AssetsRecalculate;
import com.demo.wallet.entity.OperationType;
import com.demo.wallet.exception.NoResultException;
import com.demo.wallet.exception.UnsupportedOperationTypeException;
import com.demo.wallet.userAccount.repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetsService {

    private static final Logger log =
            LoggerFactory.getLogger(AssetsService.class);

    private final AssetsRepository assetsRepository;

    private final UserAccountRepository userAccountRepository;

    public AssetsService(AssetsRepository assetsRepository, UserAccountRepository userAccountRepository) {
        this.assetsRepository = assetsRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public Assets save(Assets assets, OperationType operationType) {
        final String mail = assets.getUserAccountEmail();
        var userAccount = userAccountRepository.findByMail(mail).orElseThrow(() -> {
            throw new NoResultException(String.format("Conta de usuário %s não cadastrada", mail));
        });
        assets.setUserAccount(userAccount);

        var persistedAsset = this.findByUserAccountIdAndTicker(assets.getUserAccount().getId(), assets.getTicker()).orElse(null);

        if (OperationType.SALE.equals(operationType)
                && (persistedAsset == null || assets.quantityGreaterThanOrEqual(persistedAsset))) {
            throw new UnsupportedOperationTypeException();
        }

        var calculatedAsset = new AssetsRecalculate(assets, persistedAsset, operationType).recalculate();

        log.info("Inserindo ativo {}", calculatedAsset);
        calculatedAsset = assetsRepository.save(calculatedAsset);
        log.info("Ativo {} inserido com sucesso", calculatedAsset);
        return calculatedAsset;
    }

    public Optional<Assets> findByUserAccountIdAndTicker(Long accountId, String ticker) {
        log.info("Buscando ativo {} da conta {}", ticker, accountId);
        var assetsOptional= assetsRepository.findByUserAccountIdAndTicker(accountId, ticker);
        if (assetsOptional.isEmpty()) {
            log.info("Nenhum ativo {} encontrado da conta {}", ticker, accountId);
        }
        return assetsOptional;
    }

    public List<Assets> findByUserAccount(Long accountId) {
        log.info("Buscando todas os ativos da conta {}", accountId);
        var assets = assetsRepository.findByUserAccountId(accountId)
                .orElseThrow(() -> {
                    log.info("Nenhum ativo encontrado da conta {} ", accountId);
                    throw new NoResultException("Nenhum ativo encontrado");
                });
        log.info("Encontrado {} ativos da conta {}", assets.size(), accountId);
        return assets;
    }
}
