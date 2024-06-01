package com.example.demo.exceptions;

public class CustomerNotFoundException extends Exception {// exception non surveillé

	public CustomerNotFoundException(String message) {
		super(message);
	}
}
