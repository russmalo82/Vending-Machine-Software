package com.techelevator;

import java.text.NumberFormat;

public class Item {

    private String position;
    private String name;
    private double price;
    private ItemType type;
    private int amount = 5; // Set this to five because the machine always restocks at 5 on initialization.

    public Item(String position, String name, double price, ItemType type) { //required constructor

        this.position = position;
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public boolean isAvailable() { // simple boolean call to check if the private integer "amount" is above 0
        return this.amount > 0;
    }

    public String displayItem() { // when called returns a string that consists of the Position of the Item, the Name of the Item, the Price of the Item, and the Amount of Items left in the machine.
        NumberFormat priceFormat = NumberFormat.getCurrencyInstance();

        return this.position + " " + this.name + " " + priceFormat.format(this.price) + " Remaining: " + this.amount;
    }

    public void itemPurchased() { // when called subtracts 1 from the current amount of this item, unless it's 0 or less, then we print "SOLD OUT"

        if (this.amount > 0)
            this.amount -= 1;
        else
            System.out.println("SOLD OUT");
    }


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public int getAmount() { return this.amount; };
}
