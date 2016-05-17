package com.developer.jw;

public class Zoo {
    private String name = "Fluffy";
    {System.out.println("setting field");}
    public Zoo() {
        name = "Tiny";
        System.out.println("setting constructor");
    }

    public static void main(String[] args) {
        Zoo zoo = new Zoo();
        System.out.println(zoo.name);
    }
}
