package com.bootcamp.msDebitService.services.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bootcamp.msDebitService.models.dto.CustomerDTO;
import com.bootcamp.msDebitService.models.entities.DebitCard;
import com.bootcamp.msDebitService.repositories.DebitServiceRepository;
import com.bootcamp.msDebitService.services.IDebitCardService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class DebitServiceImpl implements IDebitCardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebitServiceImpl.class);

    @Autowired
    @Qualifier("client")
    private WebClient.Builder client;

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

}
