package com.nikolaykul.waveadvance.data.properties;

public class Property {
    private String mName;
    private double mValue;

    public Property(String name, double value) {
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
