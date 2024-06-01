package com.example.demo;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.BankAccountDTO;
import com.example.demo.dtos.CurrentBankAccountDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.dtos.SavingBankAccountDTO;
import com.example.demo.entities.AccountOperation;
import com.example.demo.entities.BankAccount;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAccount;
import com.example.demo.enums.AccountStatus;
import com.example.demo.enums.OperationType;
import com.example.demo.exceptions.BalanceNotSufficientException;
import com.example.demo.exceptions.BankAccountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.repositorys.AccountOperationRepository;
import com.example.demo.repositorys.BankAccountRepository;
import com.example.demo.repositorys.CustomerRepository;
import com.example.demo.services.BankAccountService;
import com.example.demo.services.BankService;


@SpringBootApplication
public class DigitalBankBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankBackendApplication.class, args);
	}
	@Bean
	@Transactional // ces opérations s'éxécute em meme temps
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
			return args -> {
				Stream.of("Khaled","Sofienne","Safa").forEach(name->{
					CustomerDTO customer = new CustomerDTO();
					customer.setName(name);
					customer.setEmail(name+"gmail.com");
					bankAccountService.saveCustomer(customer);
					
				});
				bankAccountService.listCustomers().forEach(customer->{
					try {
						bankAccountService.saveCurrentBankAccount(Math.random()*9000, 9000,customer.getId());
						bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5, customer.getId());
						
					} catch (CustomerNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				List<BankAccountDTO> listBank = bankAccountService.bankAccountList();
				for (BankAccountDTO bankAccount: listBank) {
					for (int i=0; i<10; i++) {
					String accountId;
					if (bankAccount instanceof SavingBankAccountDTO)
						accountId = ((SavingBankAccountDTO)bankAccount).getId();
					else 
						accountId = ((CurrentBankAccountDTO) bankAccount).getId();
					bankAccountService.credit(accountId, 1000+Math.random()*120000, "credit");
					bankAccountService.debit(accountId, 1000+Math.random()*9000, "debit");
					}
				}
				
			};
	}
	
	
	
	//@Bean
	//@Transactional // ces opérations s'éxécute em meme temps
	CommandLineRunner start( CustomerRepository customerRepository,
			BankAccountRepository bankAccountReposotory,
			AccountOperationRepository accountOperationRepository) {
		return args -> {
			Stream.of("Rahma","Khaled","Adam", "Eya", "Amel").forEach(name->{
				Customer customer = new Customer()	;
				customer.setName(name);
				customer.setEmail( name + "@gmail.com");
				customerRepository.save(customer);
				
			});
			customerRepository.findAll().forEach(cust -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*10000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setOverDraft(9000);
				bankAccountReposotory.save(currentAccount);
				
				
				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*90000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setInterestRate(5.5);
				bankAccountReposotory.save(savingAccount);
			});	
			bankAccountReposotory.findAll().forEach(acc->{
				for (int i = 0; i < 10; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random()*12000);
					accountOperation.setType(Math.random()> 0.5? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);
					
				}
				
			});
		};
	}

}
