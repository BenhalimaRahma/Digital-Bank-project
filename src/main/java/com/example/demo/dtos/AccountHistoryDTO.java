package com.example.demo.dtos;

import java.util.List;

public class AccountHistoryDTO {

	private String accountId;
	private double balance;
	private int currentPages;
	private int totalPages;
	private int PageSize;
	private List<AccountOperationDTO> operationDTO;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getCurrentPages() {
		return currentPages;
	}
	public void setCurrentPages(int currentPages) {
		this.currentPages = currentPages;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getPageSize() {
		return PageSize;
	}
	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}
	public List<AccountOperationDTO> getOperationDTO() {
		return operationDTO;
	}
	public void setOperationDTO(List<AccountOperationDTO> operationDTO) {
		this.operationDTO = operationDTO;
	}
	
}
