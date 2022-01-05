package ui;


import model.CodeBank;
import model.CodeSnippet;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//Code storage Application
public class CodeStorageApp {
    private static final String JSON_STORE = "./data/codeBank.json";
    private Scanner input;
    private CodeBank userCodeBank;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: Runs the code storage application
    public CodeStorageApp() throws FileNotFoundException {
        runCodeStorage();
    }

    // MODIFIES: this
    // EFFECTS: Processes user input from the main menu
    private void runCodeStorage() {
        boolean keepRunning = true;
        String command;

        init();

        while (keepRunning) {
            displayMainMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepRunning = false;
            } else {
                processMainMenuCommands(command);
            }

        }

        System.out.println("\nGoodBye!");
    }

    // MODIFIES: this
    // EFFECTS: Processes user input from the code snippets menu
    private void codeSnippetsMenu() {
        String command;
        boolean stayOnCurrentMenu = true;
        while (stayOnCurrentMenu) {
            displayCodeSnippetMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("x")) {
                stayOnCurrentMenu = false;
            } else {
                processCodeSnippetMenuCommands(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Processes user input from the search menu
    private void searchMenu() {
        boolean stayOnCurrentMenu = true;
        String command;
        while (stayOnCurrentMenu) {
            displaySearchMenu();
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("x")) {
                stayOnCurrentMenu = false;
            } else {
                processSearchMenuCommands(command);
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: Initializes a CodeBank
    private void init() {
        userCodeBank = new CodeBank();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: Displays menu of main options to user
    private void displayMainMenu() {
        System.out.println("\nSelect from:");
        System.out.println("d  -> Display CodeSnippet Menu");
        System.out.println("l  -> Load code bank from file");
        System.out.println("s  -> Save code bank to file");
        System.out.println("q  -> Quit");
    }

    // EFFECTS: Displays menu of code snippets options to user
    private void displayCodeSnippetMenu() {
        System.out.println("\nSelect from:");
        System.out.println("a  -> Add code snippet");
        System.out.println("d  -> Delete code snippet");
        System.out.println("l  -> List titles and Language of all stored code snippets");
        System.out.println("r  -> Retrieve code snippet");
        System.out.println("s  -> Search through code snippets in the code bank");
        System.out.println("x  -> To exit the current menu");
    }

    // EFFECTS: Displays menu of options for searching the code bank
    private void displaySearchMenu() {
        System.out.println("\nSelect from:");
        System.out.println("t  -> Search by Title");
        System.out.println("c  -> Search by Coding Language");
        System.out.println("x  -> To exit the current menu");
    }

    // MODIFIES: this
    // EFFECTS: Processes user commands from main menu
    private void processMainMenuCommands(String command) {
        switch (command) {
            case "d":
                codeSnippetsMenu();
                break;
            case "l":
                loadCodeBank();
                break;
            case "s":
                saveCodeBank();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: Processes user commands from code snippet menu
    private void processCodeSnippetMenuCommands(String command) {
        switch (command) {
            case "a":
                addCodeSnippet();
                break;
            case "d":
                deleteCodeSnippet();
                break;
            case "l":
                listStoredSnippets();
                break;
            case "r":
                retrieveCodeSnippet();
                break;
            case "s":
                searchMenu();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // EFFECTS: Processes user command from search menu
    private void processSearchMenuCommands(String command) {
        switch (command) {
            case "t":
                searchByTitle();
                break;
            case "c":
                searchByCodingLanguage();
                break;
            default:
                System.out.println("Not a valid option");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds the user code snippet to the userCodeBank if possible, otherwise displays error message.
    private void addCodeSnippet() {
        System.out.println("The combination of Code Snippet title and coding language used must be unique!");
        System.out.print("Code Snippet Title: ");
        String title = input.next().trim();
        System.out.print("Language utilized: ");
        String codeLanguage = input.next().trim();
        input.useDelimiter("/saveCode()");
        System.out.println("Enter the code body below, type /saveCode() when finished:");
        String codeBody = input.next();
        input.useDelimiter("\n");
        CodeSnippet userCodeSnippet = new CodeSnippet(title, codeLanguage, codeBody);
        if (userCodeBank.addSnippet(userCodeSnippet)) {
            System.out.println();
            System.out.println("Added Successfully!");
            System.out.println("You currently have " + userCodeBank.length() + " Code Snippets stored.");
        } else {
            System.out.println();
            System.out.println("Failed to Add! \nA CodeSnippet with that title and coding language already exists!");
        }
        input.nextLine();
        continuePrompt();

    }

    // MODIFIES: this
    // EFFECTS: Deletes a users code snippet if the snippet has been stored, otherwise displays error message
    private void deleteCodeSnippet() {
        System.out.println("Please enter the title and language of the code Snippet you wish to remove.");
        System.out.print("Code Snippet Title: ");
        String title = input.next().trim();
        System.out.print("Language utilized: ");
        String codeLanguage = input.next().trim();
        if (userCodeBank.removeSnippet(title, codeLanguage)) {
            System.out.println("Code Snippet has been removed!");
            System.out.println("You currently have " + userCodeBank.length() + " Code Snippets stored.");
        } else {
            System.out.println("Not found try again! (Make sure to enter title and language correctly!).");
        }
        continuePrompt();
    }

    // EFFECTS: Displays all the stored code snippets title and language, or empty message if none stored.
    private void listStoredSnippets() {
        if (userCodeBank.length() == 0) {
            System.out.println("Your code bank is empty!");
        } else {
            for (int i = 0; i < userCodeBank.length(); i++) {
                String codeTitle = userCodeBank.get(i).getTitle();
                String codeLanguage = userCodeBank.get(i).getCodeLanguage();
                System.out.println("TITLE: " + codeTitle + "  " + "CODING LANGUAGE: " + codeLanguage);
            }

        }
        continuePrompt();
    }

    // EFFECTS: Displays the user code Snippet if found, otherwise returns not found message
    private void retrieveCodeSnippet() {
        System.out.println("Please enter the title and language of the code Snippet you wish to retrieve.");
        System.out.print("Code Snippet Title: ");
        String title = input.next().trim();
        System.out.print("Language utilized: ");
        String codeLanguage = input.next().trim();
        CodeSnippet retrievedCodeSnippet = userCodeBank.retrieveCode(title, codeLanguage);
        if (retrievedCodeSnippet != null) {
            retrieveCodeBody(retrievedCodeSnippet);
        } else {
            System.out.println("Not found try again! (title is case sensitive).");
        }
        continuePrompt();
    }

    // EFFECTS: Prompts the user to enter c to continue
    private void continuePrompt() {
        String continueProgram = "a";
        while (!continueProgram.equalsIgnoreCase("c")) {
            System.out.println();
            System.out.print("Enter c to continue: ");
            continueProgram = input.next();
        }
    }


    // EFFECTS: saves the codeBank to file
    private void saveCodeBank() {
        try {
            jsonWriter.open();
            jsonWriter.write(userCodeBank);
            jsonWriter.close();
            System.out.println("Saved " + userCodeBank.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
        continuePrompt();
    }

    // MODIFIES: this
    // EFFECTS: loads codeBank from file
    private void loadCodeBank() {
        try {
            userCodeBank = jsonReader.read();
            System.out.println("Loaded " + userCodeBank.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
        continuePrompt();

    }

    // EFFECTS: searches through code bank based on a given string. Produce code snippets, with the title
    //          starting with the given string if any found. Otherwise, state none were found.
    private void searchByTitle() {
        System.out.println("Enter a string to search by: ");
        String keyword = input.next();
        ArrayList<CodeSnippet> searchResults = userCodeBank.searchByTitle(keyword);
        if (searchResults.size() == 0) {
            System.out.println("No code snippet title found starting with the given string!");
            continuePrompt();
        } else {
            displaySearchResults(searchResults);
        }
    }

    // EFFECTS: searches through code bank based on a given Coding Language. Produce code snippets with the given
    //          coding language if any found. Otherwise, state none were found.
    private void searchByCodingLanguage() {
        System.out.println("Enter coding language to search by: ");
        String codingLanguage = input.next();
        ArrayList<CodeSnippet> searchResults = userCodeBank.searchByLanguage(codingLanguage);
        if (searchResults.size() == 0) {
            System.out.println("No Code Snippet found utilizing the given coding language!");
            continuePrompt();
        } else {
            displaySearchResults(searchResults);
        }

    }

    // EFFECTS: Displays the items found in a search one by one, each time allowing the user to retrieve the results
    //          if they wish to.
    private void displaySearchResults(ArrayList<CodeSnippet> searchResults) {
        System.out.println(searchResults.size() + " result(s) found! \n");
        for (CodeSnippet snippet : searchResults) {
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("The following code snippet has been found: \n");
            String codeTitle = snippet.getTitle();
            String codeLanguage = snippet.getCodeLanguage();
            System.out.println("TITLE: " + codeTitle + "  " + "CODING LANGUAGE: " + codeLanguage + "\n");
            retrieveAfterSearch(snippet);
        }
    }

    // EFFECTS: Provides the user with the option to retrieve the body of
    //          code snippet found via search.
    private void retrieveAfterSearch(CodeSnippet snippet) {
        System.out.println("Would you like to retrieve this code snippet? (y/n)");
        String wantToRetrieve = input.next();
        boolean noValidInputGiven = true;
        while (noValidInputGiven) {
            if (wantToRetrieve.equalsIgnoreCase("y")) {
                retrieveCodeBody(snippet);
                noValidInputGiven = false;
                continuePrompt();
            } else if (wantToRetrieve.equalsIgnoreCase("n")) {
                noValidInputGiven = false;
            } else {
                System.out.println("Please enter y or n!");
            }
        }
    }

    // EFFECTS: Displays the body of the code snippet that is being retrieved.
    private void retrieveCodeBody(CodeSnippet snippet) {
        System.out.println("Your Code:");
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println(snippet.getSnippetCode());
        System.out.println("-------------------------------------------------------------------------------------");
    }
}


