package console;

import controller.Controller;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.util.TreeMap;

import model.Book;
import model.BookComparator;
import model.Register;

public class Console {
    private static final Register bookLibrary = new Register();
    private static final Controller controller = new Controller(bookLibrary);
    private static Map<Book, Integer> boughtBooks = new TreeMap<Book, Integer>(new BookComparator());
    
    public static void main(String[] args){
        Console console = new Console();
        console.startApplication();
    }   
    
    public void startApplication(){
        int selection = startMenu();
        
        switch(selection){
            case 1:
                adminArea();
                break;
            case 2:
                customerArea();
                break;
            case 3:
                System.out.println("Goodbye!");
                break;
        }
    }
    
    private int startMenu(){
        Scanner input = new Scanner(System.in);
        
        System.out.println("Start Menu");
        System.out.println("-------------------------");
        System.out.println("1 - Go to Admin area");
        System.out.println("2 - Go to Customer area");
        System.out.println("3 - Quit");
        
        String stringSelection = input.nextLine();
        while(!stringSelection.matches("[1-3]")){
            System.out.println("Your input is not an option");
            System.out.println("Please enter a valid input: ");
            stringSelection = input.nextLine();
        }
        int selection = Integer.parseInt(stringSelection);
        
        return selection;
    }
    
    private void adminArea(){
        int selection = adminMenu();
        
        switch(selection){
            case 1:
                System.out.println("All books in library:\n" + getAllBooksInLibrary());
                if(proceed()){
                    adminArea();
                }
                break;
            case 2:
                System.out.println(add());
                if(proceed()){
                    adminArea();
                }
                break;
            case 3:
                startApplication();
                break;
            case 4:
                break;
        }
    }
    
    private int adminMenu(){
        Scanner input = new Scanner(System.in);
        
        System.out.println("Admin Menu");
        System.out.println("-------------------------");
        System.out.println("1 - Get all books in library");
        System.out.println("2 - Add new books to library");
        System.out.println("-----------");
        System.out.println("3 - Go back to Start Menu");
        System.out.println("4 - Quit");
        
        String stringSelection = input.nextLine();
        while(!stringSelection.matches("[1-4]")){
            System.out.println("Your input is not an option");
            System.out.println("Please enter a valid input: ");
            stringSelection = input.nextLine();
        }
        int selection = Integer.parseInt(stringSelection);
        
        return selection;
    }
    
    private void customerArea(){
        int selection = customerMenu();
        
        switch(selection){
            case 1:
                System.out.println("All books in library:\n" + getAllBooksInLibrary());
                if(proceed()){
                    customerArea();
                }
                break;
            case 2:
                System.out.println("All books in basket:\n" + getAllBooksInBasket() + "Total Cost: " + getTotalCostInBasket() + " kr");
                if(proceed()){
                    customerArea();
                }
                break;
            case 3:
                System.out.println("All bought books:\n" + getAllBoughtBooks());
                if(proceed()){
                    customerArea();
                }
                break;
            case 4:
                System.out.println("Result of the search:\n" + bookSearch());
                if(proceed()){
                    customerArea();
                }
                break;
            case 5:
                System.out.println(addBooksToBasket());
                if(proceed()){
                    customerArea();
                }
                break;
            case 6:
                System.out.println(removeBooksFromBasket());
                if(proceed()){
                    customerArea();
                }
                break;
            case 7:
                System.out.println(buy());
            if(proceed()){
                customerArea();
            }
            case 8:
                startApplication();
                break;
            case 9:
                break;           
        }
    }
    
    private int customerMenu(){      
        Scanner input = new Scanner(System.in);
            
        System.out.println("Customer Menu");
        System.out.println("-------------------------");
        System.out.println("1 - Get all books in library");
        System.out.println("2 - Get all books in basket");
        System.out.println("3 - Get all books you have bought");
        System.out.println("-----------");
        System.out.println("4 - Search for a specific book");
        System.out.println("-----------");
        System.out.println("5 - Add books to basket");
        System.out.println("6 - Remove books from basket");
        System.out.println("7 - Buy books in basket");
        System.out.println("-----------");
        System.out.println("8 - Go back to Start Menu");
        System.out.println("9 - Quit");
        
        String stringSelection = input.nextLine();
        while(!stringSelection.matches("[1-9]")){
            System.out.println("Your input is not an option");
            System.out.println("Please enter a valid input: ");
            stringSelection = input.nextLine();
        }
        int selection = Integer.parseInt(stringSelection);
        
        return selection;
    }
    
    private String getAllBooksInLibrary(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Book, Integer> book : controller.getAllBooksInLibrary().entrySet()){
            sb.append(book.getKey().getTitle() + ", " + book.getKey().getAuthor() + ", " + book.getKey().getPrice().toString() + ", " + book.getValue() + "\n");
        }
        String allBooksInLibrary = sb.toString();
        
        if(allBooksInLibrary == null || allBooksInLibrary.isEmpty()){
            allBooksInLibrary = "Our library is empty, we have no books to offer :(\n";
        }        
        
        return allBooksInLibrary;
    }
    
    private String getAllBooksInBasket(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Book, Integer> book : controller.getAllBooksInBasket().entrySet()){
            sb.append(book.getKey().getTitle() + ", " + book.getKey().getAuthor() + ", " + book.getKey().getPrice().toString() + ", " + book.getValue() + "\n");
        }
        String allBooksInBasket = sb.toString();
        
        if(allBooksInBasket == null || allBooksInBasket.isEmpty()){
            allBooksInBasket = "You have no books in your basket\n";
        }
        
        return allBooksInBasket;
    }
    
    private String getAllBoughtBooks(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Book, Integer> book : boughtBooks.entrySet()){
            sb.append(book.getKey().getTitle() + ", " + book.getKey().getAuthor() + ", " + book.getKey().getPrice().toString() + ", " + book.getValue() + "\n");
        }
        String allBoughtBooks = sb.toString();
        
        if(allBoughtBooks.isEmpty()){
            allBoughtBooks = "You have not bought any books";
        }
        return allBoughtBooks;
    }
    
    private String add(){
        Scanner input = new Scanner(System.in);
        String status = null;
        
        System.out.println("Write the title of the book: ");
        String bookTitle = input.nextLine();
        while(bookTitle.equals("")){
            System.out.println("The book needs to have a title!");
            System.out.println("Please enter a valid title for the book: ");
            bookTitle = input.nextLine();
        }
        System.out.println("Write the author of the book: ");
        String bookAuthor = input.nextLine();
        while(bookAuthor.equals("")){
            System.out.println("The book needs to have an author!");
            System.out.println("Please enter a valid author for the book: ");
            bookAuthor = input.nextLine();
        }
        System.out.println("Write the price of the book: ");
        String stringPrice = input.nextLine();
        while(!stringPrice.matches("[0-9.]*") || stringPrice.equals("0")){
            System.out.println("The price needs to be a number");
            System.out.println("Please enter a valid price for the book: ");
            stringPrice = input.nextLine();
        }
        BigDecimal price = new BigDecimal(stringPrice);
        System.out.println("Write how many examples of this book you want to add: ");
        String stringAmount = input.nextLine();
        while(!stringAmount.matches("[0-9]+")){
            System.out.println("The amount needs to be a number");
            System.out.println("Please enter a valid amount: ");
            stringAmount = input.nextLine();
        }
        int amount = Integer.parseInt(stringAmount);
        
        Book tmpBook = new Book(bookTitle, bookAuthor, price);
        
        boolean success = controller.add(tmpBook, amount); 
        
        if (success){
            status = amount + " copies of " + tmpBook.getTitle() + " has been added";
        }
        else{
            status = "The book(s) could not be added";
        }
        return status;
    }
    
    private String bookSearch(){
        Scanner input = new Scanner(System.in);
        System.out.println("Write either the author of the book or the title of the book:");
        String searchString = input.nextLine();
        
        Book[] searchedBooks = controller.list(searchString);
        
        StringBuilder sb = new StringBuilder();
        for(Book b : searchedBooks){
            sb.append(b.getTitle() + ", " + b.getAuthor() + ", " + b.getPrice().toString() + "\n");
        }
        
        String stringSearchedBooks = sb.toString();
        
        if(searchedBooks == null || stringSearchedBooks.isEmpty()){
            stringSearchedBooks = "Your search didn't generate in any matched results";
        }
        
        return stringSearchedBooks;
    }
    
    //One way to add books to Basket
    private String addBooksToBasket(){
        Scanner input = new Scanner(System.in);
        List<String> allBooksInLibrary = new ArrayList<String>(); 
        String status = null;
        
        System.out.println("All Books in Library: ");
        int i = 0;
        for(Map.Entry<Book, Integer> book : controller.getAllBooksInLibrary().entrySet()){
            i++;
            String stringBook = book.getKey().getTitle() + ", " + book.getKey().getAuthor() + ", " + book.getKey().getPrice();
            allBooksInLibrary.add(stringBook);
            System.out.println(i + " - BOOK: " + stringBook + ", QUANTITY: " + book.getValue());
        }
        
        System.out.println("Write the number of the book you want to buy");
        String stringNumber = input.nextLine();
        while(!stringNumber.matches("[1-" + i + "]")){
            System.out.println("We dont have a book in our library with this number");
            System.out.println("Please enter a valid number: ");
            stringNumber = input.nextLine();
        }
        int number = Integer.parseInt(stringNumber);
        int numberForSelection = number - 1;
        
        System.out.println("Write how many examples of this book do you want to add: ");
        String stringAmount = input.nextLine();
        while(!stringAmount.matches("[0-9]+") || stringAmount.equals("0")){
            System.out.println("The quantity needs to be a number and cannot be 0");
            System.out.println("Please enter a valid quantity: ");
            stringAmount = input.nextLine();
        }
        int quantity = Integer.parseInt(stringAmount);
        
        Collections.sort(allBooksInLibrary);
        String selectedBook = allBooksInLibrary.get(numberForSelection);
        String[] bookValues = selectedBook.split(", ");
        BigDecimal bookPrice = convertStringToBigDecimal(bookValues[2]);
        Book wantedBook = new Book(bookValues[0], bookValues[1], bookPrice);
        
        boolean success = controller.addToBasket(wantedBook, quantity);
        
        if(success == false){
            status = "This book doesn't exist in our library";
        }
        else{
            status = "You have added " + quantity + " copies of the following book to basket:\n" + selectedBook;
        }
        return status;
    }
    
    //Another way to add books to basket, maybe better? Positive: more code in backend and less code in frontend
    private String addBooksToBasketAlternative(){
        Scanner input = new Scanner(System.in);
        
        System.out.println("All Books in library: ");
        int i = 0;
        for(Map.Entry<Book, Integer> book : controller.getAllBooksInLibrary().entrySet()){
            i++;
            String stringBook = book.getKey().getTitle() + ", " + book.getKey().getAuthor() + ", " + book.getKey().getPrice();
            System.out.println(i + " - BOOK: " + stringBook + ", QUANTITY: " + book.getValue());
        }
        System.out.println("Write the number of the book you want to buy");
        String stringNumber = input.nextLine();
        while(!stringNumber.matches("[1-" + i + "]")){
            System.out.println("We dont have a book in our library with this number");
            System.out.println("Please enter a valid number: ");
            stringNumber = input.nextLine();
        }
        int number = Integer.parseInt(stringNumber);
        int index = number - 1;
        
        System.out.println("Write how many examples of this book do you want to add: ");
        String stringAmount = input.nextLine();
        while(!stringAmount.matches("[0-9]+") || stringAmount.equals("0")){
            System.out.println("The quantity needs to be a number and cannot be 0");
            System.out.println("Please enter a valid quantity: ");
            stringAmount = input.nextLine();
        }
        int quantity = Integer.parseInt(stringAmount);
        
        String status = controller.addToBasketAlternative(index, quantity);

        return status;
    }
    
    private String removeBooksFromBasket(){
        String status = null;
        if(!controller.getAllBooksInBasket().isEmpty()){
            Scanner input = new Scanner(System.in);
            
        
            System.out.println("All Books in basket: ");
            int i = 0;
            for(Map.Entry<Book, Integer> book : controller.getAllBooksInBasket().entrySet()){
                i++;
                String stringBook = book.getKey().getTitle() + ", " + book.getKey().getAuthor() + ", " + book.getKey().getPrice();
                System.out.println(i + " - BOOK: " + stringBook + ", QUANTITY: " + book.getValue());
            }
            System.out.println("Write the number of the book you want to remove from basket");
            String stringNumber = input.nextLine();
            while(!stringNumber.matches("[1-" + i + "]")){
                System.out.println("We dont have a book in our library with this number");
                System.out.println("Please enter a valid number: ");
                stringNumber = input.nextLine();
            }
            int number = Integer.parseInt(stringNumber);
            int index = number - 1;
        
            status = controller.removeElementInBasket(index);
        }
        else{
            status = "Your basket is empty, there is nothing to remove";
        }
        return status;
    }
    
    private String buy() {
        String stringStatus = null;
        if(!controller.getAllBooksInBasket().isEmpty()){
            ArrayList<Book> books = new ArrayList<Book>();
            for(Map.Entry<Book, Integer> book : controller.getAllBooksInBasket().entrySet()){
                Book tmpBook = new Book(book.getKey().getTitle(), book.getKey().getAuthor(), book.getKey().getPrice());
                int quantity = book.getValue();
                for(int i = 0; i<quantity; i++){
                    books.add(tmpBook);
                }
            }
        
            Book[] booksArr = new Book[books.size()];
            for (int i=0; i < booksArr.length; i++){
                booksArr[i] = books.get(i);
            }
            
            int[] status = controller.buy(booksArr);
            if(status[2] == 1){
                stringStatus = "Status (Does_Not_Exist): Book(s) does not exist in our store";
            }
        
            else if(status[1] == 1){
                stringStatus = "Status (Not_In_Stock): Book(s) is not in stock";
            }
        
            else if(status[0] == 1){
                stringStatus = "Status (OK): Thank you for buying book(s) in our store";
                for(Map.Entry<Book, Integer> book : controller.getAllBooksInBasket().entrySet()){
                    boughtBooks.put(book.getKey(), book.getValue());
                }
                controller.clearBasket();
            }
            else{
                stringStatus = "Status (Error): Something is wrong with the program";
            }
        }
        else{
            stringStatus = "Your basket is empty, there is nothing to buy";
        }
        return stringStatus;
    }
    
    private String getTotalCostInBasket(){ 
        String totalCost = controller.getTotalCostInBasket().toString();   
        return totalCost;
    }
    
    private boolean proceed(){
        boolean proceed = true;
        Scanner input = new Scanner(System.in);
        System.out.println("write 'Q' if you want to quit, else if you want to continue just press a random keyboard button except 'Q'");
        if(input.nextLine().equalsIgnoreCase("Q")){
            proceed = false;
        }
        return proceed;
    }
    
    private BigDecimal convertStringToBigDecimal(String value){
        if(value.contains(",")){
            value = value.replaceAll(",", "");
        }
        BigDecimal bigDecimalValue = new BigDecimal(value);

        return bigDecimalValue;
    }
}
