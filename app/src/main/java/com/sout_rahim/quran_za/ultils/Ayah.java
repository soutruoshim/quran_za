package com.sout_rahim.quran_za.ultils;

/**
 * Created by sout_rahim on 16/07/10.
 */
public class Ayah {
    private String suraID;
    private String ayahID;
    private String ayahText;
    private String ayahTextKhmer;
    private String ayahNormal;

    public Ayah(){}
    public Ayah(String suraID, String ayahID, String ayahText, String ayahTextKhmer, String ayahNormal) {
        this.suraID = suraID;
        this.ayahID = ayahID;
        this.ayahText = ayahText;
        this.ayahTextKhmer = ayahTextKhmer;
        this.ayahNormal = ayahNormal;
    }

    public void setSuraID(String suraID) {
        this.suraID = suraID;
    }

    public void setAyahID(String ayahID) {
        this.ayahID = ayahID;
    }

    public void setAyahText(String ayahText) {
        this.ayahText = ayahText;
    }

    public void setAyahTextKhmer(String ayahTextKhmer) {
        this.ayahTextKhmer = ayahTextKhmer;
    }

    public void setAyahNormal(String ayahNormal) {
        this.ayahNormal = ayahNormal;
    }

    public String getSuraID() {
        return suraID;
    }

    public String getAyahID() {
        return ayahID;
    }

    public String getAyahText() {
        return ayahText;
    }

    public String getAyahTextKhmer() {
        return ayahTextKhmer;
    }

    public String getAyahNormal() {
        return ayahNormal;
    }
}
