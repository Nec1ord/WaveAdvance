package com.nikolaykul.waveadvance.data.properties;

public class Property {
    private String mName;
    private double mValue;
    private OnPropertyChangedCallback mCallback;

    public Property(String name, double value) {
        this(name, value, null);
    }

    public Property(String name, double value, OnPropertyChangedCallback callback) {
        mName = name;
        mValue = value;
        mCallback = callback;
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

    public void notifyValueChanged() {
        if (null != mCallback) mCallback.propertyChanged();
    }

}
