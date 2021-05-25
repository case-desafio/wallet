package com.demo.wallet.assets.repository;

import com.demo.wallet.entity.Assets;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetsRepository extends JpaRepository<Assets, Long> {

    Optional<Assets> findByTicker(String ticker);

    Optional<List<Assets>> findByUserAccountId(Long accountId);

    Optional<Assets> findByUserAccountIdAndTicker(Long accountId, String ticker);
}
