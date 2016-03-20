package model;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



public class Register implements BookList {
    private Map<Book, Integer> booksInBasket = new TreeMap<Book, Integer>(new BookComparator());
    private Map<Book, Integer> booksInLibrary = new TreeMap<Book, Integer>(new BookComparator());

    private BigDecimal totalCostInBasket;
    
    public Map<Book, Integer> getAllBooksInLibrary() {
        Map<Book, Integer> getAllBooksInLibrary = new TreeMap<Book, Integer>(new BookComparator());
        for (Map.Entry<Book, Integer> book : booksInLibrary.entrySet()) {
            getAllBooksInLibrary.put(book.getKey(), book.getValue());
        }
        return getAllBooksInLibrary;
    }

    public boolean addToBasket(Book b, int quantity) {
        boolean added = false;
        String stringWantedBookInBasket = b.getTitle() + ", " + b.getAuthor() + ", " + b.getPrice();
        try{
            for(Map.Entry<Book, Integer> book : booksInBasket.entrySet()){
                String stringBookInBasket = book.getKey().getTitle() + ", " + book.getKey().getAuthor() + ", " + book.getKey().getPrice();
                if(stringWantedBookInBasket.equalsIgnoreCase(stringBookInBasket)){
                    int value = booksInBasket.get(book.getKey());
                    booksInBasket.put(b, quantity + value);
                    added = true;
                }
            }
            if(!booksInBasket.containsKey(b)){
                booksInBasket.put(b, quantity);
                added = true;
            }
        }
        catch(Exception exception){
            added = false;
        }
        return added;
    }
    
    public String addToBasketAlternative(int index, int quantity) {
        String status = null;
        List<String> allBooksInLibrary = new ArrayList<String>(); 

        for(Map.Entry<Book, Integer> book : booksInLibrary.entrySet()){
            String stringBook = book.getKey().getTitle() + ", " + book.getKey().getAuthor() + ", " + book.getKey().getPrice();
            allBooksInLibrary.add(stringBook);
        }
        
        Collections.sort(allBooksInLibrary);
        String selectedBook = allBooksInLibrary.get(index);
        String[] bookValues = selectedBook.split(", ");
        BigDecimal bookPrice = convertStringToBigDecimal(bookValues[2]);
        Book wantedBook = new Book(bookValues[0], bookValues[1], bookPrice);
        
        try{
            for(Map.Entry<Book, Integer> book : booksInBasket.entrySet()){
                if(booksInBasket.containsKey(book.getKey())){
                    int value = booksInBasket.get(book.getKey());
                    booksInBasket.put(wantedBook, quantity + value);
                }
                else{
                    booksInBasket.put(wantedBook, quantity);
                }
            }            
            status = "You have added " + quantity + " copies of the following book to basket:\n" + selectedBook;
        }
        catch(Exception e){
            status = "Error when adding the book to basket";
        }
        return status;
    }

    public Map<Book, Integer> getAllBooksInBasket() {
        Map<Book, Integer> getAllBooksInBasket = new TreeMap<Book, Integer>(new BookComparator());
        int totalQuantity = 0;
        List<BigDecimal> prices = new ArrayList<BigDecimal>();
        for (Map.Entry<Book, Integer> book : booksInBasket.entrySet()) {
            getAllBooksInBasket.put(book.getKey(), book.getValue());
            prices.add(book.getKey().getPrice());
            totalQuantity += book.getValue(); 
        }
        BigDecimal totalPrices = BigDecimal.ZERO;
        for (int i = 0; i < prices.size(); i++) 
        {
            totalPrices = totalPrices.add(prices.get(i));
        }
        BigDecimal totalCost = totalPrices.multiply(new BigDecimal(String.valueOf(totalQuantity)));
        setTotalCostInBasket(totalCost);
        
        return getAllBooksInBasket;
    }
    
    public String removeElementInBasket(int index){
            String status = null;
            List<String> allBooksInBasket = new ArrayList<String>(); 

            try{
                for(Map.Entry<Book, Integer> book : booksInBasket.entrySet()){
                    String stringBook = book.getKey().getTitle() + ", " + book.getKey().getAuthor() + ", " + book.getKey().getPrice() + ", " + book.getValue(); 
                    allBooksInBasket.add(stringBook);
                }
            
                Collections.sort(allBooksInBasket);
                String selectedBook = allBooksInBasket.get(index);
                String[] bookValues = selectedBook.split(", ");
                BigDecimal bookPrice = convertStringToBigDecimal(bookValues[2]);
                Book wantedBook = new Book(bookValues[0], bookValues[1], bookPrice);
            
                booksInBasket.remove(wantedBook);
                status = "You have removed the following books from basket:\n" + selectedBook;
            }
            catch(Exception e){
                status = "The books could not be removed";
            }
            return status;
        }

    @Override
    public boolean add(Book tmpBook, int amount) {
        boolean added = false;
        
        for (Map.Entry<Book, Integer> book : booksInLibrary.entrySet()) {
            if (book.getKey() == tmpBook) {
                amount += book.getValue();
                book.setValue(amount);
                added = true;
            }
        }
        if (added == false) {
            booksInLibrary.put(tmpBook, amount);
            added = true;
        }
        
        return added;
    }
        
    @Override
    public Book[] list(String searchString) {
        List<Book> foundBooks = new ArrayList<Book>();
        Book b = null;
        Book[] foundBooksArr = null;
        for (Map.Entry<Book, Integer> book : booksInLibrary.entrySet()) {
            b = book.getKey();
            String bookString = b.getTitle() + ", " + b.getAuthor() + ", " + b.getPrice().toString();
            if (bookString.contains(searchString)) {
                foundBooks.add(b);
            }
        }
        foundBooksArr = new Book[foundBooks.size()];
        foundBooksArr = foundBooks.toArray(foundBooksArr);
        
        return foundBooksArr;
    }

    @Override
    public int[] buy(Book... books) {
        List<Book> booksInList = new ArrayList<Book>(Arrays.asList(books));
        Map<Book, Integer> updateBooksInBasket = new TreeMap<Book, Integer>(new BookComparator());
        updateBooksInBasket.putAll(booksInLibrary);
        
        boolean bookDoesNotExist = false;
        int ok = 0;
        int notInStock = 0;
        int doesNotExist = 0;
        int removeBooks = 0;
        String bookInBasketString = null;

        for(Book b : books){
            bookInBasketString = b.getTitle() + ", " + b.getAuthor() + ", " + b.getPrice().toString();
            int amountOfSameBookInArray = 0;
            while(booksInList.contains(b)){
                amountOfSameBookInArray++;
                booksInList.remove(b);
            }
            doesNotExist = 0;
            int updatedValue = updateBooksInBasket.get(b);
            for(Map.Entry<Book, Integer> bookInLibrary : updateBooksInBasket.entrySet()){
                String bookInLibraryString = bookInLibrary.getKey().getTitle() + ", " + bookInLibrary.getKey().getAuthor() + ", " + bookInLibrary.getKey().getPrice().toString();
                if(bookInBasketString.equalsIgnoreCase(bookInLibraryString)){
                    if (amountOfSameBookInArray > bookInLibrary.getValue()) {
                        notInStock = 1;
                        break;
                    }
                    else{
                        removeBooks++;
                        updatedValue--;
                        for(Map.Entry<Book, Integer> updateBookInBasket : updateBooksInBasket.entrySet()){
                            String stringUpdateBook = updateBookInBasket.getKey().getTitle() + ", " + updateBookInBasket.getKey().getAuthor() + ", " + updateBookInBasket.getKey().getPrice().toString();
                            if(bookInLibraryString.equalsIgnoreCase(stringUpdateBook)){
                                updateBooksInBasket.put(b, updatedValue);
                                if(removeBooks == books.length){
                                    for(Map.Entry<Book, Integer> updateBook : updateBooksInBasket.entrySet()){
                                        booksInLibrary.remove(updateBook.getKey());
                                        booksInLibrary.putAll(updateBooksInBasket);
                                    }
                                }
                            }
                        }
                    }
                }
                else{
                    doesNotExist += 1;
                    if(doesNotExist == booksInLibrary.size()){
                        bookDoesNotExist = true;
                    }   
                }
            }
        }
        if(notInStock == 0 && bookDoesNotExist == false){
            ok = 1;
            doesNotExist = 0;
        }
        else if(notInStock == 1){
            ok = 0;
            doesNotExist = 0;
        }
        else if(bookDoesNotExist){
            doesNotExist = 1;
            ok = 0;
        }
        
        int[] status = new int[] {ok, notInStock, doesNotExist};
        
        //If i want a status for specific book I must change the int[] buy 
        //method to a for example Map<Book, Integer> buy method. Or I can make an output in backend 
        //but I want all of my outputs that concerns the user in frontend.  
        
        return status;
    }

    public void clearBasket(){
        booksInBasket.clear();    
    }

    public void setTotalCostInBasket(BigDecimal totalCostInBasket) {
        this.totalCostInBasket = totalCostInBasket;
    }

    public BigDecimal getTotalCostInBasket() {
        return totalCostInBasket;
    }
    
    private BigDecimal convertStringToBigDecimal(String value){
        BigDecimal bigDecimalValue = null;
        if(value.contains(",")){
            value = value.replaceAll(",", "");
        }
        bigDecimalValue = new BigDecimal(value);
        
        return bigDecimalValue;
    }
}