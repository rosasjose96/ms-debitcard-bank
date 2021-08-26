package com.bootcamp.msDebitService.services;

import com.bootcamp.msDebitService.models.dto.CustomerDTO;
import com.bootcamp.msDebitService.models.entities.DebitCard;

import reactor.core.publisher.Mono;

public interface IDebitCardService extends ICRUDService<DebitCard,String> {

    public Mono<DebitCard> findByPan(String pan);
}
