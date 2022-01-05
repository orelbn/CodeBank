package ui;

import model.CodeBank;
import model.CodeSnippet;
import model.Event;
import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// The main program window for the codeBank, that implements the main portion of the application
public class CodeBankMainProgramWindow extends JFrame implements ActionListener, WindowListener {
    private static final String JSON_STORE = "./data/codeBank.json";
    private Boolean cancelled;
    private CodeBank userCodeBank;
    private DefaultListModel<CodeSnippet> codeSnippetModel;
    private JButton confirmButton;
    private JButton cancelButton;
    private JDialog codeBodyInputWindow;
    private JFrame codeBankWindow;
    private JList<CodeSnippet> codeSnippetList;
    private JMenuBar menuBar;
    private JMenuItem addCodeSnippetButton;
    private JMenuItem removeCodeSnippetButton;
    private JMenuItem searchCodeBankButton;
    private JMenuItem saveCodeBankButton;
    private JMenuItem loadCodeBankButton;
    private JScrollPane textAreaScrollBar;
    private JSplitPane mainPanel;
    private JTextArea codeSnippetTextArea;


    // MODIFIES: this
    // EFFECTS: Constructs and sets up the different components of the main window application,
    //          based on the user codeBank
    public CodeBankMainProgramWindow(CodeBank codeBank) {
        userCodeBank = codeBank;
        setupFrame();
        setupList();
        setupSplitPanel();
        setupMenus();
        showSelectedCodeSnippet();
        codeBankWindow.add(mainPanel, BorderLayout.CENTER);
        codeBankWindow.add(menuBar, BorderLayout.PAGE_START);
        codeBankWindow.pack();
        codeBankWindow.setVisible(true);
    }


    // MODIFIES: this
    // EFFECTS: Sets up the main frame for the application and gives it an icon
    private void setupFrame() {
        codeBankWindow = new JFrame("CodeBank");
        codeBankWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        codeBankWindow.setPreferredSize(new Dimension(800, 600));
        codeBankWindow.setLocationRelativeTo(null);
        codeBankWindow.setLayout(new BorderLayout());
        codeBankWindow.addWindowListener(this);
        ImageIcon icon = new ImageIcon("./data/Icon.png");
        codeBankWindow.setIconImage(icon.getImage());

    }

    // MODIFIES: this
    // EFFECTS: Adds the list of code snippets to the left component of the Split Panel
    private void setupList() {
        codeSnippetList = new JList<>();
        codeSnippetModel = new DefaultListModel<>();
        codeSnippetList.setModel(codeSnippetModel);
        if (userCodeBank != null) {
            for (CodeSnippet codeSnippet : userCodeBank.getCodeSnippets()) {
                codeSnippetModel.addElement(codeSnippet);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: sets up a SplitPanel, with one side being for the list of codeSnippets the other for
    //          the text area where the code snippets are viewed and copied.
    private void setupSplitPanel() {
        mainPanel = new JSplitPane();
        mainPanel.setOneTouchExpandable(true);
        mainPanel.setContinuousLayout(true);
        codeSnippetTextArea = new JTextArea();
        codeSnippetTextArea.setEditable(false);
        textAreaScrollBar = (new JScrollPane(codeSnippetTextArea));
        mainPanel.setLeftComponent(new JScrollPane(codeSnippetList));
        mainPanel.setRightComponent(textAreaScrollBar);
        mainPanel.getLeftComponent().setMinimumSize(new Dimension(150, 400));
        mainPanel.getRightComponent().setMinimumSize(new Dimension(150, 400));
    }


    // MODIFIES: this
    // EFFECTS: sets up a Menu Bar containing the menus which holds the add, remove, save, and load options
    private void setupMenus() {
        JMenu fileMenu = new JMenu("File");
        JMenu editingMenu = new JMenu("Edit");

        saveCodeBankButton = initMenuButton("Save CodeBank", fileMenu);
        loadCodeBankButton = initMenuButton("Load CodeBank", fileMenu);
        addCodeSnippetButton = initMenuButton("Add Code Snippet", editingMenu);
        removeCodeSnippetButton = initMenuButton("Remove Code Snippet", editingMenu);
        searchCodeBankButton = initMenuButton("Search For CodeSnippet", editingMenu);

        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editingMenu);
    }

    // MODIFIES: this
    // EFFECTS: initialize a new JMenuItem with the given text, and adds to given menu
    private JMenuItem initMenuButton(String text, JMenu menu) {
        JMenuItem menuButton = new JMenuItem(text);
        menuButton.addActionListener(this);
        menu.add(menuButton);
        return menuButton;
    }


    // MODIFIES: this
    // EFFECTS: Sets up a selection listener that calls a method which will
    //          showcase the selected code snippet from code snippet list
    //          when a code snippet is selected
    private void showSelectedCodeSnippet() {
        codeSnippetList.getSelectionModel().addListSelectionListener(this::valueChanged);
    }

    // MODIFIES: this
    // EFFECTS: shows the selected code snippet in the text-field area, and make sure that scroll bar
    //          is set to the top of the text area
    private void valueChanged(ListSelectionEvent e) {
        CodeSnippet codeSnippet = codeSnippetList.getSelectedValue();
        codeSnippetTextArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        if (codeSnippet != null) {
            codeSnippetTextArea.setText(codeSnippet.getSnippetCode());
            SwingUtilities.invokeLater(() ->
                    textAreaScrollBar.getViewport().setViewPosition(new Point(0, 0)));
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes all code Snippets currently displaying and updates left list component
    //          with elements from the CodeBank that is being loaded.
    private void updateListLoadedCodeBank() {
        codeSnippetModel.removeAllElements();
        if (userCodeBank != null) {
            for (CodeSnippet codeSnippet : userCodeBank.getCodeSnippets()) {
                codeSnippetModel.addElement(codeSnippet);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Provides a pop that allows to take input for user and provides the user with a prompt to specify
    //          requested input
    private String userInputPopUp(String prompt, String popUpTitle) {
        return (String) JOptionPane.showInputDialog(codeBankWindow,
                prompt, popUpTitle, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }

    // MODIFIES: this
    // EFFECTS: Sets up a Window that takes in the users code snippet body and returns it.
    //          returns null if the user cancels or closes the window
    private String setCodeSnippetCodeBody() {
        cancelled = true;
        codeBodyInputWindow = new JDialog(codeBankWindow, "Add Coding Snippet Code Body", true);
        codeBodyInputWindow.setSize(new Dimension(400, 400));
        codeBodyInputWindow.setLocationRelativeTo(null);
        JTextArea input = new JTextArea();
        JScrollPane inputScrollBar = new JScrollPane(input);
        codeBodyInputWindow.add(inputScrollBar);
        confirmButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        confirmButton.addActionListener(this);
        cancelButton.addActionListener(this);
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new GridLayout());
        buttonContainer.add(confirmButton);
        buttonContainer.add(cancelButton);
        codeBodyInputWindow.add(buttonContainer, BorderLayout.PAGE_END);
        codeBodyInputWindow.setVisible(true);
        if (cancelled) {
            return null;
        } else {
            return input.getText();
        }
    }

    // MODIFIES: this
    // EFFECTS: clears the text area
    private void clearTextArea() {
        codeSnippetTextArea.setText("");
    }

    // MODIFIES: this
    // EFFECTS: Performs actions when the user press on components in the CodeBank
    //          Actions included save CodeBank
    //          Load CodeBank from previous version
    //          Add new CodeSnippet to CodeBank
    //          Remove CodeSnippet from CodeBank
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadCodeBankButton) {
            loadCodeBank();
        } else if (e.getSource() == saveCodeBankButton) {
            saveCodeBank();
        } else if (e.getSource() == addCodeSnippetButton) {
            addCodeSnippet();
        } else if (e.getSource() == removeCodeSnippetButton) {
            removeCodeSnippet();
        } else if (e.getSource() == searchCodeBankButton) {
            searchForCodeSnippets();
        } else if (e.getSource() == confirmButton) {
            cancelled = false;
            codeBodyInputWindow.dispose();
        } else if (e.getSource() == cancelButton) {
            codeBodyInputWindow.dispose();
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads the codeBank from codeBank.json and updates the current application with the loaded codeBank
    //          Catches IOException and prints out error message if codeBank fails to load.
    private void loadCodeBank() {
        JsonReader jsonReader = new JsonReader(JSON_STORE);
        try {
            userCodeBank = jsonReader.read();
            updateListLoadedCodeBank();
            codeSnippetTextArea.setText("");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(codeBankWindow, "Failed to load");
        }
    }

    // MODIFIES: codeBank.json
    // EFFECTS: Saves the current codeBank to a JSON file
    //          Catches IOException and prints out error message if codeBank fails to load.
    private void saveCodeBank() {
        JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
        try {
            jsonWriter.open();
            jsonWriter.write(userCodeBank);
            jsonWriter.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(codeBankWindow, "Failed to save");
        }
    }

    // MODIFIES: this
    // EFFECTS: Allows the user to add a new codeSnippet to the Code Bank and Modifies the GUI accordingly to reflect
    //          the change. If a Code Snippet with the given title and coding language is already in the code bank
    //          produces an information message and does not add the code snippet.
    private void addCodeSnippet() {
        String title = userInputPopUp("Input Code Snippet Title:", "Add Code Snippet Title");
        if (title != null) {
            String codingLanguage = userInputPopUp("Input The Coding Language:",
                    "Add Code Snippet Coding Language");
            if (codingLanguage != null) {
                String codeBody = setCodeSnippetCodeBody();
                if (codeBody != null) {
                    CodeSnippet snippetToAdd = new CodeSnippet(title, codingLanguage, codeBody);
                    if (userCodeBank == null) {
                        userCodeBank = new CodeBank();
                    }
                    if (userCodeBank.addSnippet(snippetToAdd)) {
                        codeSnippetModel.addElement(snippetToAdd);
                    } else {
                        showMessage(4);
                    }
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes a code snippet from the Code Bank and modifies the GUI accordingly to reflect that change.
    //          If no code snippets to remove produce an information, does nothing if user cancels or closes the pop-up
    private void removeCodeSnippet() {
        if (codeSnippetModel.size() == 0) {
            showMessage(3);
        } else {
            Object[] codeSnippets = userCodeBank.getCodeSnippets().toArray();
            Object snippetToRemove = JOptionPane.showInputDialog(codeBankWindow,
                    "Select Code Snippet to Remove:", "Remove Code Snippet",
                    JOptionPane.PLAIN_MESSAGE, null,
                    codeSnippets, userCodeBank.get(0));
            codeSnippetModel.removeElement(snippetToRemove);
            userCodeBank.removeSnippet((CodeSnippet) snippetToRemove);
            clearTextArea();
        }
    }

    // MODIFIES: this
    // EFFECTS: Searches for code snippets containing the title or coding language provided by the user
    private void searchForCodeSnippets() {
        if (codeSnippetModel.size() == 0) {
            showMessage(2);
        } else {
            ArrayList<CodeSnippet> codeSnippets = new ArrayList<>();
            String searchTerm = userInputPopUp("Input The Title or Coding Language to search for! "
                    + "(case sensitive):", "Search Code Bank");
            if (searchTerm != null) {
                for (CodeSnippet codeSnippet : userCodeBank.getCodeSnippets()) {
                    if (codeSnippet.getTitle().contains(searchTerm)
                            || codeSnippet.getCodeLanguage().equals(searchTerm)) {
                        codeSnippets.add(codeSnippet);
                    }
                }
                selectCodeSnippet(codeSnippets);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: allows the user to select from the filtered list of codeSnippets after a search is conducting
    //          and display the body of the selected code snippet in the text area
    private void selectCodeSnippet(ArrayList<CodeSnippet> codeSnippets) {
        if (codeSnippets.isEmpty()) {
            showMessage(1);
        } else {
            Object[] filteredCodeSnippets = codeSnippets.toArray();
            Object selectedCodeSnippet = JOptionPane.showInputDialog(codeBankWindow,
                    "Select Code Snippet to Remove:", "Remove Code Snippet",
                    JOptionPane.PLAIN_MESSAGE, null,
                    filteredCodeSnippets, codeSnippets.get(0));
            if (selectedCodeSnippet != null) {
                codeSnippetTextArea.setText(((CodeSnippet) selectedCodeSnippet).getSnippetCode());
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Showcases an information message to the window
    private void showMessage(int i) {
        switch (i) {
            case 1:
                JOptionPane.showMessageDialog(codeBankWindow, "No code snippets found!");
                break;
            case 2:
                JOptionPane.showMessageDialog(codeBankWindow, "There are no code snippets to search for!");
                break;
            case 3:
                JOptionPane.showMessageDialog(codeBankWindow, "There are no code snippets to remove!");
                break;
            case 4:
                JOptionPane.showMessageDialog(codeBankWindow,
                        "CodeSnippet with given title and language as " + "already been added!");
                break;
            default:
                break;
        }
    }

    // EFFECTS: Prints out the event logs to the console
    @Override
    public void windowClosed(WindowEvent e) {
        System.out.println("EVENT LOG:");
        for (Event event : EventLog.getInstance()) {
            System.out.print(event.toString() + "\n\n");
        }
        EventLog.getInstance().clear();
    }

    // EFFECTS: Does not have an effect was required to be placed by the Window Listener class
    @Override
    public void windowOpened(WindowEvent e) {
    }

    // EFFECTS: Does not have an effect was required to be placed by the Window Listener class
    @Override
    public void windowClosing(WindowEvent e) {
    }

    // EFFECTS: Does not have an effect was required to be placed by the Window Listener class
    @Override
    public void windowIconified(WindowEvent e) {
    }

    // EFFECTS: Does not have an effect was required to be placed by the Window Listener class
    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    // EFFECTS: Does not have an effect was required to be placed by the Window Listener class
    @Override
    public void windowActivated(WindowEvent e) {
    }

    // EFFECTS: Does not have an effect was required to be placed by the Window Listener class
    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
