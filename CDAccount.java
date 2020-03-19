package com.meritamerica.assignment4;

import java.text.ParseException;
import java.util.Date;

public class CDAccount extends BankAccount {

    private int term;
    private Date startDate;

    public CDAccount(double balance, double interestRate, int term) {
        super(balance, interestRate, new Date(), MeritBank.getNextAccountNumber());
        this.term = term;
    }

    public CDAccount(double balance, double interestRate, int term, long accountNumber) {
        super(balance, interestRate, new Date(), accountNumber);
        this.term = term;
    }

    public CDAccount(double balance, double interestRate, Date date, long accountNumber, int term) {
        super(balance, interestRate, date, accountNumber);
        this.term = term;
    }

    public CDAccount(CDOffering offering, double balance) {
        super(balance, offering.getInterestRate(), new Date(), MeritBank.getNextAccountNumber());
        this.term = offering.getTerm();
    }

    public CDAccount(CDOffering offering, double balance, long accountNumber, Date date) {
        super(balance, offering.getInterestRate(), date, accountNumber);
        this.term = offering.getTerm();
    }

    public int getTerm() {
        return this.term;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * CD accounts should refuse all withdraws
     */
    @Override
    public boolean withdraw(double amount) throws ExceedsFraudSuspicionLimitException {

        throw new ExceedsFraudSuspicionLimitException("Unable to withdraw from CD Account.");
    }

    /**
     * CD accounts should refuse all deposits
     */
    @Override
    public boolean deposit(double amount) throws ExceedsFraudSuspicionLimitException {
        throw new ExceedsFraudSuspicionLimitException("Unable to deposit to CD account");
    }

    /**
     * Represents this object as a string of text to save in a file
     *
     * See the MeritBank.readFromFile method for information formatting
     * @return the created String
     */
    @Override
    public String writeToString() {
        String s = super.writeToString();
        s += "," + this.term;
        return s;
    }

    /**
     * Turns a string of text loaded from a file into a Bank Account object
     *
     * See the MeritBank.readFromFile method for information formatting
     * @return the created object
     */
    public static CDAccount readFromString(String accountData) throws ParseException {

        String termString = "";
        int iMax = accountData.length() - 1;
        for(int i = iMax; i >=0; i--) {
            char c = accountData.charAt(i);
            accountData = accountData.substring(0, accountData.length() - 1);
            if(c == ',') { break; }
            termString = c + termString;
        }
        int newTerm = Integer.parseInt(termString);

        CheckingAccount temp = CheckingAccount.readFromString(accountData);

        //MeritBank.setNextAccountNumber(temp.getAccountNumber());
        CDAccount tempCD = new CDAccount(temp.getBalance(),
                temp.getInterestRate(), temp.getOpenedOn(), temp.getAccountNumber(), newTerm);

        return tempCD;
    }

   }

