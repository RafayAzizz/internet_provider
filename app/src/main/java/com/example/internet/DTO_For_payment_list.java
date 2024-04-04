package com.example.internet;

public class DTO_For_payment_list {
    public DTO_For_payment_list(String month, String amount) {
        Month = month;
        Amount = amount;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    String Month;
    String Amount;
}
