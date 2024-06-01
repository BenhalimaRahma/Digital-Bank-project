package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.BankAccount;
import com.example.demo.entities.CurrentAccount;
import com.example.demo.entities.SavingAccount;
import com.example.demo.repositorys.BankAccountRepository;

@Service
@Transactional
public class BankService {

	@Autowired
	BankAccountRepository bankAccountReposotory;
	public void consulter() {
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
	}
}
