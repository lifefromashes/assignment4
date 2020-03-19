package com.meritamerica.assignment4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This parent class will set the basic methods for each of the account
 * types.
 *
 * @date 3/9/2020
 */
public abstract class BankAccount {

    private double balance;
    private double interestRate;
    private long accountNumber;
    private Date accountOpenedOn;
    private ArrayList<Transaction> transactions;

    BankAccount(double balance, double interestRate) {
        this.balance = balance;
        this.interestRate = interestRate;
        this.accountOpenedOn = new Date();
        this.accountNumber = MeritBank.getNextAccountNumber();
        this.transactions = new ArrayList<Transaction>();
    }


    BankAccount(double balance, double interestRate, Date accountOpenedOn, long accountNumber) {
        this.balance = balance;
        this.interestRate = interestRate;
        this.accountOpenedOn = accountOpenedOn;
        this.accountNumber = accountNumber;
    }

    /**
     * Withdraw funds from the account
     * Rejects negative numbers and overdrafts
     *
     * @param amount double, the amount to withdraw
     * @return true if the transaction took place
     */
    public boolean withdraw(double amount) throws ExceedsAvailableBalanceException, NegativeAmountException, ExceedsFraudSuspicionLimitException {
        if (amount <= 0 || amount > this.balance) {
            throw new ExceedsAvailableBalanceException("Unable to process. Insufficient funds");
        } else if (amount < 0) {
            throw new NegativeAmountException("Unable to process. Amount must be above zero.");
        } else if (amount > 1000) {
            throw new ExceedsFraudSuspicionLimitException("Suspicious activity detected. Sent to fraud team.");
        }
        this.balance = this.balance - amount;
        return true;
    }

    /**
     * Add funds to the account
     * Rejects negative numbers
     *
     * @param amount double, the amount to deposit
     * @returns true if the transaction took place
     */
    public boolean deposit(double amount) throws NegativeAmountException{
        if (amount <= 0) {
            throw new NegativeAmountException("Amount must be greater than zero.");
        }

        this.balance = this.balance + amount;
        return true;
    }

    /**
     * Calculates the total value that will be in the account after interest
     * as accrued for a number of years
     *
     * @param years int, the time the account collects interest for
     * @return double, the projected total value of the account
     */
    public double futureValue(int years) {
        double multiplier = Math.pow(1 + this.interestRate, years);
        double futureValue = balance * multiplier;
        return futureValue;
    }

    /**
     * The test cases are calling this with no argument, is that a
     * mistake?
     * overloaded to handle
     */
    public double recursiveFutureValue(double presentValue, int years, double interestRate) {
        // FV = PV(1+i)n
        double base = presentValue * (1 + interestRate); //or is it amount
        if (years == 0) {
            return this.balance;
        }
        //return base * recursiveFutureValue(presentValue, years - 1, interestRate);
        return base * recursiveFutureValue(presentValue, years - 1, interestRate);
}

    public double futureValue() throws ExceedsFraudSuspicionLimitException {
        throw new ExceedsFraudSuspicionLimitException("Suspicious activity detected.");

    }

    /**
     * Output a general message for when a transaction fails
     */
    private void printError() {
        System.out.println("Error, unable to process transaction.");
    }

    public void addTransaction(Transaction t) {
        this.transactions.add(t);
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
        }


//    /**
//     * Turns a string of text loaded from a file into a Bank Account object
//     * <p>
//     * See the MeritBank.readFromFile method for information formatting
//     *
//     * @return the created object
//     */
//    public static BankAccount readFromString(String accountData) throws ParseException {
//        String accountNumberString = "";
//        String balanceString = "";
//        String rateString = "";
//        String dateString = "";
//        int position = 1;
//
//        for (char c : accountData.toCharArray()) {
//            if (c == ',') {
//                position++;
//                continue;
//            }
//            if (position == 1) {
//                accountNumberString += c;
//            }
//            if (position == 2) {
//                balanceString += c;
//            }
//            if (position == 3) {
//                rateString += c;
//            }
//            if (position == 4) {
//                dateString += c;
//            }
//        }
//
//        if (accountNumberString == "" || balanceString == "" ||
//                rateString == "" || dateString == "" || position != 4) {
//            throw new NumberFormatException();
//        }
//
//        long newAccountNumber = Long.parseLong(accountNumberString);
//        double newBalance = Double.parseDouble(balanceString);
//        double newRate = Double.parseDouble(rateString);
//        Date newDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
//
//        //MeritBank.setNextAccountNumber(newAccountNumber); //
//        BankAccount newAccount = new BankAccount(newBalance, newRate, newDate, newAccountNumber);
//
//        return newAccount;
//    }

    /**
     * Represents this object as a string of text to save in a file
     * <p>
     * See the MeritBank.readFromFile method for information formatting
     *
     * @return the created String
     */
    public String writeToString() {
        String s = "";
        s += this.accountNumber + ",";
        s += this.balance + ",";
        s += this.interestRate + ",";
        s += this.accountOpenedOn;
        return s;
    }

    // begin getters
    public long getAccountNumber() {
        return this.accountNumber;
    }

    public double getBalance() {
        return this.balance;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public Date getOpenedOn() {
        return this.accountOpenedOn;
    }
    // end getters
}


