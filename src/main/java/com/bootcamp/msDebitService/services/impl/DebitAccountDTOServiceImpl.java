package com.bootcamp.msDebitService.services.impl;


import com.bootcamp.msDebitService.models.dto.*;
import com.bootcamp.msDebitService.repositories.DebitServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bootcamp.msDebitService.services.IDebitAccountDTOService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Debit account dto service.
 */
@Service
public class DebitAccountDTOServiceImpl implements IDebitAccountDTOService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebitAccountDTOServiceImpl.class);

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private DebitServiceRepository repository;


    @Override
    public Mono<Pasive> getSavingAccount(String accountNumber) {
        Map<String, Object> params = new HashMap<>();
        LOGGER.info("Searching SavingAccount by: {}" + accountNumber);
        params.put("accountNumber", accountNumber);
        return webClientBuilder.baseUrl("http://SAVINGACCOUNT-SERVICE/api/savingAccount")
                .build()
                .get()
                .uri("/account/{accountNumber}", params)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Pasive.class))
                .doOnNext(c -> LOGGER.info("SavingAccount Response: savingaccount={}", c.getId()));
    }

    @Override
    public Mono<Pasive> getCurrentAccount(String accountNumber) {
        Map<String, Object> params = new HashMap<>();
        LOGGER.info("Searching CurrentAccount by: {}" + accountNumber);
        params.put("accountNumber", accountNumber);
        return webClientBuilder.baseUrl("http://CURRENTACCOUNT-SERVICE/api/currentAccount")
                .build()
                .get()
                .uri("/account/{accountNumber}", params)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Pasive.class))
                .doOnNext(c -> LOGGER.info("CurrentAccount Response: CurrentAccount={}", c.getAccountNumber()));
    }

    @Override
    public Mono<Pasive> getFixedTermAccount(String accountNumber) {
        Map<String, Object> params = new HashMap<>();
        LOGGER.info("Searching FixedTermAccount by: {}" + accountNumber);
        params.put("accountNumber", accountNumber);
        return webClientBuilder.baseUrl("http://FIXEDTERMACCOUNT-SERVICE/api/fixedTermAccount")
                .build()
                .get()
                .uri("/account/{accountNumber}", params)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Pasive.class))
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
                    .doOnNext(c -> LOGGER.info("FixedTerm Response: FixedTerm Amounth={}", c.getAmount()));
        } else {
            LOGGER.info("Entra aquí fallido");
            return Mono.empty();
        }
    }

    @Override
    public Mono<DebitAccountDTO> getAccountAmount(String typeofdebit, String accountNumber) {
        if(typeofdebit.equals("SAVING_ACCOUNT")){
            return getSavingAccount(accountNumber).map(saving -> DebitAccountDTO
                    .builder().id(saving.getId()).accountNumber(saving.getAccountNumber())
                    .amount(saving.getAmount()).typeOfAccount(saving.getTypeOfAccount()).build());
        } else if(typeofdebit.equals("CURRENT_ACCOUNT")){
            return getCurrentAccount(accountNumber).map(current -> DebitAccountDTO
                    .builder().id(current.getId()).accountNumber(current.getAccountNumber())
                    .amount(current.getAmount()).typeOfAccount(current.getTypeOfAccount()).build());
        } else if(typeofdebit.equals("FIXEDTERM_ACCOUNT")){
            return getFixedTermAccount(accountNumber).map(fixedTerm -> DebitAccountDTO
                    .builder().id(fixedTerm.getId()).accountNumber(fixedTerm.getAccountNumber())
                    .amount(fixedTerm.getAmount()).typeOfAccount(fixedTerm.getTypeOfAccount()).build());
        } else return Mono.empty();
    }

    /**
     * Thi Method find the debitcard by its pan number, then iterate through its
     * associated accounts in search the amount of these. Finally, filter the accounts that
     * not have an amount bigger than request's amount.
     * @param pan
     * @param amount
     * @return Mono<Pasive>
     */
    @Override
    public Mono<Pasive> searchEspecificAccount(String pan, double amount, String passwd) {

        return repository.findByPan(pan).flatMap(debitcard -> {
            if( debitcard.getPassword().equals(passwd)) {
            Mono<List<Pasive>> accounts = Flux.fromIterable(debitcard.getAccounts())
                    .flatMapSequential(account ->
                      getAccountAmount(account.getTypeOfAccount(), account.getNumberOfAccount())
                            .map(accountValue -> Pasive.builder()
                                    .id(accountValue.getId())
                                    .typeOfAccount(accountValue.getTypeOfAccount())
                                    .accountNumber(accountValue.getAccountNumber())
                                    .amount(accountValue.getAmount()).build())

                ).collectList();
                return accounts;
                } else return Mono.error(new Throwable("The password is incorrect"));
                }
                ).flatMap(accounts -> {
                   List<Pasive> lista= accounts.stream()
                    .filter(account -> account.getAmount()>amount).collect(Collectors.toList());

//            if(lista.isEmpty()){
//                return Mono.empty();
//            } else return Mono.just(lista.get(0))
//                    .doOnNext(account -> LOGGER.info("la encontrada es : "
//                            +account.getId()));

                    return Mono.just(lista.stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Do not have an account with enough amount")));
        });
    }

}
