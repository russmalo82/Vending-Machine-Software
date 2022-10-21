package com.techelevator.view;

import com.techelevator.VendingMachineCLI;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.text.NumberFormat;

public class FeedMoneyTest {
    File inventory = new File("vendingmachine.csv");
    VendingMachineCLI vmcli = new VendingMachineCLI(new Menu(System.in, System.out));
    NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();

    @Test
    public void feedMoneyTest() {

        vmcli.feedMoney(1); // 1 dollar
        Assert.assertEquals("$1.00", moneyFormat.format(vmcli.currentMoney));
        vmcli.finishTransaction(); // clear currentMoney variable

        vmcli.feedMoney(2); // 2 dollars
        Assert.assertEquals("$2.00", moneyFormat.format(vmcli.currentMoney));
        vmcli.finishTransaction();

        vmcli.feedMoney(3); // 5 dollars
        Assert.assertEquals("$5.00", moneyFormat.format(vmcli.currentMoney));
        vmcli.finishTransaction();

        vmcli.feedMoney(4); // 10 dollars
        Assert.assertEquals("$10.00", moneyFormat.format(vmcli.currentMoney));
        vmcli.finishTransaction();
    }
}
