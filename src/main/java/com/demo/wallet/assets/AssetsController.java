package com.demo.wallet.assets;

import com.demo.wallet.assets.service.AssetsService;
import com.demo.wallet.entity.Assets;
import com.demo.wallet.entity.UserAccount;
import com.demo.wallet.exception.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.demo.wallet.assets.AssetsReponseMapper.mapper;

@RestController
@RequestMapping("assets")
public class AssetsController {

    private static final Logger log =
            LoggerFactory.getLogger(AssetsController.class);

    private final AssetsService assetsService;

    public AssetsController(AssetsService assetsService) {
        this.assetsService = assetsService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<AssetsResponse> create(@Valid @RequestBody @NonNull AssetsRequest assetsRequest) {
        var assets = new Assets(
                assetsRequest.getTicker(),
                assetsRequest.getQuantity(),
                assetsRequest.getAmount(),
                new UserAccount(assetsRequest.getUserAccountMail())
        );
        assets = assetsService.save(assets, assetsRequest.getOperationType());
        return ResponseEntity.created(URI.create("/" + assets.getId())).body(mapper(assets));
    }

    @GetMapping("/userAccount/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AssetsResponse> findByUserAccount(@PathVariable Long id) {
        return assetsService.findByUserAccount(id)
                .stream()
                .map(AssetsReponseMapper::mapper)
                .collect(Collectors.toList());
    }

    @GetMapping("/userAccount/{id}/{ticker}")
    @ResponseStatus(code = HttpStatus.OK)
    public AssetsResponse findByTicker(@PathVariable Long id, @PathVariable String ticker) {
        var assets = assetsService.findByUserAccountIdAndTicker(id, ticker).orElseThrow(() -> {
            log.info(String.format("Nenhum ativo encontrado para o ticker %s", ticker));
            throw new NoResultException(String.format("Nenhum ativo encontrado para o ticker [%s]", ticker));
        });
        return mapper(assets);
    }
}
