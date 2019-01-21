package com.moneymoney.app.accountservice.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.moneymoney.app.accountservice.entity.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, Integer>{

}
