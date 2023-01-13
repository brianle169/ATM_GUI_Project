package ATM_GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Log_In {

    JFrame mainFrame;
    JTextField logo;
    JPanel logoPanel;
    JTextField idInput;
    private JPasswordField passwordInput;
    JLabel idLabel = new JLabel("Account ID: ");
    JLabel passwordLabel = new JLabel("Password: ");
    JTextField logInMessage = new JTextField("");
    JButton logInButton;
    JButton registerButton;
    JPanel logInPanel;
    JPanel buttonsPanel;
    String actionLogin = "Log In";
    String actionRegister = "Register";
    GridLayout layout = new GridLayout(1,2);
    RegisterPage registerPage;
    private HashMap<String,Account> userData = new HashMap<>();

    public Log_In(){
        loadData();
        setMainFrame();

        createLogo();
        createLogInArea();
        createButtonArea();
        createLogInMessage();

        mainFrame.setVisible(true);
    }

    private void loadData(){
        File database = new File(Constant.logInDataFile);
        String ID;
        Account account;
        try {
            Scanner reader = new Scanner(database);
            while (reader.hasNextLine()){
                if (reader.nextLine().isEmpty()) {break;}
                String[] line = reader.nextLine().split(",");
                ID = line[0];
                account = new Account(Integer.parseInt(line[0]),line[1],line[2],Double.parseDouble(line[3]),line[4]);
                userData.put(ID,account);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void createLogInArea() {
        logInPanel = new JPanel();
        logInPanel.setBounds(25,150,340,80);
        logInPanel.setToolTipText("Log In Panel");
        logInPanel.setBackground(Color.WHITE);
        logInPanel.setLayout(null);

        idLabel.setFont(new Font(Font.DIALOG,Font.BOLD,15));
        idLabel.setBounds(30,5,100,25);
        idLabel.setForeground(Color.BLACK);
        passwordLabel.setFont(new Font(Font.DIALOG,Font.BOLD,15));
        passwordLabel.setBounds(30,40,100,25);
        passwordLabel.setForeground(Color.BLACK);

        idInput = new JTextField();
        idInput.setBackground(Color.WHITE);
        idInput.setBounds(120,5,190,25);
        idInput.setHorizontalAlignment(JTextField.LEFT);
        idInput.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        idInput.setBorder(new EtchedBorder(null,Color.BLACK));

        passwordInput = new JPasswordField();
        passwordInput.setBackground(Color.WHITE);
        passwordInput.setBounds(120,40,190,25);
        passwordInput.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
        passwordInput.setBorder(new EtchedBorder(null,Color.black));

        logInPanel.add(idLabel);
        logInPanel.add(idInput);
        logInPanel.add(passwordLabel);
        logInPanel.add(passwordInput);

        mainFrame.add(logInPanel);
    }

    private void createButtonArea(){
        buttonsPanel = new JPanel();
        buttonsPanel.setToolTipText("Buttons Panel");
        buttonsPanel.setBounds(45,250,300,30);
        buttonsPanel.setBackground(Color.WHITE);
        layout.setHgap(20);
        buttonsPanel.setLayout(layout);

        logInButton = createButton("Log In",actionLogin, new ButtonListener());
        registerButton = createButton("Register",actionRegister, new ButtonListener());

        buttonsPanel.add(logInButton);
        buttonsPanel.add(registerButton);

        mainFrame.add(buttonsPanel);
    }

    private JButton createButton(String name, String actionCommand, ActionListener l){
        JButton newButton = new JButton(name);
        newButton.setFont(new Font(Font.DIALOG,Font.BOLD,13));
        newButton.setActionCommand(actionCommand);
        newButton.addActionListener(l);
        newButton.setBackground(Color.WHITE);
        newButton.setBorder(new EtchedBorder(Color.BLACK,Color.BLACK));
        newButton.setFocusable(false);
        return newButton;
    }

    private void setMainFrame(){
        mainFrame = new JFrame("Log In");
        mainFrame.setSize(Constant.LI_WIDTH,Constant.LI_HEIGHT);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
        mainFrame.setResizable(false);
        mainFrame.getContentPane().setBackground(Color.WHITE);
        mainFrame.setLocationRelativeTo(null);
    }

    private void createLogo(){
        logoPanel = new JPanel();
        logoPanel.setBounds(0,70,Constant.LI_WIDTH,50);
        logoPanel.setLayout(new BorderLayout());

        logo = new JTextField();
        logo.setFont(new Font("Arial",Font.BOLD,30));
        logo.setForeground(Color.RED);
        logo.setText("TECHCOMBANK v2");
        logo.setBorder(new EmptyBorder(0,0,0,0));
        logo.setHorizontalAlignment(JTextField.CENTER);
        logo.setEditable(false);
        logo.setBackground(Color.WHITE);

        logoPanel.add(logo, BorderLayout.CENTER);
        mainFrame.add(logoPanel);
    }

    private void createLogInMessage(){
        logInMessage = new JTextField("");
        logInMessage.setBounds(0,310,Constant.LI_WIDTH,30);
        logInMessage.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        logInMessage.setHorizontalAlignment(JTextField.CENTER);
        logInMessage.setForeground(Color.BLACK);
        logInMessage.setEditable(false);
        logInMessage.setBorder(new EmptyBorder(0,0,0,0));
        logInMessage.setBackground(Color.WHITE);

        mainFrame.add(logInMessage);
    }

    private void checkLogIn(){
        String inputPass = String.valueOf(passwordInput.getPassword());
        String inputID = idInput.getText();

        if (!userData.containsKey(inputID)){
            logInMessage.setForeground(Color.RED);
            logInMessage.setText("Invalid ID");
            idInput.setText("");
            passwordInput.setText("");
        }

        else if (!userData.get(inputID).checkPassword(inputPass)){
            logInMessage.setForeground(Color.RED);
            logInMessage.setText("Password Incorrect");
            passwordInput.setText("");
        }

        else if (userData.get(inputID).checkPassword(inputPass)){
            mainFrame.dispose();
            new MainPanel(userData.get(inputID));
        }

    }
    private class RegisterPage {

        JFrame registerFrame;
        JTextField registerLogo;
        JPanel registerLogoPanel;
        JTextField usernameRegister;
        JLabel usernameLabel = new JLabel("Full Name: ");
        JTextField mobileNumberRegister;
        JLabel mobileLabel = new JLabel("Phone Number: ");
        JTextField initialBalanceRegister;
        JLabel balanceLabel = new JLabel("Initial Balance: ");
        private JPasswordField passwordRegister;
        JLabel passwordRegisterLabel = new JLabel("Password: ");
        private JPasswordField passwordConfirm;
        JLabel confirmLabel = new JLabel("Confirm Password: ");
        JPanel registerInputPanel;
        JPanel registerButtonPanel;
        JTextField registerMessage = new JTextField("");
        JButton createAccountButton;
        JButton returnButton;
        String actionRegister = "Create Account";
        String actionReturn = "Return";
        GridLayout registerButtonLayout = new GridLayout(1,2);
        Account newUser;


        private RegisterPage(){
            setRegisterFrame();

            setUpLogo();
            setUpRegisterArea();
            setUpButtonArea();
            setUpRegisterMessage();

            registerFrame.setVisible(true);
        }

        private void setUpRegisterMessage() {
            registerMessage = new JTextField("");
            registerMessage.setBounds(0,380,Constant.LI_WIDTH,30);
            registerMessage.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
            registerMessage.setHorizontalAlignment(JTextField.CENTER);
            registerMessage.setForeground(Color.BLACK);
            registerMessage.setEditable(false);
            registerMessage.setBorder(new EmptyBorder(0,0,0,0));
            registerMessage.setBackground(Color.WHITE);

            registerFrame.add(registerMessage);
        }

        private void setUpButtonArea() {
            registerButtonPanel = new JPanel();
            registerButtonPanel.setToolTipText("Register Button Area");
            registerButtonPanel.setBackground(Color.WHITE);
            registerButtonPanel.setBounds(45,320,300,30);
            registerButtonLayout.setHgap(20);
            registerButtonPanel.setLayout(registerButtonLayout);

            createAccountButton = createButton("Create Account",actionRegister, new RegisterListener());
            returnButton = createButton("Return",actionReturn, new RegisterListener());

            registerButtonPanel.add(createAccountButton);
            registerButtonPanel.add(returnButton);

            registerFrame.add(registerButtonPanel);
        }

        private void setUpRegisterArea() {
            int fieldWidth = 160;
            int fieldHeight = 25;
            registerInputPanel = new JPanel();
            registerInputPanel.setToolTipText("Register Input Area");
            registerInputPanel.setBackground(Color.WHITE);
            registerInputPanel.setLayout(null);
            registerInputPanel.setBounds(25,110,340,200);

            //Username input:
            usernameLabel.setFont(new Font(Font.DIALOG,Font.BOLD,15));
            usernameLabel.setBounds(20,5,100,25);

            usernameRegister = new JTextField();
            usernameRegister.setBackground(Color.WHITE);
            usernameRegister.setBounds(110,5,fieldWidth+40,fieldHeight);
            usernameRegister.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
            usernameRegister.setBorder(new EtchedBorder(null,Color.BLACK));

            registerInputPanel.add(usernameLabel);
            registerInputPanel.add(usernameRegister);

            //Mobile input:
            mobileLabel.setFont(new Font(Font.DIALOG,Font.BOLD,15));
            mobileLabel.setBounds(20,40,120,25);

            mobileNumberRegister = new JTextField();
            mobileNumberRegister.setBackground(Color.WHITE);
            mobileNumberRegister.setBounds(150,40,fieldWidth,fieldHeight);
            mobileNumberRegister.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
            mobileNumberRegister.setBorder(new EtchedBorder(null,Color.BLACK));

            registerInputPanel.add(mobileLabel);
            registerInputPanel.add(mobileNumberRegister);

            //Initial Balance Input:
            balanceLabel.setFont(new Font(Font.DIALOG,Font.BOLD,15));
            balanceLabel.setBounds(20,75,120,25);

            initialBalanceRegister = new JTextField();
            initialBalanceRegister.setBackground(Color.WHITE);
            initialBalanceRegister.setBounds(140,75,fieldWidth,fieldHeight);
            initialBalanceRegister.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
            initialBalanceRegister.setBorder(new EtchedBorder(null,Color.BLACK));

            registerInputPanel.add(balanceLabel);
            registerInputPanel.add(initialBalanceRegister);

            //Password input:
            passwordRegisterLabel.setFont(new Font(Font.DIALOG,Font.BOLD,15));
            passwordRegisterLabel.setBounds(20,110,100,25);

            passwordRegister = new JPasswordField();
            passwordRegister.setBackground(Color.WHITE);
            passwordRegister.setBounds(110,110,fieldWidth,fieldHeight);
            passwordRegister.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
            passwordRegister.setBorder(new EtchedBorder(null,Color.BLACK));

            registerInputPanel.add(passwordRegisterLabel);
            registerInputPanel.add(passwordRegister);

            //Confirm password:
            confirmLabel.setFont(new Font(Font.DIALOG,Font.BOLD,15));
            confirmLabel.setBounds(20,145,150,25);

            passwordConfirm = new JPasswordField();
            passwordConfirm.setBackground(Color.WHITE);
            passwordConfirm.setBounds(175,145,fieldWidth,fieldHeight);
            passwordConfirm.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));
            passwordConfirm.setBorder(new EtchedBorder(null,Color.BLACK));

            registerInputPanel.add(confirmLabel);
            registerInputPanel.add(passwordConfirm);

            registerFrame.add(registerInputPanel);

        }

        private void setUpLogo() {
            registerLogoPanel = new JPanel();
            registerLogoPanel.setBounds(0,30,Constant.LI_WIDTH,50);
            registerLogoPanel.setLayout(new BorderLayout());

            registerLogo = new JTextField();
            registerLogo.setFont(new Font("Arial",Font.BOLD,30));
            registerLogo.setForeground(Color.RED);
            registerLogo.setText("REGISTER");
            registerLogo.setBorder(new EmptyBorder(0,0,0,0));
            registerLogo.setHorizontalAlignment(JTextField.CENTER);
            registerLogo.setEditable(false);
            registerLogo.setBackground(Color.WHITE);

            registerLogoPanel.add(registerLogo, BorderLayout.CENTER);
            registerFrame.add(registerLogoPanel);
        }

        private void setRegisterFrame(){
            registerFrame = new JFrame("Register");
            registerFrame.setSize(Constant.LI_WIDTH,Constant.LI_HEIGHT);
            registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            registerFrame.setLayout(null);
            registerFrame.setResizable(false);
            registerFrame.getContentPane().setBackground(Color.WHITE);
            registerFrame.setLocationRelativeTo(null);
        }

        private class RegisterListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                switch (command){
                    case "Create Account" -> register();
                    case "Return" -> {
                        registerFrame.dispose();
                        new Log_In();
                    }
                }
            }
        }

        private void register(){
            String message = "Your account ID is: ";
            checkInputValidity();

            if (initialBalanceRegister.getText().isEmpty()){
                newUser = new Account(usernameRegister.getText(), mobileNumberRegister.getText());
                newUser.setPassword(String.valueOf(passwordRegister.getPassword()));
                try {
                    updateDataFile(newUser,Constant.logInDataFile);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                JOptionPane.showMessageDialog(null,message+newUser.getID(),"Log In Successful",JOptionPane.INFORMATION_MESSAGE);
                disableInputFields();
            }
            else {
                try {
                    double initialAmount = Double.parseDouble(initialBalanceRegister.getText());
                    if (initialAmount >= 50_000) {
                        newUser = new Account(usernameRegister.getText(), mobileNumberRegister.getText(), initialAmount);
                        newUser.setPassword(String.valueOf(passwordRegister.getPassword()));
                        try {
                            updateDataFile(newUser,Constant.logInDataFile);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        JOptionPane.showMessageDialog(null,message+newUser.getID(),"Log In Successful",JOptionPane.INFORMATION_MESSAGE);
                        disableInputFields();
                    }
                    else {
                        initialBalanceRegister.setText("");
                        registerMessage.setForeground(Color.RED);
                        registerMessage.setText("Minimum balance in an account is 50,000 VND");
                    }
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                    System.out.println(initialBalanceRegister.getText());
                    initialBalanceRegister.setText("");
                    registerMessage.setForeground(Color.RED);
                    registerMessage.setText("Invalid initial balance value");
                }
            }
        }

        private void updateDataFile(Account newAccount, String filePath) throws FileNotFoundException {
            StringBuilder newInfo = new StringBuilder();
            File file = new File(filePath);

            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()){
                newInfo.append(reader.nextLine());
                newInfo.append("\n");
            }

            PrintWriter writer = new PrintWriter(file);
            newInfo.append(newAccount.getUpdatedData());
            writer.print(newInfo);

            reader.close();
            writer.close();
        }

        private void disableInputFields(){
            registerMessage.setForeground(new Color(0,153,0));
            registerMessage.setText("Registration Complete!");
            usernameRegister.setEnabled(false);
            mobileNumberRegister.setEnabled(false);
            initialBalanceRegister.setEnabled(false);
            passwordRegister.setEnabled(false);
            passwordConfirm.setEnabled(false);
            createAccountButton.setEnabled(false);
        }

        private void checkInputValidity(){

            String inputUsername = usernameRegister.getText();
            String inputMobile = mobileNumberRegister.getText();
            String inputPassword = String.valueOf(passwordRegister.getPassword());
            String inputConfirm = String.valueOf(passwordConfirm.getPassword());
            Pattern specialCase = Pattern.compile("[^a-z\\D]", Pattern.CASE_INSENSITIVE);
            Pattern characters = Pattern.compile("\\D",Pattern.CASE_INSENSITIVE);
            Matcher nameMatcher = specialCase.matcher(inputUsername);
            Matcher mobileMatcher = characters.matcher(inputMobile);

            //Prompt immediately if missing any entry, except for initial balance
            if (inputUsername.isEmpty() || inputMobile.isEmpty() || inputPassword.isEmpty() || inputConfirm.isEmpty()){
                passwordRegister.setText("");
                passwordConfirm.setText("");
                registerMessage.setForeground(Color.RED);
                registerMessage.setText("Please fill in all entries");
            }
            //Else starts creating account
            else if (nameMatcher.find()) {
                usernameRegister.setText("");
                registerMessage.setForeground(Color.RED);
                registerMessage.setText("Username must not contain special characters");
            }

            else if (mobileMatcher.find()){
                mobileNumberRegister.setText("");
                registerMessage.setForeground(Color.RED);
                registerMessage.setText("Phone number must be numeric");
            }

            else if (!inputPassword.equals(inputConfirm)) {
                registerMessage.setForeground(Color.RED);
                registerMessage.setText("Password entries do not match");
                passwordRegister.setText("");
                passwordConfirm.setText("");
            }

            else if (inputPassword.length() < 8){
                registerMessage.setForeground(Color.RED);
                registerMessage.setText("Password is less than 8 characters");
                passwordRegister.setText("");
                passwordConfirm.setText("");
            }
        }
    }


    private class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command){
                case "Log In" -> {
                    logInMessage.setText("Log In");
                    checkLogIn();
                }
                case "Register" -> {
                    mainFrame.dispose();
                    registerPage = new RegisterPage();
                }
            }
        }
    }
}
