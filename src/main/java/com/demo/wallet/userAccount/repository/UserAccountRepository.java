package com.demo.wallet.userAccount.repository;

import com.demo.wallet.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByMail(String mail);
}
