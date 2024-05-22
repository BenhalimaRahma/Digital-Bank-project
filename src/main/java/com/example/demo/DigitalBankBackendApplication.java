package com.example.demo;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.AccountOperation;
import com.example.demo.entities.BankAccount;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAccount;
import com.example.demo.enums.AccountStatus;
import com.example.demo.enums.OperationType;
import com.example.demo.repositorys.AccountOperationRepository;
import com.example.demo.repositorys.BankAccountRepository;
import com.example.demo.repositorys.CustomerRepository;


@SpringBootApplication
public class DigitalBankBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankBackendApplication.class, args);
	}
	@Bean
	@Transactional // ces opérations s'éxécute em meme temps
	CommandLineRunner commandLineRunner(BankAccountRepository bankAccountReposotory) {
			return args -> {
				BankAccount bankAcount = bankAccountReposotory.findById("211b1d96-5a97-4232-96de-6e11730f9079").get();
				if(bankAcount != null) {
					System.out.println("********************************");
					System.out.println(bankAcount.getId());
					System.out.println(bankAcount.getBalance());
					System.out.println(bankAcount.getStatus());
					System.out.println(bankAcount.getCreatedAt());
					if (bankAcount instanceof SavingAccount) {
						System.out.println("Rate=>" + ((SavingAccount) bankAcount).getInterestRate());
						
					}else if(bankAcount instanceof CurrentAccount) {
						System.out.println("Over Draft=>" + ((CurrentAccount) bankAcount).getOverDraft());
					}
					bankAcount.getAccountOperration().forEach(op ->{
						System.out.println(op.getType() + "\t" + op.getOperationDate() + "\t" + op.getAmount());
						
					});
					
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
