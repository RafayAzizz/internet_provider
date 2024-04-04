package com.example.internet;

public class DTO_Expance {
    String date , expance,amount,docID;

    public DTO_Expance(String date, String expance, String amount, String docID) {
        this.date = date;
        this.expance = expance;
        this.amount = amount;
        this.docID = docID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpance() {
        return expance;
    }

    public void setExpance(String expance) {
        this.expance = expance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }
}
