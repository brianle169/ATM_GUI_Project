package ATM_GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class testIO {
    public static void main(String[] args) throws FileNotFoundException {
        Account a1 = new Account("Binobo","0987654321",560000);
        Account a2 = new Account("Bibibi","0823030523",400000);

        File logIn = new File("D:\\Java\\Fun Stuff\\test_in.txt");
        File output = new File("D:\\Java\\Fun Stuff\\test_out.txt");
        StringBuilder testString = new StringBuilder();

        Scanner reader = new Scanner(logIn);
        while (reader.hasNextLine()){
            testString.append(reader.nextLine());
            testString.append("\n");
        }

        a1.deposit(10000);

        PrintWriter writer = new PrintWriter(logIn);
        testString.append(a1.getUpdatedData());
        writer.print(testString);


        reader.close();
        writer.close();
    }
}
