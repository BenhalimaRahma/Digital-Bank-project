package com.example.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.AccountHistoryDTO;
import com.example.demo.dtos.AccountOperationDTO;
import com.example.demo.dtos.BankAccountDTO;
import com.example.demo.exceptions.BankAccountNotFoundException;
import com.example.demo.services.BankAccountService;

@RestController
public class BankAccountRestController {
	
	@Autowired
	private BankAccountService bankAccountService;

	public BankAccountRestController(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}
	@GetMapping("/accounts/{accountId}")
	public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
		return bankAccountService.getBankAccount(accountId);
	}
	
	@GetMapping("/accounts") 
	public List<BankAccountDTO> listAccounts(){
		return bankAccountService.bankAccountList();
	}
	@GetMapping("/accounts/{accountId}/operations")
	public List<AccountOperationDTO> accountHistory(@PathVariable String accountId){
		
		return bankAccountService.accountHistory(accountId);
	}
	@GetMapping("/accounts/{accountId}/pageOperations")
	public AccountHistoryDTO getAccountHistory(
			@PathVariable String accountId,
			@RequestParam(name="page",defaultValue = "0") int page,
			@RequestParam(name="size",defaultValue = "5") int size) throws BankAccountNotFoundException{
		
		return bankAccountService.getAccountHistory(accountId, page, size);
	}
	

}
