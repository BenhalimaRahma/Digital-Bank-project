package com.example.demo.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.AccountOperation;


public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long>{

}
