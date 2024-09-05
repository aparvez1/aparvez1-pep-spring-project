package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account){
        return accountRepository.save(account);
    }

    public Optional<Account> getAccountById(Integer id){
        return accountRepository.findById(id);
    }

    public Optional<Account> getAccountByUsername(String username){
        return accountRepository.findByUsername(username);
    }

    public List<Account> getAllAccount(){
        return accountRepository.findAll();
    }

    

}
