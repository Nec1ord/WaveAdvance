package com.nikolaykul.waveadvance.item;

public class PropertyItem {
    private String mName;
    private double mValue;

    public PropertyItem(String name, double value) {
        mName = name;
        mValue = value;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(double value) {
        mValue = value;
    }

}
