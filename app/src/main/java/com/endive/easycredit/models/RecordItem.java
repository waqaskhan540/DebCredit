package com.endive.easycredit.models;

import java.util.Date;

/**
 * Created by MWaqas on 12/31/2016.
 */

public class RecordItem {

    public double Debit;
    public double Credit;
    public String Description;
    public double Total;
    public String Date;

    public String getDate() {
        return Date;
    }

    public void setDate(String date){
        Date = date;
    }
    public double getTotal() {
        return Total;
    }

    public void setTotal(double amount){
        Total = amount;
    }
    public double getDebit() {
        return Debit;
    }

    public void setDebit(double debit) {
        Debit = debit;
    }

    public double getCredit() {
        return Credit;
    }

    public void setCredit(double credit) {
        Credit = credit;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
