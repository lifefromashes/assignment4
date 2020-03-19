package com.meritamerica.assignment4;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Each instance of this class represents a checking account
 * Every checking account must be related to a customer
 *
 * @date 3/9/2020
 *
 */

public class CheckingAccount extends BankAccount{

    static final double INTEREST_RATE = .0001;

    // constructor
    public CheckingAccount (double openingBalance) {
        super(openingBalance, INTEREST_RATE);
    }

    public CheckingAccount (BankAccount bankAccount) {
        super(bankAccount.getBalance(), bankAccount.getInterestRate(), bankAccount.getOpenedOn(), bankAccount.getAccountNumber());
    }

    public CheckingAccount(double balance, double interestRate, Date openedOn, long accountNumber){
        super(balance, interestRate, openedOn, accountNumber);
    }

    /**
     * Turns a string of text loaded from a file into a Bank Account object
     *
     * See the MeritBank.readFromFile method for information formatting
     * @return the created object
     */
    public static CheckingAccount readFromString(String accountData) throws ParseException {
        String accountNumberString = "";
        String balanceString = "";
        String interestRateString = "";
        String dateString = "";
        int position = 1;

        for (char c : accountData.toCharArray()) {
            if (position == ',') {
                position++;
                continue;
            }
            if (position == 1) {
                accountNumberString += c;
            }
            if (position == 2) {
                balanceString += c;
            }
            if (position == 3) {
                interestRateString += c;
            }
            if (position == 4) {
                dateString += c;
            }

            if (accountNumberString == "" || balanceString == "" || interestRateString == ""
                    || dateString == "" || position != 4) {
                throw new NumberFormatException();
            }
        }

            long newAccountNumber = Long.parseLong(accountNumberString);
            double newBalance = Double.parseDouble(balanceString);
            double newRate = Double.parseDouble(interestRateString);
            Date newDate = new SimpleDateFormat("dd/MM/yyy").parse(dateString);

            CheckingAccount newChkAccount = new CheckingAccount(newBalance, newRate, newDate, newAccountNumber);
            return newChkAccount;

    }

        //return new CheckingAccount( BankAccount.readFromString(accountData) );


    /**
     * Condenses account info into a String for output
     *
     * @return a String of the account information, ready for outputting
     */
    @Override
    public String toString() {
        DecimalFormat format = new DecimalFormat("0.00");
        DecimalFormat other = new DecimalFormat("0.0000");
        String string = "";
        string += "Checking Account Balance: $";
        string += format.format(getBalance()) + "\n";
        string += "Checking Account Interest Rate: ";
        string += other.format(getInterestRate()) + "\n";
        string += "Checking Account Balance in 3 years: $";
        string += format.format(futureValue(3));

        return string;
    }

}

