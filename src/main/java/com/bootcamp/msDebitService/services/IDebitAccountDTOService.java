package com.bootcamp.msDebitService.services;


import com.bootcamp.msDebitService.models.dto.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * The interface Debit account dto service.
 */
public interface IDebitAccountDTOService {
    /**
     * Find by account number mono.
     *
     * @param typeofdebit   the typeofdebit
     * @param accountNumber the account number
     * @return the mono
     */
    Mono<DebitAccountDTO> findByAccountNumber(String typeofdebit, String accountNumber);

    Mono<Pasive> getSavingAccount(String accountNumber);

    Mono<Pasive> getCurrentAccount(String accountNumber);

    Mono<Pasive> getFixedTermAccount(String accountNumber);

    Mono<DebitAccountDTO> getAccountAmount(String typeofdebit, String accountNumber);

    Mono<Pasive> searchEspecificAccount(String pan, double amount, String passwd);

}
