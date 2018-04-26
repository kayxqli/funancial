package edu.umd.cs.funanical.Objects;

/**
 * Created by apple on 4/22/18.
 */

public class Building {
    public int pictureId;

    public Building(){

    }

    public Building(int pictureId){
        this.pictureId = pictureId;
    }

    public void setPictureId(int pictureId){
        this.pictureId = pictureId;
    }

    public int getPictureId(){
        return pictureId;
    }
}
