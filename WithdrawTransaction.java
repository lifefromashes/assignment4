package com.meritamerica.assignment4;

import java.util.Date;

public class WithdrawTransaction extends Transaction{

     public WithdrawTransaction(BankAccount targetAccount, double amount){
        super(targetAccount,
                targetAccount,
                amount,
                new Date());
    }

    public WithdrawTransaction( long sourceAccount, long targetAccount, double amount, Date transactionDate){
        super(targetAccount, targetAccount, amount, transactionDate);
    }
}


