package com.example.demo.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String>{

}
