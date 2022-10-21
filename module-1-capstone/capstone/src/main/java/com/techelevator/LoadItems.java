package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadItems {

    public static List<Item> itemList = new ArrayList<>();

    public static List<Item> loadItems(File stock) {

        itemList = new ArrayList<>();

        try (Scanner fileReader = new Scanner(stock)) { // try with resources, so we can read from a given vending machine inventory file.

            while (fileReader.hasNextLine()) { // loops while there is another line in the given file

                String[] splitLine = fileReader.nextLine().split("[|]"); // delimits the line from the file by the pipe symbol

                switch(splitLine[3].toLowerCase()) { // switch case that looks at the type of item and adds it to the public itemList
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
                        System.err.println("Error reading type of Item from given file.");
                        break;
                }

            }
        } catch (FileNotFoundException e) { // if the given file isn't found, catch that exception.
            System.err.println(e.getLocalizedMessage());
        }

        return itemList;
    }
}
