package com.developer.jw;

/**
 * Created by JW on 2016-05-29.
 */
public class Korean {
    public String firstName;
    public String lastName;
    public String personality;
    public String bloodType;

    public Korean() {

    }

    public static void main(String[] args) {
        Korean boy = new Korean();

        boy.firstName = "Jae";
        boy.lastName = "Lee";
        boy.personality = "nice";
        boy.bloodType = "AB";
    }
}