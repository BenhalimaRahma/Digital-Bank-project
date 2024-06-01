package com.example.demo.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.AccountHistoryDTO;
import com.example.demo.dtos.AccountOperationDTO;
import com.example.demo.dtos.BankAccountDTO;
import com.example.demo.dtos.CurrentBankAccountDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.dtos.SavingBankAccountDTO;
import com.example.demo.entities.AccountOperation;
import com.example.demo.entities.BankAccount;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAccount;
import com.example.demo.enums.OperationType;
import com.example.demo.exceptions.BalanceNotSufficientException;
import com.example.demo.exceptions.BankAccountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.mapper.BankAccountMapperImpl;
import com.example.demo.repositorys.AccountOperationRepository;
import com.example.demo.repositorys.BankAccountRepository;
import com.example.demo.repositorys.CustomerRepository;

//import lombok.extern.slf4j.Slf4j;



@Service
@Transactional
//@AllArgsConstructor
//@Slf4j
public class BankAccountServiceImpl implements BankAccountService{
	
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private BankAccountRepository bankAccountRepository;
	@Autowired
	private AccountOperationRepository accountOperationRepository;
	@Autowired
	private BankAccountMapperImpl bankAccountMapper;
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
		log.info("saving new account");
		Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
		Customer  savedCustomer = customerRepository.save(customer);
		return bankAccountMapper.fromCustomer(savedCustomer);
	}
	
	@Override
	public List<CustomerDTO> listCustomers() {
		List<Customer> customers = customerRepository.findAll();
		//programmation fonctionnelle avec stream
		//map() => chaque object je le mappe en un autre objet
		List<CustomerDTO>customersDTO= customers.stream()
				.map(customer->bankAccountMapper.fromCustomer(customer)).toList();
		return customersDTO;
	}
	@Override
	public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
		Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("customer not found"));
		return bankAccountMapper.fromCustomer(customer);
	}
	@Override
	public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException{
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(()-> new BankAccountNotFoundException("Bank not found"));
		if(bankAccount instanceof SavingAccount) {
			SavingAccount savingAccount = (SavingAccount) bankAccount;
			return bankAccountMapper.fromSavingBankAccount(savingAccount);
		}else {
			CurrentAccount currentAccount =(CurrentAccount)bankAccount;
			return bankAccountMapper.fromCurrentBankAccount(currentAccount);
		}
	}
	@Override
	public void debit(String accountId, double amount, String description)throws BalanceNotSufficientException,BankAccountNotFoundException {
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(()-> new BankAccountNotFoundException("Bank not found"));
		if(bankAccount.getBalance()<amount)
			throw new BalanceNotSufficientException("Balance not sufficient");
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setDescription(description);
		accountOperation.setAmount(amount);
		accountOperation.setOperationDate(new Date());
		accountOperation.setBankAccount(bankAccount);
		accountOperationRepository.save(accountOperation);
		bankAccount.setBalance(bankAccount.getBalance()- amount);
		bankAccountRepository.save(bankAccount);
	}
	@Override
	public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException{
		BankAccount bankAccount = bankAccountRepository.findById(accountId)
				.orElseThrow(()-> new BankAccountNotFoundException("Bank not found"));
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.CREDIT);
		accountOperation.setDescription(description);
		accountOperation.setAmount(amount);
		accountOperation.setOperationDate(new Date());
		accountOperation.setBankAccount(bankAccount);
		accountOperationRepository.save(accountOperation);
		bankAccount.setBalance(bankAccount.getBalance()+ amount);
		bankAccountRepository.save(bankAccount);
		
	}
	@Override
	public void transfer(String accountIdSource, String accountIdDestination, double amount)
			throws BalanceNotSufficientException, BankAccountNotFoundException {
		debit(accountIdSource,amount,"Transfert to "+ accountIdDestination);
		credit(accountIdSource, amount,"Transfert from "+ accountIdSource);
		
	}
	@Override
	public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
			throws CustomerNotFoundException {
		log.info("saving new account");
		Customer customer = customerRepository.findById(customerId).orElse(null);
		if (customer == null)
			throw new CustomerNotFoundException("customer not found");
		CurrentAccount currentBankAccount = new CurrentAccount();
		
		currentBankAccount.setId(UUID.randomUUID().toString());
		currentBankAccount.setBalance(initialBalance);
		currentBankAccount.setCreatedAt(new Date());
		currentBankAccount.setOverDraft(overDraft);
		currentBankAccount.setCustomer(customer);
		CurrentAccount savedBankAcount = bankAccountRepository.save(currentBankAccount);
		return bankAccountMapper.fromCurrentBankAccount(savedBankAcount);
	}
	@Override
	public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
			throws CustomerNotFoundException {
		log.info("saving new account");
		Customer customer = customerRepository.findById(customerId).orElse(null);
		if (customer == null)
			throw new CustomerNotFoundException("customer not found");
		SavingAccount savingBankAccount = new SavingAccount();
		savingBankAccount.setId(UUID.randomUUID().toString());
		savingBankAccount.setBalance(initialBalance);
		savingBankAccount.setCreatedAt(new Date());
		savingBankAccount.setInterestRate(interestRate);
		savingBankAccount.setCustomer(customer);
		SavingAccount savedBankAcount = bankAccountRepository.save(savingBankAccount);
		return bankAccountMapper.fromSavingBankAccount(savedBankAcount);
	}
	@Override
	public List<BankAccountDTO> bankAccountList(){
		List<BankAccount> bankList= bankAccountRepository.findAll();
		List<BankAccountDTO> bankAccountDTOList = bankList.stream().map(bankAccount-> {
			if(bankAccount instanceof SavingAccount) {
				SavingAccount savingAccount = (SavingAccount) bankAccount;
				return bankAccountMapper.fromSavingBankAccount(savingAccount);
			}else {
				CurrentAccount currentAccount =(CurrentAccount)bankAccount;
				return bankAccountMapper.fromCurrentBankAccount(currentAccount);
			}
		}).collect(Collectors.toList());
		
		return bankAccountDTOList;
	}
	@Override
	public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
		log.info("saving new account");
		Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
		Customer  savedCustomer = customerRepository.save(customer);
		return bankAccountMapper.fromCustomer(savedCustomer);
	}
	@Override
	public void deleteCustomer(Long customerId) {
		customerRepository.deleteById(customerId);
	}
	@Override
	public List<AccountOperationDTO> accountHistory(String accountId){
		List<AccountOperation> accountOperation = accountOperationRepository.findByBankAccountId(accountId);
		List<AccountOperationDTO> operationListDTO = accountOperation.stream()
				.map(operation ->bankAccountMapper.fromAccountOperation(operation)).collect(Collectors.toList());
		return operationListDTO;
	}
	

	@Override
	public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
		BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
		if(bankAccount == null) 
			throw new BankAccountNotFoundException("Not found Bank Account");
		Page<AccountOperation>  accountOperationslist = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
		AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
		List<AccountOperationDTO> accountoperationListDTO = accountOperationslist.getContent().stream()
				.map(op -> bankAccountMapper.fromAccountOperation(op)).collect(Collectors.toList());
		accountHistoryDTO.setOperationDTO(accountoperationListDTO);
		accountHistoryDTO.setAccountId(accountId);
		accountHistoryDTO.setBalance(bankAccount.getBalance());
		accountHistoryDTO.setCurrentPages(page);
		accountHistoryDTO.setPageSize(size);
		accountHistoryDTO.setTotalPages(accountOperationslist.getTotalPages());
		return accountHistoryDTO;
	}

}
