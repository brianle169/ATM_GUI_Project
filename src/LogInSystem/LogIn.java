package LogInSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogIn implements ActionListener {

    JFrame logInFrame;
    JPanel inputPanel;
    JPanel buttonPanel;
    JTextField usernameField;
    JPasswordField passwordField;
    private String actionLogIn;
    private String actionHelp;

    LogIn(){
        //Basic set up:
        logInFrame = new JFrame();
        logInFrame.setSize(400,200);
        logInFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logInFrame.setTitle("Log In");
        logInFrame.setLayout(null);
        logInFrame.setLocationRelativeTo(null);

        actionHelp = "Help";
        actionLogIn = "Log In";

        createInputPanel();
        createButtonPanel();

        logInFrame.setVisible(true);
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));
        buttonPanel.setBackground(Color.CYAN);
        buttonPanel.setBounds(112,110,160,30);

        JButton logInButton = new JButton("Log In");
        logInButton.addActionListener(this);
        logInButton.setActionCommand(actionLogIn);
        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(this);
        helpButton.setActionCommand(actionHelp);

        buttonPanel.add(logInButton);
        buttonPanel.add(helpButton);

        logInFrame.add(buttonPanel);
    }

    private void createInputPanel() {
        inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setBounds(42,20,300,80);

        JLabel usernameLabel = createLabels("Username: ");
        usernameLabel.setBounds(5,10,80,20);
        JLabel passwordLabel = createLabels("Password: ");
        passwordLabel.setBounds(5,50,80,20);

        inputPanel.add(usernameLabel);
        inputPanel.add(passwordLabel);
        inputPanel.add(createUsernameField());
        inputPanel.add(createPasswordField());

        logInFrame.add(inputPanel);
    }

    private JPasswordField createPasswordField(){
        passwordField = new JPasswordField();
        passwordField.setBounds(90,51,170,20);
        passwordField.setActionCommand(actionLogIn);
        passwordField.addActionListener(this);
        return passwordField;
    }

    private JTextField createUsernameField(){
        usernameField = new JTextField();
        usernameField.setBounds(90,11,170,20);
        return usernameField;
    }

    private JLabel createLabels(String text){
        JLabel newLabel = new JLabel();
        newLabel.setText(text);
        newLabel.setFont(new Font("Arial",Font.PLAIN,15));
        newLabel.setForeground(Color.BLACK);
        return newLabel;
    }

    private boolean passwordCorrect(char[] inputPassword){
        String initialPassword = "07052016";
        String input = String.valueOf(inputPassword);
        return (input.equals(initialPassword));
    }

    private boolean usernameCorrect(String inputUsername){
        String initialUsername = "Binobo";
        return inputUsername.equals(initialUsername);
    }

    private void logInSuccessful(){
        JOptionPane.showMessageDialog(null,"Successful! Welcome!","Log In",JOptionPane.INFORMATION_MESSAGE);
        logInFrame.dispose();
    }

    private void logInFailed(){
        JOptionPane.showMessageDialog(null,"Failed to log in! Please try again!","Log In",JOptionPane.WARNING_MESSAGE);
        usernameField.setText("");
        passwordField.setText("");
    }

    private void setPasswordIncorrect(){
        JOptionPane.showMessageDialog(null,"Password incorrect! Please try again!","Log In",JOptionPane.WARNING_MESSAGE);
        passwordField.setText("");
    }

    private void setUsernameIncorrect(){
        JOptionPane.showMessageDialog(null,"Cannot recognize username! Please try again!","Log In",JOptionPane.WARNING_MESSAGE);
        usernameField.setText("");
        passwordField.setText("");
    }

    private void help(){
        JOptionPane.showMessageDialog(null,"Nothing to help","Log In",JOptionPane.WARNING_MESSAGE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (actionLogIn.equals(command)){
            char[] inputPassword = passwordField.getPassword();
            String inputUsername = usernameField.getText();
            if (passwordCorrect(inputPassword) && usernameCorrect(inputUsername)){
                logInSuccessful();
            }
            else if (usernameCorrect(inputUsername) && !passwordCorrect(inputPassword)){
                setPasswordIncorrect();
            }
            else if (!usernameCorrect(inputUsername) && passwordCorrect(inputPassword)){
                setUsernameIncorrect();
            }
            else {
                logInFailed();
            }
        }
        else help();
    }
}

class LogInDriver{
    public static void main(String[] args) {
        new LogIn();
    }
}
