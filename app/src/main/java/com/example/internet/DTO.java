package com.example.internet;




public class DTO {

    private String name;



    private  String Area;

    private String Amount;
    private boolean isPaid;

    public DTO(String name, String Amount, Boolean check,String area) {
        this.name = name;
        Area = area;
        this.Amount = Amount;
        this.isPaid = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
    public String getArea() {return Area;}

    public void setArea(String area) {Area = area;}


    public boolean isPaid() {
        return isPaid;
    }
}