package com.meritamerica.assignment4;

import java.util.Date;

public class DepositTransaction extends Transaction{

    private BankAccount targetAccount;
    private double amount;

    public DepositTransaction(){}

    public DepositTransaction(BankAccount targetAccount, double amount, Date transactionDate) {
        super(targetAccount, targetAccount, amount, new Date());
    }


    public DepositTransaction(long sourceAccount, long targetAccount, double amount, Date transactionDate) {
        super(targetAccount, targetAccount, amount, transactionDate);

    }
}
