package com.techelevator.view;

import com.techelevator.Item;
import com.techelevator.LoadItems;
import com.techelevator.VendingMachineCLI;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;

public class ItemPurchaseTest {
    File inventory = new File("vendingmachine.csv");
    VendingMachineCLI vmcli = new VendingMachineCLI(new Menu(System.in, System.out));
    NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();


    //Test whether making a purchase works correctly.
    @Test
    public void purchaseTest() {
        //If items appear to be loading incorrectly, double check that LoadItemsTest is still passing
        List<Item> itemList = LoadItems.loadItems(inventory); // loads all the items from the given file for testing.
        Item testItem = null;
        for (Item item : itemList) {
            if (item.getPosition().equals("B3")) {
                testItem = item;
            }
        }

        assert testItem != null;
        vmcli.feedMoney(4); // 10 dollars
        vmcli.chooseProduct(testItem.getPosition());
        vmcli.finishTransaction(); // doesn't really need to be called but, just in case.

        String updatedInv = vmcli.displayInvItems();
        String[] updatedArr = updatedInv.split("[\n]"); // remove possible new lines

        String expected = testItem.getPosition() + " " + testItem.getName() + " " + moneyFormat.format(testItem.getPrice()) + " Remaining: 4"; // can change what the remaining "total" is to check that test works properly
        String actual = null; // set to null for initialization
        for (String str : updatedArr) {
            if (str.contains(testItem.getPosition())) {
                actual = str;
            }
        }
        Assert.assertEquals(expected, actual);
    }
}
