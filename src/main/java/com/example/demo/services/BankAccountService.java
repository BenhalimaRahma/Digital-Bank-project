package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.AccountHistoryDTO;
import com.example.demo.dtos.AccountOperationDTO;
import com.example.demo.dtos.BankAccountDTO;
import com.example.demo.dtos.CurrentBankAccountDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.dtos.SavingBankAccountDTO;
import com.example.demo.entities.BankAccount;
import com.example.demo.exceptions.BalanceNotSufficientException;
import com.example.demo.exceptions.BankAccountNotFoundException;
import com.example.demo.exceptions.CustomerNotFoundException;

public interface BankAccountService {
	
	public CustomerDTO saveCustomer(CustomerDTO customerDTO) ;
	CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
	SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
	List<CustomerDTO> listCustomers();
	BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
	void debit(String accountId, double amount, String description)throws BalanceNotSufficientException, BankAccountNotFoundException;
	void credit(String accountId, double amount, String description)throws BankAccountNotFoundException;
	void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceNotSufficientException, BankAccountNotFoundException;
	List<BankAccountDTO> bankAccountList();
	CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
	CustomerDTO updateCustomer(CustomerDTO customerDTO);
	
	void deleteCustomer(Long customerId);
	List<AccountOperationDTO> accountHistory(String accountId);
	public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
	
}
