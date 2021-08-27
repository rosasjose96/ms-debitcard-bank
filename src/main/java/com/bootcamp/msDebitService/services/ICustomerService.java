package com.bootcamp.msDebitService.services;


import com.bootcamp.msDebitService.models.dto.Customer;

import reactor.core.publisher.Mono;

/**
 * The interface Customer dto service.
 */
public interface ICustomerService {
    /**
     * Gets customer.
     *
     * @param customerIdentityNumber the customer identity number
     * @return the customer
     */
    public Mono<Customer> getCustomer(String customerIdentityNumber);
}
