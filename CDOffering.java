package com.meritamerica.assignment4;
import java.text.ParseException;

/**
 * This class represents a CD Offering, the terms by which
 * a new CDAccount can be opened.
 *
 *
 * @date 3/5/2020
 *
 */
public class CDOffering {

    private int term;
    private double interestRate;

    public CDOffering(int term, double interestRate){
        this.term = term;
        this.interestRate = interestRate;
    }

    public int getTerm() {
        return this.term;
    }

    public double getInterestRate() {
        return this.interestRate;
    }


    /**
     * Turns a string of text loaded from a file into a CDOffering object
     *
     * See the MeritBank.readFromFile method for information formatting
     * @return the created object
     */
    public static CDOffering readFromString(String accountData) throws NumberFormatException {
        //try {
        String termString = "";
        String rateString = "";
        int position = 1;

        for (char c: accountData.toCharArray()) {
            if(c == ',') { position ++; continue; }
            if(position == 1) { termString += c; }
            if(position == 2) { rateString += c; }
        }

        if(termString == "" || rateString == "" || position != 2) {
            throw new NumberFormatException();
        }

        int term = Integer.parseInt(termString);
        double rate = Double.parseDouble(rateString);

        CDOffering newCDOffering = new CDOffering(term, rate);

        return newCDOffering;
        //} catch (ParseException e) {
        //	return null;
        //}
    }

    /**
     * Represents this object as a string of text to save in a file
     *
     * See the MeritBank.readFromFile method for information formatting
     * @return the created String
     */
    public String writeToString() {
        String s = "";
        s += this.term + "," + this.interestRate;
        return s;
    }
}


