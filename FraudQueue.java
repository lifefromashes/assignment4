package com.meritamerica.assignment4;

import java.util.ArrayList;
import java.util.List;

public class FraudQueue {

    ArrayList<Transaction> transactions;

    public FraudQueue( ) {
        transactions = new ArrayList<Transaction>();

    }

    public void addTransaction(Transaction t) {
        this.transactions.add(t);


    }

    public Transaction getTransaction() {
        Transaction t = transactions.get(0);
        if (t != null) {
            transactions.remove(0);
        }
        return t;
    }



}

