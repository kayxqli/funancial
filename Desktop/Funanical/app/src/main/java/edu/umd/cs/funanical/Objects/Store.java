package edu.umd.cs.funanical.Objects;

/**
 * Created by apple on 4/22/18.
 */

public class Store {
    private String name;
    private int price, pictureID;

    public Store(){

    }

    public Store(String name, int price, int pictureID){
        this.name = name;
        this.price = price;
        this.pictureID = pictureID;
    }

    public void setPrice(int price){
        this.price = price;
    }

    public int getPrice(){
        return price;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getPictureID() { return pictureID;}

    public void setPictureID(int pictureID){
        this.pictureID = pictureID;
    }
}
