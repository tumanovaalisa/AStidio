package com.example.astidio;

import java.util.Map;

public class CurrentUser {
    static int id;
    static String email;
    static String name;
    static String password;
    static String role;
    static Map<Product, Integer> order;


    public static void initialization() {
        email = "";
        name = "";
        password = "";
        role = "";
    }
    public static void getUser(String e, String n, String r) {
        email = e;
        name = n;
        role = r;
    }
}