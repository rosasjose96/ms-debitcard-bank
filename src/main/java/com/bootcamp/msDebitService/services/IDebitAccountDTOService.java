package com.bootcamp.msDebitService.services;


import com.bootcamp.msDebitService.models.dto.CurrentAccount;
import com.bootcamp.msDebitService.models.dto.DebitAccountDTO;

import com.bootcamp.msDebitService.models.dto.FixedTermAccount;
import com.bootcamp.msDebitService.models.dto.SavingAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    Mono<SavingAccount> getSavingAccount(String accountNumber);

    Flux<CurrentAccount> getCurrentAccount(String accountNumber);

    Mono<FixedTermAccount> getFixedTermAccount(String accountNumber);

}
