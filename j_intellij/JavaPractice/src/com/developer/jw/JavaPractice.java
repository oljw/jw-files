package com.developer.jw;

/**
 * Created by JW on 2016-05-29.
 */
public class JavaPractice {

    public enum Days { MON, TUE, WED}
    public JavaPractice() {

    }

    public static void main(String[] args) {
        for(Days d : Days.values());
        Days [] d2 = Days.values();
        System.out.println(d2[2]);
    }
}