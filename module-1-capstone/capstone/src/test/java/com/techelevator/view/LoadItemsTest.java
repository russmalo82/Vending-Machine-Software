package com.techelevator.view;

import com.techelevator.Item;
import com.techelevator.ItemType;
import com.techelevator.LoadItems;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class LoadItemsTest {
    private File inventory = new File("vendingmachine.csv");

    @Test
    public void loadItemsTest() {
        List<Item> itemListActual = LoadItems.loadItems(inventory); // calls previously created method for loading Items from a file

        List<Item> itemList = new ArrayList<>(); // List to hold our newly generated Items from the given file

        try (Scanner fileReader = new Scanner(inventory)) { // try with resources, so we can read from a given vending machine inventory file.

            while (fileReader.hasNextLine()) { // loops while there is another line in the given file

                String[] splitLine = fileReader.nextLine().split("[|]"); // delimits the line from the file by the pipe symbol

                switch (splitLine[3].toLowerCase()) {// switch case that looks at the type of item and adds it to the public itemList
                    case "drink":
                        //splitLine[0] is our Item's position
                        //splitLine[1] is the Item's name
                        //splitLine[2] is the Item's price, which is parsed and passed as a double.
                        //each item is created with the given info from the file and added to the publicly referencable list "itemList"
                        itemList.add(new Item(splitLine[0], splitLine[1], Double.parseDouble(splitLine[2]), ItemType.DRINK));
                        break;
                    case "chip":
                        itemList.add(new Item(splitLine[0], splitLine[1], Double.parseDouble(splitLine[2]), ItemType.CHIP));
                        break;
                    case "gum":
                        itemList.add(new Item(splitLine[0], splitLine[1], Double.parseDouble(splitLine[2]), ItemType.GUM));
                        break;
                    case "candy":
                        itemList.add(new Item(splitLine[0], splitLine[1], Double.parseDouble(splitLine[2]), ItemType.CANDY));
                        break;
                    default:
                        System.err.println("Something went wrong while reading .csv file");
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
        }

        for (int i = 0; i < itemList.size(); i++) {
            Assert.assertEquals(itemList.get(i).getName(), itemListActual.get(i).getName()); // asserts that all given names match
            Assert.assertEquals(itemList.get(i).getPrice(), itemListActual.get(i).getPrice(), 0.002); // asserts that all give prices match
            Assert.assertEquals(itemList.get(i).getPosition(), itemListActual.get(i).getPosition()); // asserts that all given positions match
            Assert.assertEquals(itemList.get(i).getType(), itemListActual.get(i).getType()); // asserts that all given ItemTypes match
        }
    }
}
