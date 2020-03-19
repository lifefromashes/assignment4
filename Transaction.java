package com.meritamerica.assignment4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Transaction {
    BankAccount sourceAccount;
    BankAccount targetAccount;
    long sourceAccountId;
    long targetAccountId;
    double amount;
    Date transactionDate;

    public Transaction() {
    }

    public Transaction(long sourceAccount, long targetAccount, double amount, Date transactionDate) {
        this.sourceAccountId = sourceAccount;
        this.targetAccountId = targetAccount;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }


    public Transaction(BankAccount sourceAccount, BankAccount targetAccount, double amount, Date transactionDate) {
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public void findAccounts() {
        this.targetAccount = MeritBank.getBankAccount(this.targetAccountId);
        if (this.sourceAccountId == -1) {
            this.sourceAccount = targetAccount;
        } else {
            this.sourceAccount = MeritBank.getBankAccount(this.sourceAccountId);
        }
    }


    public static Transaction readFromString(String transactionDataString) throws ParseException {
        Transaction transaction; // = new DepositTransaction();

        String sourceAccountString = "";
        String targetAccountString = "";
        String amountString = "";
        String dateString = "";
        int position = 1;

        for (char c : transactionDataString.toCharArray()) {
            if (c == ',') {
                position++;
                continue;
            }
            if (position == 1) {
                sourceAccountString += c;
            }
            if (position == 2) {
                targetAccountString += c;
            }
            if (position == 3) {
                amountString += c;
            }
            if (position == 4) {
                dateString += c;
            }

            if (sourceAccountString == "" || targetAccountString == "" || amountString == "" || dateString == "" || position != 4) {
                throw new NumberFormatException();
            }

        }

        long sourceAccount = Long.parseLong(sourceAccountString);
        long targetAccount = Long.parseLong(targetAccountString);
        double amount = Double.parseDouble(amountString);
        Date newDate = new SimpleDateFormat("dd/MM/yyy").parse(dateString);

        if(sourceAccount == -1) {
            if(amount >= 0) {
                transaction = new DepositTransaction(sourceAccount, targetAccount, amount, newDate);
            } else {
                transaction = new WithdrawTransaction(sourceAccount,
                        targetAccount,
                        amount,
                        newDate);
            }
        } else {
            transaction = new TransferTransaction(sourceAccount, targetAccount, amount, newDate);
        }
        return transaction;
    }

    public String writeToString() {
        String s = "";
        if(this.sourceAccount == this.targetAccount) {
            s += "-1";
        } else {
            s += this.sourceAccountId;
        }

        s += this.targetAccountId + ",";
        s += this.amount + ",";
        s += this.transactionDate;
        return s;
    }

    public BankAccount getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(BankAccount sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public BankAccount getTargetAccount() {
        return this.targetAccount;
    }

    public void setTargetAccount(BankAccount targetAccount) {
        this.targetAccount = targetAccount;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return this.transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }


}
