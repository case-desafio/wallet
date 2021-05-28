package com.demo.wallet.userAccount;

import com.demo.wallet.entity.UserAccount;
import com.demo.wallet.exception.NoResultException;
import com.demo.wallet.exception.UserAccountAlreadyRegisteredException;
import com.demo.wallet.userAccount.repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/user-account")
public class UserAccountController {

    private static final Logger log =
            LoggerFactory.getLogger(UserAccountController.class);

    private final UserAccountRepository userAccountRepository;

    public UserAccountController(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<UserAccountResponse> create(@Valid @RequestBody @NonNull UserAccountRequest userAccountRequest) {
        log.info("Verificando se conta de usuário {} já está cadastrada", userAccountRequest.getMail());
        var userAccountOptional = userAccountRepository.findByMail(userAccountRequest.getMail());
        if (userAccountOptional.isPresent() && userAccountOptional.get().getId() != null) {
            log.error("Conta de usuário {} já cadastrada", userAccountRequest.getMail());
            throw new UserAccountAlreadyRegisteredException();
        }

        var userAccount = userAccountRepository.save(new UserAccount(userAccountRequest.getMail()));
        return ResponseEntity.created(URI.create("/" + userAccount.getId())).body(
                new UserAccountResponse(userAccount.getId(), userAccountRequest.getMail())
        );
    }

    @GetMapping("/{id}")
    public UserAccountResponse findById(@PathVariable Long id) {
        var userAccountResponse = userAccountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Conta de usuário com id {} não encontrada", id);
                    throw new NoResultException(String.format("Conta de usuário não encontrada %s", id));
                });
        log.info("Retornando conta de usuário {}", userAccountResponse.getMail());
        return new UserAccountResponse(userAccountResponse.getId(), userAccountResponse.getMail());
    }
}
