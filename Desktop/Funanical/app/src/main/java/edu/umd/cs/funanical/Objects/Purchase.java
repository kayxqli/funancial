package edu.umd.cs.funanical.Objects;

/**
 * Created by apple on 4/23/18.
 */

public class Purchase {
    private String name, date;
    private Double amount;

    public Purchase(){

    }

    public Purchase(String name, String date, Double amount){
        this.name = name;
        this.date = date;
        this.amount = amount;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public double getAmount(){
        return amount;
    }

    public void setAmount(Double amount){
        this.amount = amount;
    }

}
