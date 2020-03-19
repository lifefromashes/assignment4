package com.meritamerica.assignment4;
import java.text.ParseException;
import java.util.Arrays;

/**
 * Each instance of this class represents a bank customer
 *
 * customers can have (mostly) unlimited accounts, stored in
 * arrays in this object.
 *
 * @date 3/2/2020
 * @version 1.0
 *
 */

public class AccountHolder implements Comparable <AccountHolder>{

    private String firstName; // user's name info
    private String middleName;
    private String lastName;
    private String ssn; // used as customer's unique identifier

    // each kind of account is held in its own array
    // an index variable is used to track the number of each kind of account
    private CheckingAccount[] checkingAccounts;
    private int checkingAccountIndex;
    private SavingsAccount[] savingsAccounts;
    private int savingsAccountIndex;
    private CDAccount[] cdAccounts;
    private int cdAccountIndex;

    // constructors
    AccountHolder() { }

    // constructs acccountHolder with no accounts
    AccountHolder(String firstName, String middleName, String lastName, String ssn) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.ssn = ssn;

        this.checkingAccountIndex = 0;
        this.checkingAccounts = new CheckingAccount[0];
        this.savingsAccountIndex = 0;
        this.savingsAccounts = new SavingsAccount[0];
        this.cdAccountIndex = 0;
        this.cdAccounts = new CDAccount[0];
    }

    /**
     * Create a new Checking Account associated with the AccountHolder that
     * calls this method.
     *
     * @param openingBalance A double of how much money the account is opened with
     */
    public CheckingAccount addCheckingAccount(double openingBalance) throws ExceedsCombinedBalanceException{
//        if(getCheckingBalance() + getSavingsBalance() + openingBalance > 250000){
//            throw new ExceedsCombinedBalanceException("Unable to create account; Balance exceeds 250000");

        CheckingAccount newCheckingAccount = new CheckingAccount(openingBalance);
        return addCheckingAccount(newCheckingAccount);
    }

    /**
     * Associated a checking account with the AccountHolder that
     * calls this method.
     *
     * @param checkingAccount A CheckingAccount to attach to the accountHolder
     */
    public CheckingAccount addCheckingAccount(CheckingAccount checkingAccount) throws ExceedsCombinedBalanceException {
        if(getCheckingBalance() + getSavingsBalance() + checkingAccount.getBalance() >= 250000) {
            throw new ExceedsCombinedBalanceException("Unable to create new account, balance too high.");
            }


//        if (this.checkingAccounts > this.checkingAccountIndex.length){
//
//        }

        int currentArrayLimit = this.checkingAccountIndex;
        CheckingAccount[] temp = new CheckingAccount[currentArrayLimit + 1];

        for(int i=0; i<currentArrayLimit; i++) {
            temp[i] = this.checkingAccounts[i];
        }

        temp[currentArrayLimit] = checkingAccount;
        this.checkingAccountIndex ++;
        this.checkingAccounts = temp;

        return checkingAccount;

        //add deposit transaction w/opening balance
    }

    /**
     * Create a new Savings Account associated with the AccountHolder that
     * calls this method.
     *
     * @param openingBalance A double of how much money the account is opened with
     */
    public SavingsAccount addSavingsAccount(double openingBalance) throws ExceedsCombinedBalanceException{
//        if(getSavingsBalance() + getCheckingBalance() + openingBalance > 250000) {
//            throw new ExceedsCombinedBalanceException("Unable to create account. Balance exceeds 250000");
//        }
        SavingsAccount newSavingsAccount = new SavingsAccount(openingBalance);
        return addSavingsAccount(newSavingsAccount);

        //add deposit transaction w/openingBalance
        //MeritBank.processTransaction(
    }

    /**
     * Associated a savings account with the AccountHolder that
     * calls this method.
     *
     * @param savingsAccount A SavingsAccount to attach to the accountHolder
     */
    public SavingsAccount addSavingsAccount(SavingsAccount savingsAccount) throws ExceedsCombinedBalanceException {
        if(getCheckingBalance() + getSavingsBalance() + savingsAccount.getBalance()>= 250000){
            throw new ExceedsCombinedBalanceException("Unable to create new account, balance too high.");
        }

        int currentArrayLimit = this.savingsAccountIndex;
        SavingsAccount[] temp = new SavingsAccount[currentArrayLimit + 1];

        for(int i=0; i<currentArrayLimit; i++) {
            temp[i] = this.savingsAccounts[i];
        }

        temp[currentArrayLimit] = savingsAccount;
        this.savingsAccountIndex ++;
        this.savingsAccounts = temp;

        return savingsAccount;

        //add deposit transaction w/opening balance
    }

    /**
     * Create a new CD Account associated with the AccountHolder that
     * calls this method.
     *
     * @param offering A CDOffering that defines the interest and term
     * @param openingBalance A double of how much money the account is opened with
     */
    public CDAccount addCDAccount(CDOffering offering, double openingBalance) {
        if(offering == null) {
            System.out.println("Unable to find a CD offer.");
            return null;
        }
        CDAccount tempAccount = new CDAccount(openingBalance, offering.getInterestRate(), offering.getTerm());
        return addCDAccount(tempAccount);

        //add deposit transaction w/openingBalance
    }

    /**
     * Associated a CD account with the AccountHolder that
     * calls this method.
     *
     * @param cdAccount A SavingsAccount to attach to the accountHolder
     */
    public CDAccount addCDAccount(CDAccount cdAccount) {
        if(cdAccount == null) {
            System.out.println("Unable to find a CD offer.");
            return null;
        }

        int currentArrayLimit = this.cdAccountIndex;
        CDAccount[] temp = new CDAccount[currentArrayLimit + 1];

        for(int i=0; i<currentArrayLimit; i++) {
            temp[i] = this.cdAccounts[i];
        }

        temp[currentArrayLimit] = cdAccount;
        this.cdAccountIndex ++;
        this.cdAccounts = temp;

        return cdAccount;

        //add deposit transaction w/openingBalance
    }
    //end of account creation code

    public static AccountHolder readFromString(String accountData) throws ParseException {
        String firstNameString = "";
        String middleNameString = "";
        String lastNameString = "";
        String ssnString = "";
        int position = 1;

        for (char c: accountData.toCharArray()) {
            if(c == ',') { position ++; continue; }
            if(position == 1) { lastNameString += c; }
            if(position == 2) { middleNameString += c; }
            if(position == 3) { firstNameString += c; }
            if(position == 4) { ssnString += c; }
        }

        if(lastNameString == "" || firstNameString == "" || ssnString == "" || position != 4) {
            throw new ParseException(accountData, position);
        }

        AccountHolder newAccountHolder = new AccountHolder(firstNameString,
                middleNameString, lastNameString, ssnString);

        return newAccountHolder;

    }

    /**
     * Represents this object as a string of text to save in a file
     *
     * See the MeritBank.readFromFile method for information formatting
     * @return the created String
     */
    public String writeToString() {
        String s = "";
        s += this.lastName + "," + this.middleName + ",";
        s += this.firstName + "," + this.ssn;
        return s;
    }


    //begin getters and setters
    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String string) {
        this.firstName = string;
    }

    public String getMiddleName() {
        return this.middleName;
    }
    public void setMiddleName(String string) {
        this.middleName = string;
    }

    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String string) {
        this.lastName = string;
    }

    public String getSSN() {
        return this.ssn;
    }
    public void setSSN(String string) {
        this.ssn = string;
    }

    public CheckingAccount[] getCheckingAccounts() {
        return this.checkingAccounts;
    }

    public SavingsAccount[] getSavingsAccounts() {
        return this.savingsAccounts;
    }
    public CDAccount[] getCDAccounts() {
        return this.cdAccounts;
    }

    public int getNumberOfCheckingAccounts() {
        return this.checkingAccountIndex;
    }

    public int getNumberOfSavingsAccounts() {
        return this.savingsAccountIndex;
    }
    public int getNumberOfCDAccounts() {
        return this.cdAccountIndex;
    }
    //end getters and setters



    /**
     * Sums the total of all CheckingAccounts associated with the AccountHolder
     *
     * @return sum A double value of the combined checkingAccounts
     */
    public double getCheckingBalance() {
        double sum = 0;
        for(int i=0; i<this.checkingAccountIndex; i++) {
            sum += this.checkingAccounts[i].getBalance();
        }
        return sum;
    }

    /**
     * Sums the total of all SavingsAccounts associated with the AccountHolder
     *
     * @return sum A double value of the combined savingsAccounts
     */
    public double getSavingsBalance() {
        double sum = 0;
        for(int i=0; i<this.savingsAccountIndex; i++) {
            sum += this.savingsAccounts[i].getBalance();
        }
        return sum;
    }

    /**
     * Sums the total of all CDAccounts associated with the AccountHolder
     *
     * @return sum A double value of the combined cdAccounts
     */
    public double getCDAccountBalance() {
        double sum = 0;
        for(int i=0; i<this.cdAccountIndex; i++) {
            sum += this.cdAccounts[i].getBalance();
        }
        return sum;
    }

    /**
     * Sum the total of all accounts associates with the AccountHolder
     *
     * @return sum a double value of all accounts
     */
    public double getCombinedBalance() {
        double sum = getSavingsBalance();
        sum += getCheckingBalance();
        sum += getCDAccountBalance();
        return sum;
    }

    /**
     * Turns a string of text loaded from a file into an AccountHolder object
     *
     * See the MeritBank.readFromFile method for information formatting
     * @return the created object
     */




    /**
     * Account holders should be sorted by total balance
     *
     * It is worth nothing that the efficiency here is practically a crime
     */
    @Override
    public int compareTo(AccountHolder other) {
        int mySum = (int) getCombinedBalance();
        int otherSum = (int) other.getCombinedBalance();
        return mySum - otherSum;
    }

    /**
     * Pulls information from the associated accounts and returns
     * a String ready for outputting
     *
     * Obsolete, only returns info for the first checking and savings account
     * Ignores additional accounts and all other account types
     */
    @Override
    public String toString() {
        String string = "";
        string += "Name: " + this.firstName + " "+ this.middleName + " ";
        string += this.lastName +"\n";
        string += "SSN: " + this.ssn + "\n";
        string += checkingAccounts[0].toString()+ "\n";
        string += savingsAccounts[0].toString();
        return string;
    }
}

// constructs accountHolder with some starting accounts
//    AccountHolder(String firstName, String middleName, String lastName,
//                  String ssn, double checkingAccountOpeningBalance,
//                  double savingsAccountOpeningBalance){
//
//        this.firstName = firstName;
//        this.middleName = middleName;
//        this.lastName = lastName;
//        this.ssn = ssn;
//
//        this.checkingAccounts = new CheckingAccount[1];
//        this.checkingAccounts[0] = new CheckingAccount(checkingAccountOpeningBalance);
//        this.checkingAccountIndex = 1;
//
//        this.savingsAccounts = new SavingsAccount[1];
//        this.savingsAccounts[0] = new SavingsAccount(savingsAccountOpeningBalance);
//        this.savingsAccountIndex = 1;
//
//        this.cdAccounts = new CDAccount[1];
//        this.cdAccountIndex = 0;
//    }

