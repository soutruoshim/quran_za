package com.sout_rahim.quran_za.ultils;

/**
 * Created by sout_rahim on 16/07/10.
 */
public class Surah {
    private int id;
    private String name;

    public Surah(int id,String name) {
        this.id=id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
