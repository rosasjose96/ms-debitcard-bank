package com.bootcamp.msDebitService.services.impl;


import com.bootcamp.msDebitService.models.dto.CurrentAccount;
import com.bootcamp.msDebitService.models.dto.FixedTermAccount;
import com.bootcamp.msDebitService.models.dto.SavingAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bootcamp.msDebitService.models.dto.DebitAccountDTO;
import com.bootcamp.msDebitService.services.IDebitAccountDTOService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Debit account dto service.
 */
@Service
public class DebitAccountDTOServiceImpl implements IDebitAccountDTOService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebitAccountDTOServiceImpl.class);

    @Autowired
    private WebClient.Builder webClientBuilder;


    @Override
    public Mono<SavingAccount> getSavingAccount(String accountNumber) {
        Map<String, Object> params = new HashMap<>();
        LOGGER.info("Searching SavingAccount by: {}" + accountNumber);
        params.put("accountNumber", accountNumber);
        return webClientBuilder.baseUrl("http://SAVINGACCOUNT-SERVICE/api/savingAccount")
                .build()
                .get()
                .uri("/account/{accountNumber}", accountNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(SavingAccount.class))
                .switchIfEmpty(Mono.just(SavingAccount.builder().accountNumber(null).build()))
                .doOnNext(c -> LOGGER.info("SavingAccount Response: savingaccount={}", c.getAccountNumber()));
    }

    @Override
    public Flux<CurrentAccount> getCurrentAccount(String accountNumber) {
        Map<String, Object> params = new HashMap<>();
        LOGGER.info("Searching CurrentAccount by: {}" + accountNumber);
        params.put("accountNumber", accountNumber);
        return webClientBuilder.baseUrl("http://CURRENTACCOUNT-SERVICE/api/currentAccount")
                .build()
                .get()
                .uri("/account/{accountNumber}", accountNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(CurrentAccount.class))
                .switchIfEmpty(Mono.just(CurrentAccount.builder().accountNumber(null).build()))
                .doOnNext(c -> LOGGER.info("CurrentAccount Response: CurrentAccount={}", c.getAccountNumber()));
    }

    @Override
    public Mono<FixedTermAccount> getFixedTermAccount(String accountNumber) {
        Map<String, Object> params = new HashMap<>();
        LOGGER.info("Searching FixedTermAccount by: {}" + accountNumber);
        params.put("accountNumber", accountNumber);
        return webClientBuilder.baseUrl("http://FIXEDTERMACCOUNT-SERVICE/api/fixedTermAccound")
                .build()
                .get()
                .uri("/account{accountNumber}", accountNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(FixedTermAccount.class))
                .switchIfEmpty(Mono.just(FixedTermAccount.builder().accountNumber(null).build()))
                .doOnNext(c -> LOGGER.info("FixedTermAccount Response: FixedTermAccount={}", c.getAccountNumber()));
    }

    @Override
    public Mono<DebitAccountDTO> findByAccountNumber(String typeofdebit, String accountNumber) {
        Map<String, Object> params = new HashMap<String, Object>();
        LOGGER.info("initializing Debit query: " + typeofdebit);
        params.put("accountNumber", accountNumber);
        if (typeofdebit.equals("SAVING_ACCOUNT")) {
            return webClientBuilder.baseUrl("http://SAVINGACCOUNT-SERVICE/api/savingAccount")
                    .build()
                    .get()
                    .uri("/account/{accountNumber}", params)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchangeToMono(clientResponse -> clientResponse.bodyToMono(DebitAccountDTO.class))
                    .doOnNext(c -> LOGGER.info("Account Response: Account Amounth={}", c.getAmount()));
        }else if (typeofdebit.equals("CURRENT_ACCOUNT")) {
            return webClientBuilder.baseUrl("http://CURRENTACCOUNT-SERVICE/api/currentAccount")
                    .build()
                    .get()
                    .uri("/account/{accountNumber}", params)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchangeToMono(clientResponse -> clientResponse.bodyToMono(DebitAccountDTO.class))
                    .doOnNext(c -> LOGGER.info("CreditCard Response: CreditCard Amounth={}", c.getAmount()));
        } else if (typeofdebit.equals("FIXEDTERM_ACCOUNT")) {
            return webClientBuilder.baseUrl("http://FIXEDTERMACCOUNT-SERVICE/api/fixedTermAccount")
                    .build()
                    .get()
                    .uri("/account/{accountNumber}", params)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchangeToMono(clientResponse -> clientResponse.bodyToMono(DebitAccountDTO.class))
                    .doOnNext(c -> LOGGER.info("CreditCard Response: CreditCard Amounth={}", c.getAmount()));
        } else {
            LOGGER.info("Entra aquí fallido");
            return Mono.empty();
        }
    }


}
