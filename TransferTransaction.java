package com.meritamerica.assignment4;

import java.util.Date;

public class TransferTransaction extends Transaction{

    public TransferTransaction(BankAccount sourceAccount, BankAccount targetAccount, double amount) {
        super(targetAccount, targetAccount, amount, new Date());
    }

    public TransferTransaction(long sourceAccount, long targetAccount, double amount, Date transactionDate) {
        super(targetAccount, targetAccount, amount, transactionDate);
    }

}
