package com.demo.wallet.service;

import com.demo.wallet.assets.repository.AssetsRepository;
import com.demo.wallet.assets.service.AssetsService;
import com.demo.wallet.entity.Assets;
import com.demo.wallet.entity.OperationType;
import com.demo.wallet.entity.UserAccount;
import com.demo.wallet.exception.NoResultException;
import com.demo.wallet.exception.UnsupportedOperationTypeException;
import com.demo.wallet.userAccount.repository.UserAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssetsServiceTest {

    @InjectMocks
    private AssetsService assetsService;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private AssetsRepository assetsRepository;

    @Test
    public void shouldThrowExceptionWhenSavingAssetWithoutUserAccountAdded() {
        when(userAccountRepository.findByMail(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoResultException.class, () -> assetsService.save(new Assets("MGLU3", BigDecimal.TEN, BigDecimal.valueOf(100), new UserAccount("elton@teste.com")),
                OperationType.BUY));

        String expectedMessage = "Conta de usuário elton@teste.com não cadastrada";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldThrowUnsupportedOperationTypeExceptionWhenSaleAssetWithoutPersistedBuy() {
        var userAccount = new UserAccount("elton@teste.com");
        when(userAccountRepository.findByMail(any())).thenReturn(Optional.of(userAccount));
        when(assetsRepository.findByUserAccountIdAndTicker(any(), any())).thenReturn(Optional.empty());

        Assertions.assertThrows(UnsupportedOperationTypeException.class,
                () -> assetsService.save(new Assets("MGLU3", BigDecimal.TEN, BigDecimal.valueOf(100), userAccount), OperationType.SALE));
    }

    @Test
    public void shouldThrowUnsupportedOperationTypeExceptionWhenQuantitySaleGreaterThanSale() {
        var userAccount = new UserAccount("elton@teste.com");
        var persistedAsset = new Assets("MGLU3", BigDecimal.valueOf(6), BigDecimal.valueOf(50), userAccount);
        when(userAccountRepository.findByMail(any())).thenReturn(Optional.of(userAccount));
        when(assetsRepository.findByUserAccountIdAndTicker(any(), any())).thenReturn(Optional.of(persistedAsset));

        Assertions.assertThrows(UnsupportedOperationTypeException.class,
                () -> assetsService.save(new Assets("MGLU3", BigDecimal.TEN, BigDecimal.valueOf(100), userAccount), OperationType.SALE));
    }
}
