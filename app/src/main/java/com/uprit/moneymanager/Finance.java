package com.uprit.moneymanager;

import android.annotation.SuppressLint;

import java.util.Date;

public class Finance {

    private long rowId;
    private String title;
    private String price;
    private Date date;

    public Finance(long rowId, String title, String price, Date date) {
        this.rowId = rowId;
        this.title = title;
        this.price = price;
        this.date = date;
    }

    public Finance(String title, String price, Date date) {
        this.title = title;
        this.price = price;
        this.date = date;
    }

    long getRowId() {
        return rowId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @SuppressLint("DefaultLocale")
    String getDate(){
        return String.format("%td/%tb/%tY", this.date, this.date, this.date);
    }
    
    String getTanggal(){
        return String.format("%td",this.date);
    }

    String getBulan(){
        return String.format("%tb",this.date).toUpperCase();
    }

    String getTahun(){
        return String.format("%tY",this.date);
    }
}