package com.example.astidio;

public class CurrentUser {
    static int id;
    static String email;
    static String name;
    static String password;
    static String role;


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
