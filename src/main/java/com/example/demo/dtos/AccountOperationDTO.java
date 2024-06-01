package com.example.demo.dtos;

import java.util.Date;

import com.example.demo.enums.OperationType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class AccountOperationDTO {
	private Long id;
	private Date operationDate;
	private double amount;
	private String description;
	private OperationType type;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getOperationDate() {
		return operationDate;
	}
	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public OperationType getType() {
		return type;
	}
	public void setType(OperationType type) {
		this.type = type;
	}
	
}
