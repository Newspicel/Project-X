package de.newspicel.projekt.projekt.api.home;

import java.util.ArrayList;

public class Homes {
    private ArrayList<Home> homes;

    public Homes() {
    }

    public Homes(ArrayList<Home> homes) {
        this.homes = homes;
    }

    public ArrayList<Home> getHomes() {
        return homes;
    }

    public void setHomes(ArrayList<Home> homes) {
        this.homes = homes;
    }
}
