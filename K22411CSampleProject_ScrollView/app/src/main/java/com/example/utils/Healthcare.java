package com.example.utils;

import android.app.Activity;

import com.example.myapplication.R;

public class Healthcare {
    public static BMIResult calculateBMI(double height, double weight, Activity context) {
        double bmi = weight / Math.pow(height, 2);
        BMIResult result = new BMIResult();
        result.setBMI(bmi);
        String des = "";
        if (bmi < 18.5) {
            des = context.getString(R.string.title_slim);
        } else if (bmi >= 18.5 && bmi < 24.9) {
            des = context.getString(R.string.title_normal);
        } else
            des = context.getString(R.string.title_fat);
        result.setDescription(des);
        return result;

    }
}
