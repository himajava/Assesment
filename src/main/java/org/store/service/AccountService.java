package org.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.store.entity.Transaction;
import org.store.model.Account;
import org.store.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    public List<Account> getAccounts(){
        List<org.store.entity.Account> accountList = accountRepository.findAll();
        List<org.store.model.Account> account = calculateRewards(accountList);
        return null;
    }

    private List<Account> calculateRewards(List<org.store.entity.Account> accountList) {
        List<Account> accounts = null;
        if(accountList != null && !accountList.isEmpty()){
            accounts = accountList.stream().map(acc-> rewards(acc)).collect(Collectors.toList());
        }
        return  accounts;
    }

    private Account rewards(org.store.entity.Account acc) {
        Account account = null;
        if(acc != null){
            List<Transaction> transactions = acc.getTransactions();
            if(transactions != null && !transactions.isEmpty()){
                List<Transaction> transactionList = transactions.stream().map(transaction -> {
                    int amount = transaction.getAmount();
                    int rewards = 0;
                    int aboveHundred = amount % 100;
                    rewards += (aboveHundred * 2);
                    int aboveFifty = amount / 50;
                    rewards += (aboveFifty * 1);
                    transaction.setRewards(rewards);
                    return transaction;
                }).collect(Collectors.toList());
                account = new Account();
                account.setTransactions(transactionList);
                account.setId(acc.getId());
                account.setName(acc.getName());
            }
        }
        return  account;
    }
}
