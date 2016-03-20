package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import java.util.StringTokenizer;

import model.Book;
import model.Register;

public class LoadValues {

    private static Register register;

    public LoadValues(Register register) {
        this.register = register;
    }

    /**
     * loads all books
     */
    public static void loadBooks() {
        File file = new File("textfile.txt");
        String line;
        BufferedReader read;

        try {
            read = new BufferedReader(new FileReader(file));

            while ((line = read.readLine()) != null) {
                StringTokenizer token = new StringTokenizer(line, ";");

                String tmpTitle = token.nextToken();
                String tmpAuthor = token.nextToken();
                String stringPrice = token.nextToken();
                if (stringPrice.contains(",")) {
                    stringPrice = stringPrice.replaceAll(",", "");
                }
                BigDecimal tmpPrice = new BigDecimal(stringPrice);

                int tmpAmount = Integer.parseInt(token.nextToken());

                Book tmpBook = new Book(tmpTitle, tmpAuthor, tmpPrice);
                            
                register.add(tmpBook, tmpAmount);
            }
            read.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Cannot find file");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
