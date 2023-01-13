package ATM_GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MainPanel extends JPanel {

    protected Account currentUser;
    JFrame mainFrame;
    JTextField welcomeLabel;
    JPanel welcomePanel;
    DisplayPanel displayPanel = new DisplayPanel();
    DepositPanel depositPanel;
    WithdrawPanel withdrawPanel;
    CheckBalancePanel checkBalancePanel;

    JPanel displayArea;
    ButtonPanel buttonPanel;
    private String username;

    public MainPanel(Account currentUser){
        this.currentUser = currentUser;
        //Set up Main Frame
        mainFrame = new JFrame("ATM");
        mainFrame.setSize(Constant.MAIN_WIDTH,Constant.MAIN_HEIGHT);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setBackground(Color.RED);
        mainFrame.setLayout(null);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);

        //Set up Main Panel
        this.setPreferredSize(new Dimension(Constant.PANEL_WIDTH,Constant.PANEL_HEIGHT));
        this.setBounds(50,50,Constant.PANEL_WIDTH,Constant.PANEL_HEIGHT);
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        this.setOpaque(true);
        this.setBorder(new EtchedBorder(Color.BLACK,Color.BLACK));

        //Create welcome message
        createWelcomePanel();

        //Create buttons panel
        buttonPanel = new ButtonPanel();
        this.add(buttonPanel);

        //Add display area
        createDisplay(displayPanel);

        mainFrame.add(this);
        mainFrame.setVisible(true);
    }

    private void createDisplay(JPanel display){
        this.displayArea = display;
        this.add(this.displayArea);
    }

    private void createWelcomePanel() {
        welcomePanel = new JPanel();
        welcomePanel.setBounds(15,15,Constant.PANEL_WIDTH-30,50);
        welcomePanel.setBackground(Color.GREEN);
        welcomePanel.setLayout(new BorderLayout());
        createWelcomeText();
        this.add(welcomePanel);
    }

    private void createWelcomeText() {
        welcomeLabel = new JTextField();
        username = currentUser.getUsername();
        welcomeLabel.setFont(new Font("Arial",Font.BOLD,30));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setBorder(new EmptyBorder(0,0,0,0));
        welcomeLabel.setBackground(Color.WHITE);
        welcomeLabel.setBounds(0,0, welcomePanel.getWidth(), welcomePanel.getHeight());
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setText("Welcome " + username + " - Account ID: " + currentUser.getID());
        welcomeLabel.setEditable(false);
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
    }

    private void checkBalance() {
        this.remove(displayArea);
        checkBalancePanel = new CheckBalancePanel();
        this.createDisplay(checkBalancePanel);
        this.repaint();
    }

    private void withdrawMoney() {
        this.remove(displayArea);
        withdrawPanel = new WithdrawPanel();
        this.createDisplay(withdrawPanel);
        this.repaint();
    }

    private void depositMoney() {
        this.remove(displayArea);
        depositPanel = new DepositPanel();
        this.createDisplay(depositPanel);
        this.repaint();
    }

    private void toDefaultPage() {
        this.remove(displayArea);
        this.createDisplay(displayPanel);
        this.repaint();
    }

    private void logOut() {
        if (currentUser != null){
            if (checkBalancePanel == null){
                checkBalancePanel = new CheckBalancePanel();
            }
            updateLatestData(currentUser.getUpdatedData());
        }
        new Log_In();
        mainFrame.dispose();
    }

    private void updateLatestData(StringBuilder data){
        File originalFile = new File(Constant.logInDataFile);
        File destinationFile = new File(Constant.logInDataFile);
        String replaceWith = data.toString();
        StringBuilder originalData = new StringBuilder();
        String toBeReplaced = "";

        //Read original file:
        try {
            Scanner reader = new Scanner(originalFile);
            while (reader.hasNextLine()){
                String line = reader.nextLine();
                originalData.append(line);
                originalData.append("\n");
                if (Integer.parseInt(line.split(",")[0]) == currentUser.getID()){
                    toBeReplaced = line;
                }
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String stringBuffer = originalData.toString();
        stringBuffer = stringBuffer.replace(toBeReplaced,replaceWith);

        try {
            PrintWriter writer = new PrintWriter(destinationFile);
            writer.print(stringBuffer);
            writer.close();
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected class ButtonPanel extends JPanel{
        protected JButton deposit;
        protected JButton withdraw;
        protected JButton check;
        protected JButton logout;
        protected GridLayout layout = new GridLayout(1,4);

        protected ButtonPanel(){
            this.setBounds(75,475,650,50);
            this.setBackground(Color.WHITE);
            layout.setHgap(40);
            this.setLayout(layout);
            addButtons();
        }

        private void addButtons() {
            deposit = createButton("Deposit");
            withdraw = createButton("Withdraw");
            check = createButton("Balance");
            logout = createButton("Log Out");
            this.add(deposit);
            this.add(withdraw);
            this.add(check);
            this.add(logout);
        }

        private JButton createButton(String name){
            JButton button = new JButton();
            button.setText(name);
            button.setActionCommand(name);
            button.setBackground(Color.WHITE);
            button.setFocusable(false);
            button.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
            button.setBorder(new EtchedBorder(Color.BLACK,Color.BLACK));
            button.addActionListener(new BAL());
            return button;
        }

    }
    protected class BAL implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command){
                case "Deposit" -> depositMoney();
                case "Withdraw" -> withdrawMoney();
                case "Balance" -> checkBalance();
                case "Log Out" -> logOut();
                case "Return" -> toDefaultPage();
            }
        }
    }

    private class DefaultDisplay extends JPanel{
        EtchedBorder border = new EtchedBorder(Color.BLACK,Color.BLACK);

        public DefaultDisplay(){
            this.setBounds(30,80,Constant.PANEL_WIDTH-60,375);
            this.setBorder(border);
            this.setLayout(null);
        }
        public JButton createButton(String name, ActionListener AL){
            JButton button = new JButton(name);
            button.setActionCommand(name);
            button.setFocusable(false);
            button.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
            button.setBorder(new EtchedBorder(Color.BLACK,Color.BLACK));
            button.addActionListener(AL);
            return button;
        }
    }

    protected class DisplayPanel extends DefaultDisplay{

        public DisplayPanel(){
            super();
            this.setToolTipText("Display Panel");
            this.setBackground(Color.WHITE);
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);

            //Paint Strings:
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial",Font.PLAIN,23));
            g.drawString("TECHCOMBANK ATM SERVICE: ",190,40);
            g.drawString("> Deposit - deposit an amount of money to your account",40,100);
            g.drawString("> Withdraw - withdraw an amount of money from your account",40,150);
            g.drawString("> Check - check your current balance",40,200);
            g.drawString("> Log Out - log out of the service",40,250);
            g.drawString("----------------------------------------------------------------",110,320);
        }
    }

    protected class DepositPanel extends DefaultDisplay{

        private JButton returnButton;
        private JButton confirmButton;
        private JTextField depositLabel;
        private JTextField depositMessage;
        private JTextField inputField;
        private double inputAmount;
        private TitledBorder border;

        public DepositPanel(){
            super();

            border = new TitledBorder(new EtchedBorder(Color.BLACK,Color.BLACK));
            border.setTitleFont(new Font("Arial",Font.BOLD,18));
            border.setTitle("Deposit");
            this.setBorder(border);

            this.setToolTipText("Deposit Panel");
            this.setBackground(Color.WHITE);

            returnButton = createButton("Return", new BAL());
            returnButton.setBounds(600,310,100,30);
            this.add(returnButton);

            createConfirmButton();

            createDepositLabel();
            createInputField();
            createDepositMessage();
        }

        private void createConfirmButton(){
            confirmButton = new JButton("Confirm");
            confirmButton.setFocusable(false);
            confirmButton.setBackground(Color.WHITE);
            confirmButton.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
            confirmButton.setBorder(new EtchedBorder(Color.BLACK,Color.BLACK));
            confirmButton.setBounds(550,120,100,30);
            confirmButton.addActionListener(e -> {
                String userInput = inputField.getText();
                try {
                    inputAmount = Double.parseDouble(userInput);
                    if (inputAmount >= 1000.0 && inputAmount <= 500_000_000) {
                        currentUser.deposit(inputAmount);
                        inputField.setText("");
                        depositMessage.setForeground(Color.GREEN);
                        depositMessage.setText("Deposit Successful!");
                        inputField.setEnabled(false);
                    }
                    else if (inputAmount > 500_000_000){
                        depositMessage.setForeground(Color.RED);
                        depositMessage.setText("500,000,000 VND is the LIMIT to each transition");
                        inputField.setText("");
                    }
                    else {
                        depositMessage.setForeground(Color.RED);
                        depositMessage.setText("INVALID: Please input an amount larger than 1,000 VND");
                        inputField.setText("");
                    }
                }
                catch (NumberFormatException exception){
                    inputField.setText("");
                    depositMessage.setForeground(Color.RED);
                    depositMessage.setText("INVALID: Please input a numeric value");
                }
            });
            this.add(confirmButton);
        }

        private void createDepositMessage() {
            depositMessage = new JTextField("");
            depositMessage.setBounds(5,200,this.getWidth()-10,30);
            depositMessage.setEditable(false);
            depositMessage.getCaret().setVisible(false);
            depositMessage.setBackground(Color.WHITE);
            depositMessage.setBorder(new EmptyBorder(0,0,0,0));
            depositMessage.setFont(new Font("Arial",Font.BOLD,23));
            depositMessage.setHorizontalAlignment(JTextField.CENTER);
            this.add(depositMessage);
        }

        private void createInputField() {
            inputField = new JTextField();
            inputField.setBounds(130,120,400,30);
            inputField.setBorder(new EtchedBorder(Color.BLACK,Color.BLACK));
            inputField.setBackground(Color.WHITE);
            inputField.setHorizontalAlignment(JTextField.CENTER);
            inputField.setFont(new Font(Font.MONOSPACED,Font.BOLD,25));
            this.add(inputField);
        }

        private void createDepositLabel() {
            depositLabel = new JTextField("Insert the deposit amount below");
            depositLabel.setBounds(5,40,this.getWidth()-10,30);
            depositLabel.setBorder(new EmptyBorder(0,0,0,0));
            depositLabel.setFont(new Font("Arial",Font.BOLD,23));
            depositLabel.setHorizontalAlignment(JTextField.CENTER);
            this.add(depositLabel);
        }
    }

    protected class WithdrawPanel extends DefaultDisplay{
        private JButton returnButton;
        private JButton confirmButton;
        private JTextField withdrawLabel;
        private JTextField withdrawMessage;
        private JTextField inputField;
        private TitledBorder border;
        private double inputAmount;
        public WithdrawPanel(){
            super();

            border = new TitledBorder(new EtchedBorder(Color.BLACK,Color.BLACK));
            border.setTitleFont(new Font("Arial",Font.BOLD,18));
            border.setTitle("Withdraw");
            this.setBorder(border);

            this.setToolTipText("Withdraw Panel");
            this.setBackground(Color.WHITE);

            returnButton = createButton("Return", new BAL());
            returnButton.setBounds(600,310,100,30);
            this.add(returnButton);

            createConfirmButton();

            createWithdrawLabel();
            createInputField();
            createWithdrawMessage();
        }

        private void createConfirmButton() {
            confirmButton = new JButton("Confirm");
            confirmButton.setFocusable(false);
            confirmButton.setBackground(Color.WHITE);
            confirmButton.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
            confirmButton.setBorder(new EtchedBorder(Color.BLACK,Color.BLACK));
            confirmButton.setBounds(550,120,100,30);
            confirmButton.addActionListener(e -> {
                String userInput = inputField.getText();
                try {
                    inputAmount = Double.parseDouble(userInput);
                    if (inputAmount >= 1000.0 && inputAmount <= currentUser.getBalance()) {
                        currentUser.withdraw(inputAmount);
                        inputField.setText("");
                        withdrawMessage.setForeground(Color.GREEN);
                        withdrawMessage.setText("Withdraw Successful!");
                        inputField.setEnabled(false);
                    }
                    else if (inputAmount > currentUser.getBalance()){
                        withdrawMessage.setForeground(Color.RED);
                        withdrawMessage.setText("ERROR: Insufficient balance");
                        inputField.setText("");
                    }
                    else {
                        withdrawMessage.setForeground(Color.RED);
                        withdrawMessage.setText("INVALID: Please input an amount larger than 1,000 VND");
                        inputField.setText("");
                    }
                }
                catch (NumberFormatException exception){
                    inputField.setText("");
                    withdrawMessage.setForeground(Color.RED);
                    withdrawMessage.setText("INVALID: Please input a numeric value");
                }
            });

            this.add(confirmButton);
        }

        private void createWithdrawLabel() {
            withdrawLabel = new JTextField("Insert the withdraw amount below");
            withdrawLabel.setBounds(5,40,this.getWidth()-10,30);
            withdrawLabel.setBorder(new EmptyBorder(0,0,0,0));
            withdrawLabel.setFont(new Font("Arial",Font.BOLD,23));
            withdrawLabel.setHorizontalAlignment(JTextField.CENTER);
            this.add(withdrawLabel);
        }

        private void createInputField() {
            inputField = new JTextField();
            inputField.setBounds(130,120,400,30);
            inputField.setBorder(new EtchedBorder(Color.BLACK,Color.BLACK));
            inputField.setBackground(Color.WHITE);
            inputField.setHorizontalAlignment(JTextField.CENTER);
            inputField.setFont(new Font(Font.MONOSPACED,Font.BOLD,25));
            this.add(inputField);
        }

        private void createWithdrawMessage() {
            withdrawMessage = new JTextField("");
            withdrawMessage.setBounds(5,200,this.getWidth()-10,30);
            withdrawMessage.setEditable(false);
            withdrawMessage.getCaret().setVisible(false);
            withdrawMessage.setBackground(Color.WHITE);
            withdrawMessage.setBorder(new EmptyBorder(0,0,0,0));
            withdrawMessage.setFont(new Font("Arial",Font.BOLD,23));
            withdrawMessage.setHorizontalAlignment(JTextField.CENTER);
            this.add(withdrawMessage);
        }

    }

    protected class CheckBalancePanel extends DefaultDisplay{

        private JButton returnButton;
        private TitledBorder border;
        private JTextArea infoArea;
        private String info;

        public CheckBalancePanel(){
            super();

            info = "Customer Information:\n\n" + currentUser;

            border = new TitledBorder(new EtchedBorder(Color.BLACK,Color.BLACK));
            border.setTitleFont(new Font("Arial",Font.BOLD,18));
            border.setTitle("Balance");
            this.setBorder(border);

            this.setToolTipText("Check Balance Panel");
            this.setBackground(Color.WHITE);

            returnButton = createButton("Return", new BAL());
            returnButton.setBounds(600,310,100,30);
            this.add(returnButton);

            createInfoArea(info);
        }

        private void createInfoArea(String text) {
            infoArea = new JTextArea();
            infoArea.setEditable(false);
            infoArea.setBounds(80,60,600,300);
            infoArea.setLineWrap(true);
            infoArea.setFont(new Font("Arial",Font.PLAIN,23));
            infoArea.setText(text);
            this.add(infoArea);
        }

        public String getInfo(){
            return info;
        }
    }
}
