package com.example.astidio;

import java.util.HashMap;
import java.util.Map;

public class CurrentUser {
    static String id;
    static String email;
    static String name;
    static String role;
    static Map<Product, Integer> order;


    public static void initialization() {
        id = "";
        email = "";
        name = "";
        role = "";
        order = new HashMap<Product, Integer>();
    }
    public static void getUser(String e, String n,
                               String r, String i) {
        id = i;
        email = e;
        name = n;
        role = r;

    }
}