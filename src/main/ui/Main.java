package ui;

import java.io.FileNotFoundException;


// Class Comment: Runs the non-graphical version of the CodeBank Storage App
// ATTRIBUTION: The UI is based on the UI found in the TellerAPP
// LINK TO CODE: https://github.students.cs.ubc.ca/CPSC210/TellerApp
// ATTRIBUTION: UI Modified based on code found in JsonSerializationDemo
// LINK TO CODE: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class Main {
    public static void main(String[] args) {
        try {
            new CodeStorageApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
