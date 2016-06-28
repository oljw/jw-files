package com.developer.jw;

public class Japanese {
    private String firstName;
    private String lastName;
    private String personality;
    private String bloodType;
    private String pet;
    private String breakfast;

    public static void main(String[] args) {
        Japanese girl = new Japanese();

        girl.firstName = "Miki";
        girl.lastName = "Okuyama";
        girl.personality = "mean";
        girl.bloodType = "B";
        girl.pet ="Carry";

        System.out.println("This Japanese girl's name is " + girl.firstName + " " + girl.lastName +
                ", and her personality is " + girl.personality + ", and her blood type is " + girl. bloodType +
                ". Her pet is " + girl.pet);
    }
}

