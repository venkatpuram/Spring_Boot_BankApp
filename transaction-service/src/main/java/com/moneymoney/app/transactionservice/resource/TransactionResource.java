package com.moneymoney.app.transactionservice.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.moneymoney.app.transactionservice.entity.Transaction;
import com.moneymoney.app.transactionservice.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionResource {

	@Autowired
	private RestTemplate template;
	
	@Autowired
	private TransactionService service;
	
	@PostMapping
	public ResponseEntity<Transaction> deposit(@RequestBody Transaction transaction)
	{
		ResponseEntity<Double> entity=template.getForEntity("http://localhost:9090/accounts/"+transaction.getAccountNumber()+"/balance",Double.class );
		
		double currentBalance=entity.getBody();
		double updatedBalance=service.deposit(transaction.getAccountNumber(),transaction.getTransactionDetails(),currentBalance,transaction.getAmount());
		template.put("http://localhost:9090/accounts/"+transaction.getAccountNumber()+"?currentBalance="+updatedBalance, null);
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<Transaction> withDraw(@RequestBody Transaction transaction)
	{
		ResponseEntity<Double> entity=template.getForEntity("http://localhost:9090/accounts/"+transaction.getAccountNumber()+"/balance",Double.class );
		
		double currentBalance=entity.getBody();
		double updatedBalance=service.withDraw(transaction.getAccountNumber(),transaction.getTransactionDetails(),currentBalance,transaction.getAmount());
		template.put("http://localhost:9090/accounts/"+transaction.getAccountNumber()+"?currentBalance="+updatedBalance, null);
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	@GetMapping("/statements")
	public ResponseEntity<CurrentDataSet> getStatement()
	{
		CurrentDataSet dataset=new CurrentDataSet();
		List<Transaction> transactions=service.getStatement();
		dataset.setTransactions(transactions);
		return new ResponseEntity<CurrentDataSet>(dataset,HttpStatus.OK);
	}
	
	
}
