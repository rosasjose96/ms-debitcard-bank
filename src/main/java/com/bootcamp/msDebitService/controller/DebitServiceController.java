package com.bootcamp.msDebitService.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bootcamp.msDebitService.models.dto.AccountsDTO;
import com.bootcamp.msDebitService.models.dto.DebitAccountDTO;
import com.bootcamp.msDebitService.models.entities.DebitCard;
import com.bootcamp.msDebitService.services.IDebitAccountDTOService;
import com.bootcamp.msDebitService.services.IDebitCardService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping()
public class DebitServiceController {


	 private static final Logger LOGGER = LoggerFactory.getLogger(DebitServiceController.class);
		@Autowired
		private IDebitCardService debitCardService;
		@Autowired
		private IDebitAccountDTOService debitAccountDTOService;
	
		@GetMapping()
		public Flux<DebitCard> getAllDebitCard() {
			
			Flux<DebitCard> debitCards = debitCardService.findAll();
			return debitCards;
		}
		
		@PostMapping()
		public Mono<DebitCard> saveDebitCard(@RequestBody DebitCard debitCard) {
			debitCard.getAccounts().forEach(a-> a.setCreateAt(new Date()));
			
			Mono<DebitCard> debitCardCreate = debitCardService.create(debitCard);
			return debitCardCreate;
		
		}
		
		@PostMapping("/payProduct")
		public Mono<DebitCard> payProductDebitCard(@RequestBody DebitCard debitCard, double amountOfDebit) {
			
			List<Mono<DebitAccountDTO>> accounts = new ArrayList<>();
			
			debitCard.getAccounts().forEach(acc->{
				accounts.add(acc.getOrder()-1 ,debitAccountDTOService.
					findByAccountNumber(acc.getTypeOfAccount(),acc.getNumberOfAccount()).flatMap((a)-> Mono.just(a)));
			});
			Flux<DebitAccountDTO> debitAccounts =Flux.concat(accounts);
			Mono<Double>  amountInAccount=debitAccounts.flatMap((d)->Mono.just(d.getAmount())).reduce(0.0, Double::sum) ;
			LOGGER.info("amountInAccount");
			if (amountInAccount.block() < amountOfDebit) {
				LOGGER.info("amountInAccount");
			}else {
				
			}
			return null;
			
		}
		
		
		
		@GetMapping("/getBalance/{pan}")
		public Mono<DebitAccountDTO> getBalance(@PathVariable(name="pan",required= true) String pan ) {
			
			 Mono<List<AccountsDTO>> accounts=debitCardService.findByPan(pan).flatMap(a ->	
			 Mono.just(	a.getAccounts().stream().filter(c->c.getOrder()<2).map(asa->asa).collect(Collectors.toList()))) ;
			 AccountsDTO mainAccount =	 accounts.block().get(0);
			 
		return	  debitAccountDTOService.
				findByAccountNumber(  mainAccount.getTypeOfAccount(), mainAccount.getNumberOfAccount());
		}


		@DeleteMapping("/{pan}")
		public Mono<Void> deleteByID(@PathVariable(name="pan",required= true) String pan) {
		
		return	debitCardService.findByPan(pan).flatMap(a->debitCardService.delete(a) );
			
		}
		@GetMapping("findByPan/{pan}")
		public Mono<DebitCard> findByID(@PathVariable(name="pan",required= true) String pan) {
		
		return	debitCardService.findByPan(pan);
			
			}
		}
	


