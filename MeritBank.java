package com.meritamerica.assignment4;

import java.io.*;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * This class is the main point for storing bank information
 * All methods and variables should be static
 *
 * @date 3/5/2020
 */

public class MeritBank {

    private static AccountHolder[] accountHolders = new AccountHolder[100];
    private static CDOffering[] cdOfferings = new CDOffering[100];
    private static long nextAccountNumber = 123456;
    private static FraudQueue fraudQueue;

    /**
     * Call this method from the App to save a created account holder
     * to the bank's list
     *
     * @param accountHolder the AccountHolder object to save
     */
    public static void addAccountHolder(AccountHolder accountHolder) {
        //determine the size to make the account holder array
        int arraySize = accountHolders.length;

        AccountHolder[] temp = new AccountHolder[arraySize + 10];
        for (int i = 0; i < arraySize; i++) {
            temp[i] = accountHolders[i];
        }
        temp[arraySize] = accountHolder;
        accountHolders = temp;
    }

    public static BankAccount getBankAccount(long accountId) {
        //return null if account not found
        //loop through account holders to find checkingAccountID
        for (int i = 0; i < accountHolders.length; i++) {
           for (int j = 0; j < accountHolders[i].getCheckingAccounts().length; j++) {
                if (accountHolders[i].getCheckingAccounts()[j].getAccountNumber() == accountId) {
                    return accountHolders[i].getCheckingAccounts()[j];
                }
            }
            //loop through account holders to find SavingsAccountID
            for (int j = 0; j < accountHolders[i].getSavingsAccounts().length; j++) {
                if (accountHolders[i].getCheckingAccounts()[j].getAccountNumber() == accountId) {
                    return accountHolders[i].getSavingsAccounts()[j];
                }
            }
            //loop through account holders to find CDAccountID
            for (int j = 0; j < accountHolders[i].getCDAccounts().length; j++) {
                if (accountHolders[i].getCDAccounts()[j].getAccountNumber() == accountId) {
                    return accountHolders[i].getCDAccounts()[j];
                }
            }
        }
        return null;
    }

    public double recursiveFutureValue(double presentValue, int years, double interestRate) {
        // FV = PV(1+i)n

        if (years != 0) {
            double base = presentValue * (1 + interestRate); //or is it amount
            return base * recursiveFutureValue(presentValue, years - 1, interestRate);
        } else
            return -1;
    }

    /**
     * Calculates the value of an account with interest applied
     *
     * @param presentValue a double indicating the starting value of the account
     * @param interestRate a double indicating the interest rate to apply
     * @param term         an int indicating the number of years the interest will be applied for
     * @return a double of the projected account balance
     */
    static double futureValue(double presentValue, double interestRate, int term) {
        double futureValue = presentValue * (Math.pow(1 + presentValue, term));
        return futureValue;
    }

    public static boolean processTransaction(Transaction transaction) throws NegativeAmountException, ExceedsAvailableBalanceException,
            ExceedsFraudSuspicionLimitException {
        if (transaction.getSourceAccount() != transaction.getTargetAccount()) {
            if (transaction.getAmount() < 0) {
                throw new NegativeAmountException("Unable to process transaction. Must be positive amount");
            }
            if (transaction.getAmount() > transaction.getSourceAccount().getBalance()) {
                throw new ExceedsAvailableBalanceException("Exceeds available balance. Unable to process transaction.");
            }
        }

        if (transaction.getAmount() < 0 && transaction.getAmount() > transaction.getSourceAccount().getBalance()) {
            throw new ExceedsAvailableBalanceException("Exceeds available balance. Unable to process transaction.");
        }

        //the transaction exceeds 1000 then throw the exception for it to go to fraud team
        if (transaction.getAmount() > 1000) {
            //add transaction to fraudQueue
            fraudQueue.addTransaction(transaction);
            throw new ExceedsFraudSuspicionLimitException("Unable to process, possible fraud");
        }

        if (transaction.getSourceAccount() == transaction.getTargetAccount()) {
            if (transaction.getAmount() > 0) {
                transaction.getTargetAccount().deposit(transaction.getAmount());
                return true;
            }
            if (transaction.getAmount() < 0) {
                transaction.getTargetAccount().withdraw(transaction.getAmount());
                return false; //DOES THIS NEED TO BE TRUE? NICK HAS TRUE;
            }
        } else {
            transaction.getSourceAccount().withdraw(transaction.getAmount());
            transaction.getTargetAccount().deposit(transaction.getAmount());
            return true;
        }
        return false;
    }

    // begin getters
    public static AccountHolder[] getAccountHolders() {
        return accountHolders;
    }

    public static CDOffering[] getCDOfferings() {
        return cdOfferings;
    }

    public static FraudQueue getFraudQueue() {
        return fraudQueue;
    }
    // end getters

    /**
     * Method to find the best CD account for a customer
     * Because the assignment details did not specify any length of time
     * this only return the best interest rate, regardless of time
     *
     * @param depositAmount not used in calculation =(
     * @return the cdOffering with the best interest rate
     */
    static CDOffering getBestCDOffering(double depositAmount) {
        if (cdOfferings == null) {
            return null;
        }
        double bestValue = 0;
        int bestIndex = -1;
        for (int i = 0; i < cdOfferings.length; i++) {
            if (cdOfferings[i].getInterestRate() > bestValue) {
                bestValue = cdOfferings[i].getInterestRate();
                bestIndex = i;
            }
        }

        return cdOfferings[bestIndex];
    }

    /**
     * Method to find the 2nd best CD account for a customer
     * Because the assignment details did not specify any length of time
     * this only return the best interest rate, regardless of time
     * <p>
     * calls bestCDOffering and rejects object equal to that
     *
     * @param depositAmount not used in calculation =(
     * @return the cdOffering with the best interest rate
     */
    public static CDOffering getSecondBestCDOffering(double depositAmount) {
        if (cdOfferings == null) {
            return null;
        }
        CDOffering best = getBestCDOffering(depositAmount);

        double secondBestValue = 0;
        int secondBestIndex = -1;
        for (int i = 0; i < cdOfferings.length; i++) {
            if (cdOfferings[i].getInterestRate() > secondBestValue
                    && !best.equals(cdOfferings[i])) {
                secondBestValue = cdOfferings[i].getInterestRate();
                secondBestIndex = i;
            }
        }
        if (secondBestIndex == -1) {
            return null;
        }
        return cdOfferings[secondBestIndex];
    }

    /**
     * Erase all existing CDOfferings
     */
    public static void clearCDOfferings() {
        cdOfferings = null;
    }

    /**
     * Define which CDOfferings are offered
     *
     * @param offerings An array of CDOfferings to make available to the accountHolders
     */
    public static void setCDOfferings(CDOffering[] offerings) {
        //determine the size to make the offerings array
        int arraySize = 0;
        for (int i = 0; i < offerings.length; i++) {
            if (offerings[i] == null) {
                break;
            }
            arraySize++;
        }

        cdOfferings = new CDOffering[arraySize];
        for (int i = 0; i < arraySize; i++) {
            cdOfferings[i] = offerings[i];
        }
    }

    public static long getNextAccountNumber() {
        return nextAccountNumber;
    }

    public static void setNextAccountNumber(long accountNumber) {
        nextAccountNumber = accountNumber;
    }

    /**
     * total the value of all accounts held by bank's account holders
     *
     * @return sum A double value of the combined accounts
     */
    static double totalBalances() {
        double sum = 0;
        for (AccountHolder ah : accountHolders) {
            if (ah == null) {
                break;
            }

            for (CheckingAccount account : ah.getCheckingAccounts()) {
                try {
                    sum += account.getBalance();
                } catch (NullPointerException expected) {
                    //if account holder has less CheckingAccounts than other
                    //kinds this will catch. No additional handling needed
                }
            }
            for (SavingsAccount account : ah.getSavingsAccounts()) {
                try {
                    sum += account.getBalance();
                } catch (NullPointerException expected) {
                    //if account holder has less SavingsAccounts than other
                    //kinds this will catch. No additional handling needed
                }
            }
            for (CDAccount account : ah.getCDAccounts()) {
                try {
                    sum += account.getBalance();
                } catch (NullPointerException expected) {
                    //if account holder has less CDAccounts than other
                    //kinds this will catch. No additional handling needed
                }
            }
        }


        return sum;
    }


    /**
     * erase the current arrays and counters to their start point
     * <p>
     * call this before reading from a file
     */
    static void clearMemory() {
        accountHolders = new AccountHolder[100];
        cdOfferings = new CDOffering[100];
        fraudQueue = new FraudQueue();
    }


    /**
     * load saved information- customers, accounts, offerings, etc
     */
    public static boolean readFromFile(String fileName) {
        clearMemory();

        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(reader);

            AccountHolder loadedHolder = new AccountHolder();
            String line; // current line from the file

            line = bufferedReader.readLine(); // read account number
            MeritBank.setNextAccountNumber(Long.parseLong(line));
            System.out.println("Next account: " + line);


            line = bufferedReader.readLine(); // read number of cd offerings
            int totalCDO = Integer.parseInt(line);
            CDOffering[] loadedCDOfferings = new CDOffering[totalCDO];
            System.out.println("Number of CD offerings: " + line);

            for (int i = 0; i < totalCDO; i++) {
                line = bufferedReader.readLine(); // read cd offering
                loadedCDOfferings[i] = CDOffering.readFromString(line);
                System.out.println("CD offering: " + line);
            }
            setCDOfferings(loadedCDOfferings);

            line = bufferedReader.readLine(); // read number of account holders
            int totalAccountHolders = Integer.parseInt(line);
            System.out.println("Number of Account Holders: " + line);

            for (int i = 0; i < totalAccountHolders; i++) {
                line = bufferedReader.readLine(); // read account holder
                loadedHolder = AccountHolder.readFromString(line);
                addAccountHolder(loadedHolder);
                System.out.println("Account Holder: " + line);

                line = bufferedReader.readLine(); // read number of checking accounts
                int totalChecking = Integer.parseInt(line);
                System.out.println("Number of Checking Accounts: " + line);

                for (int j = 0; j < totalChecking; j++) {
                    line = bufferedReader.readLine(); // read checking accounts
                    CheckingAccount c = CheckingAccount.readFromString(line);
                    loadedHolder.addCheckingAccount(c);
                    System.out.println("Account: " + line);

                    line = bufferedReader.readLine(); //read num of transactions
                    int totalTransactions = Integer.parseInt(line);
                    System.out.println("Number of transactions: " + line);

                    for (int k = 0; k < totalTransactions; k++) {
                        line = bufferedReader.readLine();//read transaction info
                        Transaction t = Transaction.readFromString(line);

                        loadedHolder.getCheckingAccounts()[j].addTransaction(t);
                        System.out.println("Transaction: " + line);
                    }
                } //end of loading checking accounts


                line = bufferedReader.readLine(); // read number of savings accounts
                int totalSavings = Integer.parseInt(line);
                System.out.println("Number of Savings Accounts: " + line);

                for (int j = 0; j < totalSavings; j++) {
                    line = bufferedReader.readLine(); // read savings accounts
                    SavingsAccount s = SavingsAccount.readFromString(line);
                    loadedHolder.addSavingsAccount(s);
                    System.out.println("Account: " + line);

                    line = bufferedReader.readLine(); //read number of savings transaction
                    int totalTransactions = Integer.parseInt(line);
                    System.out.println("Number of savings transactions: " + line);

                    for (int k = 0; k < totalTransactions; k++) {
                        line = bufferedReader.readLine();//read transaction info
                        Transaction t = Transaction.readFromString(line);

                        loadedHolder.getSavingsAccounts()[j].addTransaction(t);
                        System.out.println("Transaction : " + line);
                    }
                } //end of load savings accounts

                line = bufferedReader.readLine(); // read number of cd accounts
                int totalCD = Integer.parseInt(line);
                System.out.println("Number of CD Accounts: " + line);

                for (int j = 0; j < totalCD; j++) {
                    line = bufferedReader.readLine(); // read cd accounts
                    CDAccount s = CDAccount.readFromString(line);
                    loadedHolder.addCDAccount(s);
                    System.out.println("Account: " + line);
                }
            } //end of cd Accounts
        //end of load account holders

            line = bufferedReader.readLine(); //reading num of pending possible fraud transactions
            int totalTransactions = Integer.parseInt(line);
            System.out.println("Number of pending fraud reviews: " + line);

            for (int i = 0; i < totalTransactions; i++) {
                line = bufferedReader.readLine(); //read fraud transactoin
                Transaction t = Transaction.readFromString(line);

                fraudQueue.addTransaction(t);
                System.out.println("Review Transaction: " + line);
            }

            //loop through all transactions exchanging ID numbers for account objects
            for (AccountHolder ah : accountHolders) {
                for(CheckingAccount chkAcc : ah.getCheckingAccounts()){
                    for(Transaction t : chkAcc.getTransactions()) {
                        t.findAccounts();
                    }
                }
                for(SavingsAccount savAcc : ah.getSavingsAccounts()) {
                    for(Transaction t : savAcc.getTransactions()) {
                        t.findAccounts();
                    }
                }
                for (CDAccount cd : ah.getCDAccounts()) {
                    for (Transaction t : cd.getTransactions()) {
                        t.findAccounts();
                    }
                }
            }

                bufferedReader.close();
                return true;
            } catch(Exception e){
                e.printStackTrace();

            }
            return false;
        }


        /**
         * Save current information in memory to a text file for future access
         *
         * @param fileName
         * @return true if successful
         */
        static boolean writeToFile (String fileName){
            try {
                String s = "";
                s += getNextAccountNumber() + "\n";
                s+= getCDOfferings() + "\n";

                for(int i = 0; i < getCDOfferings().length; i++) {
                    s += cdOfferings[i].writeToString() + "\n";
                }

                s += getAccountHolders().length + "\n";

                for (int i = 0; i < getAccountHolders().length; i++) {
                    s += accountHolders[i].writeToString() + "\n";

                    s += accountHolders[i].getCheckingAccounts().length + "\n";
                    for (int j = 0; j<accountHolders[i].getCheckingAccounts().length; j++){
                        s += accountHolders[i].getCheckingAccounts()[j].writeToString() + "\n";
                    }

                    s += accountHolders[i].getSavingsAccounts().length + "\n";
                    for (int j = 0; j < accountHolders[i].getSavingsAccounts().length; j++) {
                        s += accountHolders[i].getSavingsAccounts()[j].writeToString() + "\n";
                    }

                    s += accountHolders[i].getCDAccounts().length + "\n";
                    for(int j = 0; j < accountHolders[i].getCDAccounts().length; j++) {
                        s += accountHolders[i].getSavingsAccounts()[j].writeToString() + "\n";
                    }
                }
                File file = new File(fileName);
                file.createNewFile();

                FileWriter writer = new FileWriter(fileName, false);
                writer.write(fileName);
                writer.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        /**
         * Sorts based on combined value of all accounts
         * See compareTo in AccountHolder for more info
         *
         * @return the sorted array of AccountHolders
         */
        static AccountHolder[] sortAccountHolders () {
            Arrays.sort(accountHolders);
            return accountHolders;
        }


    }



