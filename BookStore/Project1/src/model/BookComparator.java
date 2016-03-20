package model;

import java.util.Comparator;

public class BookComparator implements Comparator<Book>{
    @Override
    public int compare(Book book1, Book book2) {
        String stringBook1 = book1.getTitle() + ", " + book1.getAuthor() + ", " + book1.getPrice();
        String stringBook2 = book2.getTitle() + ", " + book2.getAuthor() + ", " + book2.getPrice();
        return stringBook1.compareTo(stringBook2);
    }
}
