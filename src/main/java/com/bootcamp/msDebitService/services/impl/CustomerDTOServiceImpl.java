package com.bootcamp.msDebitService.services.impl;


import com.bootcamp.msDebitService.models.dto.Customer;
import com.bootcamp.msDebitService.services.ICustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Customer dto service.
 */
@Service
public class CustomerDTOServiceImpl implements ICustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDTOServiceImpl.class);

    @Autowired
    private WebClient.Builder webClientBuilder;


    @Override
    public Mono<Customer> getCustomer(String customerIdentityNumber){
        Map<String, Object> params = new HashMap<String,Object>();
        LOGGER.info("initializing client query");
        params.put("customerIdentityNumber",customerIdentityNumber);
        return webClientBuilder
                .baseUrl("http://CUSTOMER-SERVICE/customer")
                .build()
                .get()
                .uri("/findCustomerCredit/{customerIdentityNumber}",customerIdentityNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Customer.class))
                .doOnNext(c -> LOGGER.info("Customer Response: Customer={}", c.getName()));
    }
    
}
