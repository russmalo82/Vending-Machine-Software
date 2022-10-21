package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    public static void writeToLog(File logFile, String message) { // writes the given message to the given file.
        try (PrintWriter logWriter = new PrintWriter(new FileOutputStream(logFile, true))) { // try with resources using a FileOutputStream so we can accurately append the file even after vending machine has been rebooted.

            Date date = new Date( ); // date variable
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a"); // setting date format for the log file
            String timeStamp = dateFormat.format(date); // storing the formatted date to String timeStamp

            logWriter.println(timeStamp + " " + message); // writing the message with the timeStamp

        } catch (FileNotFoundException e) { // if the user passes a nonexistent file
            System.err.println(e.getLocalizedMessage());
        }
    }
}
