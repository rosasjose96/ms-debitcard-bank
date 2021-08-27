package com.bootcamp.msDebitService.services.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.msDebitService.models.entities.DebitCard;
import com.bootcamp.msDebitService.repositories.DebitServiceRepository;
import com.bootcamp.msDebitService.services.IDebitCardService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DebitCardServiceImpl implements IDebitCardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebitCardServiceImpl.class);

    @Autowired
    private DebitServiceRepository repository;

    @Override
    public Mono<DebitCard> create(DebitCard o) {
        return repository.save(o);
    }

    @Override
    public Flux<DebitCard> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<DebitCard> findById(String s) {
        return repository.findById(s);
    }

    @Override
    public Mono<DebitCard> update(DebitCard o) {
        return repository.save(o);
    }

    @Override
    public Mono<Void> delete(DebitCard o) {
        return repository.delete(o);
    }

    @Override
    public Mono<DebitCard> findByPan(String pan) {
        return repository.findByPan(pan);
    }

    @Override
    public Mono<DebitCard> findDebitCardByCustomer_CustomerIdentityNumber(String customerIdentityNumber) {
        return repository.findDebitCardByCustomer_CustomerIdentityNumber(customerIdentityNumber);
    }

}
