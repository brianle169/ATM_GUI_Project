package ATM_GUI;

import javax.swing.*;
import java.awt.*;

public class ATM {
    
    private MainPanel mainPanel;
    private JFrame mainFrame;
    private Account currentUser;

    public ATM(Account currentUser){
        this.currentUser = currentUser;
        mainFrame = new JFrame("ATM");
        mainFrame.setSize(Constant.MAIN_WIDTH,Constant.MAIN_HEIGHT);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setBackground(Color.RED);
        mainFrame.setLayout(null);
        mainFrame.setResizable(false);
        createMainPanel(this.currentUser);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void createMainPanel(Account currentUser) {
        mainPanel = new MainPanel(currentUser);
        mainFrame.add(mainPanel);
    }
}
