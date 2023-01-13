package ATM_GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Account {
    public int accountID;
    private int ID;
    private String username;
    private double balance;
    private String mobileNumber;
    private String password;
    private Random random = new Random();


    public Account(){
        initializeID();
        ID = accountID;
        balance = 0.0;
        username = "";
        mobileNumber = "";
        password = String.valueOf(random.nextInt(1_000_000,9_999_999));
    }

    private void initializeID(){
        try {
            accountID = updateAccountID();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IndexOutOfBoundsException exception){
            accountID = 1000000;
        }
        accountID++;
    }

    private int updateAccountID() throws FileNotFoundException, IndexOutOfBoundsException {
        File data = new File("test_in.txt");
        Scanner reader = new Scanner(data);
        ArrayList<Integer> ID_List = new ArrayList<>();

        while (reader.hasNextLine()){
            ID_List.add(Integer.parseInt(reader.nextLine().split(",")[0]));
        }
        reader.close();
        return ID_List.get(ID_List.size()-1);
    }

    public Account(String username, String mobileNumber){
        initializeID();
        ID = accountID;
        this.username = username;
        this.mobileNumber = mobileNumber;
        balance = 0.0;
        password = String.valueOf(random.nextInt(1_000_000,9_999_999));
    }

    protected Account(int ID, String username, String mobileNumber, double balance, String password){
        this.ID = ID;
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.balance = balance;
        this.password = password;
    }

    public Account(String username, String mobileNumber, double balance){
        initializeID();
        ID = accountID;
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.balance = balance;
        password = String.valueOf(random.nextInt(10_000_000,99_999_999));
    }

    public void withdraw(double amount){
        this.balance -= amount;
    }

    public void deposit(double amount){
        this.balance += amount;
    }

    public int getID() {
        return ID;
    }

    public void setPassword(String password){
        this.password = password;
    }

    private String getPassword(){
        return password;
    }

    public boolean checkPassword(String input){
        return input.equals(this.password);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return this.balance;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public String getUsername() {
        return this.username;
    }

    public String toString(){
        return String.format("> Account ID: %d\n\n> Customer name: %s\n\n> Mobile number: %s\n\n> Balance: %,.2f VND",
                getID(),getUsername(),getMobileNumber(),getBalance());
    }

    protected StringBuilder getUpdatedData(){
        StringBuilder updatedData = new StringBuilder();
        updatedData.append(getID()).append(",").append(getUsername()).append(",");
        updatedData.append(getMobileNumber()).append(",").append(getBalance()).append(",");
        updatedData.append(getPassword());
        return updatedData;
    }
}


