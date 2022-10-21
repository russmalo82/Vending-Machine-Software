package com.techelevator.view;

import com.techelevator.VendingMachineCLI;
import org.junit.Assert;
import org.junit.Test;

public class ChangeReturnTest {
    VendingMachineCLI vmcli = new VendingMachineCLI(new Menu(System.in, System.out));

    @Test
    public void changeReturnTest() {
        vmcli.feedMoney(3); // 5 dollars
        String exp = "2000"; // expected here is 20 quarters, 0 dimes, 0 nickles "20 0 0"
        String actual = vmcli.finishTransaction();
        String[] actArr = actual.split("[A-z:\n ]"); // filters out all letters, capitalized and lowercase, a colon, all newlines, and all spaces, leaving just numbers

        StringBuilder strBuilder = new StringBuilder();
        for (String str : actArr) { // takes the strings inside the actArr, and appends them to the string builder to get the number of quarters, dimes, and nickles.
            strBuilder.append(str);
        }

        Assert.assertEquals(exp, strBuilder.toString()); // assert that both the strings are the same
    }
}
