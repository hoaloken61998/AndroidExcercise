package com.example.myapplication;

public class Contact {
    public String id;
    public String name;
    public String phone;
    public String email;

    public Contact() {
        // Default constructor required for calls to DataSnapshot.getValue(Contact.class)
    }

    public Contact(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}

