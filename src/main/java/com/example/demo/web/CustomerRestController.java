package com.example.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.CustomerDTO;
import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.repositorys.CustomerRepository;
import com.example.demo.services.BankAccountService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")//authorise ts les domaines *, n'importe quel application et domaine peut envoyer requete Http ver le back-end
public class CustomerRestController {
	@Autowired
	private BankAccountService bankAccountService;
	@Autowired
	private CustomerRepository customerRepository;
	@GetMapping("/customers") 
	public List<CustomerDTO> customers(){
		//utilistation du pattern DTO
		return bankAccountService.listCustomers();
	}
	@GetMapping("/customers/{id}")
	public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
		return bankAccountService.getCustomer(customerId);
		
	}
	@PostMapping("/customers")
	public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
		return bankAccountService.saveCustomer(customerDTO);
	}
	@PutMapping("customers/{customerId}")
	public CustomerDTO upadteCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO) {
		customerDTO.setId(customerId);
		return bankAccountService.updateCustomer(customerDTO);
	}
	@DeleteMapping("/customers/{id}")
	public void deleteCustomer(@PathVariable(name = "id") Long customerId) {
		bankAccountService.deleteCustomer(customerId);
	}

}
