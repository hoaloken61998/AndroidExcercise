package com.example.models;

import com.example.myapplication.R;

import java.util.ArrayList;

public class listProduct {
    ArrayList<Product> products;

    public listProduct() {
        products = new ArrayList<>();
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void generate_sample_dataset() {
        Product p1 = new Product(1, "Coca Cola", 100, 10.0, R.mipmap.coca);
        Product p2 = new Product(2, "Pepsi", 120, 9.5, R.mipmap.pepsi);
        Product p3 = new Product(3, "7Up", 90, 8.0, R.mipmap.sevenup);
        Product p4 = new Product(4, "Fanta", 85, 8.5, R.mipmap.fanta);
        Product p5 = new Product(5, "Sprite", 95, 9.0, R.mipmap.sprite);
        products.add(p1);
        products.add(p2);
        products.add(p3);
        products.add(p4);
        products.add(p5);
    }
}

