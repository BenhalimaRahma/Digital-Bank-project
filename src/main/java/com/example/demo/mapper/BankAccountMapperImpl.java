package com.example.demo.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.AccountOperationDTO;
import com.example.demo.dtos.CurrentBankAccountDTO;
import com.example.demo.dtos.CustomerDTO;
import com.example.demo.dtos.SavingBankAccountDTO;
import com.example.demo.entities.AccountOperation;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.Customer;
import com.example.demo.entities.SavingAccount;

@Service
public class BankAccountMapperImpl {

	
	public CustomerDTO fromCustomer(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		BeanUtils.copyProperties(customer, customerDTO);//MapStruct ou BeanUtils=> framework mapper
		/*customerDTO.setName(customer.getName());
		customerDTO.setEmail(customer.getEmail());
		customerDTO.setId(customer.getId());*/
		
		return customerDTO;
	}
	public Customer fromCustomerDTO(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDTO, customer);
		return customer;
	}
	
	public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount) {
		
		SavingBankAccountDTO savingAccountDTO = new SavingBankAccountDTO();
		BeanUtils.copyProperties(savingAccount, savingAccountDTO);
		savingAccountDTO.setCustomer(fromCustomer(savingAccount.getCustomer()));
		savingAccountDTO.setType(savingAccount.getClass().getSimpleName());
		return savingAccountDTO;
	}
	public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingAccountDTO) {
		SavingAccount savingAccount = new SavingAccount();
		BeanUtils.copyProperties(savingAccountDTO, savingAccount);
		savingAccount.setCustomer(fromCustomerDTO(savingAccountDTO.getCustomer()));
		return savingAccount;
	}
	public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount) {
		CurrentBankAccountDTO currentAccountDTO = new CurrentBankAccountDTO();
		BeanUtils.copyProperties(currentAccount, currentAccountDTO);
		currentAccountDTO.setCustomer(fromCustomer(currentAccount.getCustomer()));
		currentAccountDTO.setType(currentAccount.getClass().getSimpleName());
		return currentAccountDTO;
	}
	public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentAccountDTO) {
		CurrentAccount currentAccount = new CurrentAccount();
		BeanUtils.copyProperties(currentAccountDTO, currentAccount);
		currentAccount.setCustomer(fromCustomerDTO(currentAccountDTO.getCustomer()));
		return currentAccount;
	}
	public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
		AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
		BeanUtils.copyProperties(accountOperation, accountOperationDTO);
		return accountOperationDTO;
				
	}
}
