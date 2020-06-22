package com.bobona.apri.visual.ui;

public class RelativeValue {

    float value;
    float minValue;

    // Assumes the string is formatted like "min:value" or "value"
    public RelativeValue(String str) {
        String[] parts = str.split(":");
        if (str.length() == 0) {
            return;
        }
        value = Float.parseFloat(parts[parts.length - 1]);
        if (parts.length > 1) {
            minValue = Float.parseFloat(parts[0]);
        }
    }

    public float getValue(float total) {
        return Math.max(total * value, minValue);
    }
}
