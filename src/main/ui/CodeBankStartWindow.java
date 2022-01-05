package ui;


import model.CodeBank;
import model.Event;
import model.EventLog;
import persistence.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

// Sets up the initial window with the starting window for the CodeBank application
public class CodeBankStartWindow extends JFrame implements ActionListener {
    private static final String JSON_STORE = "./data/codeBank.json";
    protected JFrame frame;
    private JButton enterButton;
    private JButton loadButton;
    private JButton exitButton;
    private final GridBagConstraints grid = new GridBagConstraints();
    private CodeBank userCodeBank;

    // MODIFIES: this
    // EFFECTS: constructs the CodeBank Starting window
    public CodeBankStartWindow() {
        startingWindowFrame();
        startingWindowImage();
        startingWindowButtons();

    }

    // MODIFIES: this
    // EFFECTS: initializes the frame as 400 by 400 pixel window with an icon and a title
    //          and makes it visible
    private void startingWindowFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setTitle("CodeBank");
        frame.setResizable(false);
        frame.setVisible(true);

        ImageIcon icon = new ImageIcon("./data/Icon.png");
        frame.setIconImage(icon.getImage());
    }

    // MODIFIES: this
    // EFFECTS: Initializes and adds three buttons (CodeBank, load, exit) to the frame at the specified locations
    private void startingWindowButtons() {
        ArrayList<JButton> buttons = new ArrayList<>();
        enterButton = new JButton("Code Bank");
        loadButton = new JButton("Load");
        exitButton = new JButton("Exit");
        buttons.add(enterButton);
        buttons.add(loadButton);
        buttons.add(exitButton);
        int i = 1;
        for (JButton button : buttons) {
            initButtons(button);
            setVerticalPosition(i);
            frame.add(button, grid);
            i++;
        }
        frame.pack();
    }

    // MODIFIES: this
    // EFFECTS: Initialize and adds an image to the top center area of the window
    private void startingWindowImage() {
        JLabel startPageImage = new JLabel();
        ImageIcon icon2 = new ImageIcon("./data/CodeBank.png");
        startPageImage.setIcon(icon2);
        startPageImage.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(startPageImage);
    }

    // MODIFIES: this
    // EFFECTS: Provides a button with standard attributes and sets to the given position in the frame.
    private void initButtons(JButton button) {
        button.setFocusable(false);
        button.addActionListener(this);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        button.setBorder(BorderFactory.createEtchedBorder());

    }

    // MODIFIES: this
    // EFFECTS: Changes grid coordinates of next added component and adjust that component to desired width.
    private void setVerticalPosition(int y) {
        grid.gridx = 0;
        grid.gridy = y;
        grid.gridwidth = 5;
        grid.fill = GridBagConstraints.HORIZONTAL;
    }

    @Override
    // MODIFIES: this
    // EFFECTS: Applies the different functionalities of the buttons provided, loading, entering the code bank
    //          and exiting the application
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enterButton) {
            new CodeBankMainProgramWindow(userCodeBank);
            frame.dispose();
        } else if (e.getSource() == loadButton) {
            loadCodeBank();
        } else if (e.getSource() == exitButton) {
            for (Event event : EventLog.getInstance()) {
                System.out.println(event.toString() + "\n\n");
            }
            System.exit(0);
        }

    }

    // MODIFIES: this
    // EFFECTS: Loads the CodeBank from file and displays a confirmation message that the CodeBank as loaded
    private void loadCodeBank() {
        JsonReader jsonReader = new JsonReader(JSON_STORE);
        try {
            userCodeBank = jsonReader.read();
            JLabel confirmLoaded = new JLabel();
            confirmLoaded.setText("Your CodeBank has loaded!");
            confirmLoaded.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            setVerticalPosition(4);
            frame.add(confirmLoaded, grid);
            frame.pack();
        } catch (IOException ex) {
            System.out.println("Failed to load!");
        }

    }
}
