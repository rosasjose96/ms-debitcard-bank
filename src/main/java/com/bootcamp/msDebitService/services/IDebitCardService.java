package com.bootcamp.msDebitService.services;

import com.bootcamp.msDebitService.models.entities.DebitCard;

import reactor.core.publisher.Mono;

public interface IDebitCardService extends ICRUDService<DebitCard,String> {

    Mono<DebitCard> findByPan(String pan);

    Mono<DebitCard> findDebitCardByCustomer_CustomerIdentityNumber(String customerIdentityNumber);
}
