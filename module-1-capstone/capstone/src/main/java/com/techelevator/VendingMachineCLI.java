package com.techelevator;

import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class VendingMachineCLI {

    private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
    private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
    private static final String MAIN_MENU_OPTION_EXIT = "Exit";
    private static final String MAIN_MENU_OPTION_SALES_REPORT = "";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_SALES_REPORT};

    private static final File LOG_FILE = new File("Log.txt");
    private static final File VENDING_MACHINE = new File("vendingmachine.csv");

    private static Map<String, Integer> purchases = new HashMap<>(); // put all purchases into a HashMap to that is used when writing the Sales Report to list all sales.

    public static NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();

    private Menu menu;

    public VendingMachineCLI(Menu menu) {
        this.menu = menu;
    }

    public void run() {
        while (true) {
            String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

            switch (choice) {
                case MAIN_MENU_OPTION_DISPLAY_ITEMS:
                    // display vending machine items
                    System.out.println("\n"+displayInvItems());
                    break;
                case MAIN_MENU_OPTION_PURCHASE:
                    // do purchase
                    purchaseMenu();
                    break;
                case MAIN_MENU_OPTION_EXIT:
                    System.exit(0);
                case MAIN_MENU_OPTION_SALES_REPORT:
                    printSalesReport();
                    break;
            }
        }
    }

    public static void main(String[] args) {
        Menu menu = new Menu(System.in, System.out);
        VendingMachineCLI cli = new VendingMachineCLI(menu);
        LoadItems.loadItems(VENDING_MACHINE);
        cli.run();
    }

    public String displayInvItems() {

        StringBuilder stringBuilder = new StringBuilder();

        for (Item item : LoadItems.itemList) { // looks through all available Items that were generated from the given file and appends them with their data into the stringBuilder to be written as a string.k
            stringBuilder.append(item.displayItem()).append("\n");
        }

        return stringBuilder.toString();
    }

    public void printSalesReport() {

        Date date = new Date( );
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh.mm.ss a");
        String timeStamp = dateFormat.format(date);
        double dayOut = 0.00;

        try (PrintWriter fileOut = new PrintWriter("SALES REPORT "+timeStamp)) { // try with resources that creates a file called "SALES REPORT ___" where the ___ is the current timestamp.

            for (Map.Entry<String, Integer> entry : purchases.entrySet()) { // loop through the HashMap "purchases" which contains all purchases that were executed since the machine was turned on.

                String msg = entry.getKey() + "|" + entry.getValue(); // message to be printed to the terminal and written to the sales report file
                fileOut.println(msg);
                System.out.println(msg);

                for (Item item : LoadItems.itemList) { // for loop to get the price of items that were purchased

                    if (entry.getKey().equals(item.getName())) {
                        dayOut += entry.getValue() * item.getPrice(); // adding the price of a purchased item to the dayOut total
                    }
                }
            }

            fileOut.println("\nTOTAL SALES: " + moneyFormat.format(dayOut)); // writing the total sale at the end of the sales report file
            System.out.println("\nTOTAL SALES: " + moneyFormat.format(dayOut)); // printing the total sale to the terminal

        } catch (FileNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    public double currentMoney = 0;
    private boolean purchasing = false;
    private String message = "";
    public void purchaseMenu() {

        Scanner input = new Scanner(System.in);

        System.out.print("\n(1) Feed Money \n(2) Select Product\n(3) Finish Transaction\n\nCurrent Money Provided: ");
        System.out.println(moneyFormat.format(currentMoney)); // writing the formatted amount of money currently in the machine
        System.out.print(">>> ");

        int choice = Integer.parseInt(input.nextLine());
        purchasing = true;

        switch (choice) {

            case 1:
                //feed money
                System.out.print("(1) $1\n(2) $2\n(3) $5\n(4) $10\nCurrent Money Provided: ");
                System.out.println(moneyFormat.format(currentMoney));
                System.out.print(">>> ");
                int billChoice = Integer.parseInt(input.nextLine()); // grab user's input and parse the integer

                feedMoney(billChoice);
                break;

            case 2:
                //select product
                System.out.println(displayInvItems());
                System.out.print("Select product: ");
                String chosenProduct = input.nextLine();

                System.out.println(chooseProduct(chosenProduct));
                break;

            case 3:
                //finish transaction
                System.out.println(finishTransaction());
                break;

            default:
                System.out.println("Incorrect input");
                break;
        }

        if (purchasing) purchaseMenu();
        Logger.writeToLog(LOG_FILE, message);
    }

    public String finishTransaction() {

        message = "";

        purchasing = false;
        int quarters, dimes, nickles;
        message = "GIVE CHANGE: " + moneyFormat.format(currentMoney) + " " + moneyFormat.format(0.00);

        quarters = (int) Math.floor(currentMoney / .25); // gets the number of quarters by dividing the current money amount by .25 (the value of a quarter)
        currentMoney -= quarters * .25; // subtracts the amount of change removed by pulling the quarters from the current amount of money

        dimes = (int) Math.floor(currentMoney / .10); // gets the number of dimes by dividing the current money amount by .10 (the value of a dime)
        currentMoney -= dimes * .10; // subtracts the amount of change removed by pulling the dimes from the current amount of money

        nickles = (int) Math.floor(currentMoney / .05); // gets the number nickles by diving the current money about by .05 (the value of a nickle)
        currentMoney -= nickles * .05; // subtracts the amount of change removed by pulling the nickles from the current amount of money

        // returns a string containing all the coins that were returned.
        return "\nDispensing change:\nQuarters: " + quarters + "\nDimes: " + dimes + "\nNickles: " + nickles;
    }

    public String chooseProduct(String chosenProduct) {

        message = "";
        String noise = "";
        String itemInfo = "";
        boolean itemFound = false;

        for (Item item : LoadItems.itemList) { // loops through all items that were loaded from the given vending machine stock file
            if (item.getPosition().equals(chosenProduct.toUpperCase())) { // pushes the user's string input to upper case to match with any available item positions.

                itemFound = true;
                if (item.isAvailable()) { // check if the item is even available

                    message = item.getName() + " " + item.getPosition() + " " + moneyFormat.format(currentMoney) + " " + moneyFormat.format(currentMoney - item.getPrice());
                    Logger.writeToLog(LOG_FILE, message); // writes the user's selected item purchase to the log file

                    if (currentMoney - item.getPrice() < 0) {
                        System.out.println("Insufficient funds for item. Please insert more money.");
                        break;
                    }
                    else {
                        currentMoney -= item.getPrice();
                    } // subtract the price of the item from the current total money in the machine or request more money if not enough
                    item.itemPurchased(); // call for the item's amount to be reduced by 1

                    itemInfo = item.getName() + " " + moneyFormat.format(item.getPrice()) + " \nRemaining: " + moneyFormat.format(currentMoney);
                    // (above) builds a string containing the name of the item, it's formatted price, and the formatted Remaining currentMoney that is stored in the machine.
                    switch (item.getType()) { // switch that checks the ItemType and sets the noise variable to the correct noise string

                        case CHIP:
                            noise = "Crunch Crunch, Yum!";
                            break;

                        case CANDY:
                            noise = "Munch Much, Yum!";
                            break;

                        case DRINK:
                            noise = "Glug Glug, Yum!";
                            break;

                        case GUM:
                            noise = "Chew Chew, Yum!";
                            break;
                    }
                    // putting item purchase record into purchases Map, but checking if the item already exists, and if it does, adding one to the current total sales, else setting as one
                    purchases.put(item.getName(), purchases.get(item.getName()) != null ? purchases.get(item.getName()) + 1 : 1);

                } else {
                    System.err.println("This item is sold out.");
                }
            }
        }
        if (!itemFound) {
            System.err.println("Selected item doesn't exist!");
        }
        // returns the string containing the information sent to the LogFile and the noise that the item makes
        return "\nDispensing item...\n" + itemInfo + "\n" + noise;
    }

    public void feedMoney(int billChoice) {

        message = "";
        switch (billChoice) {

            case 1: // adds one dollar to the machine (currentMoney)
                message = "FEED MONEY: " + moneyFormat.format(currentMoney) + " " + moneyFormat.format(currentMoney + 1d);
                currentMoney += 1.00;
                break;

            case 2: // adds two dollars to the machine (currentMoney)
                message = "FEED MONEY: " + moneyFormat.format(currentMoney) + " " + moneyFormat.format(currentMoney + 2d);
                currentMoney += 2.00;
                break;

            case 3: // adds five dollars to the machine (currentMoney)
                message = "FEED MONEY: " + moneyFormat.format(currentMoney) + " " + moneyFormat.format(currentMoney + 5d);
                currentMoney += 5.00;
                break;

            case 4: // adds ten dollars to the machine (currentMoney)
                message = "FEED MONEY: " + moneyFormat.format(currentMoney) + " " + moneyFormat.format(currentMoney + 10d);
                currentMoney += 10.00;
                break;

            default: // the user input a number greater than five or less than 1
                System.out.println("Input accepted bill");
                break;
        }
    }
}
