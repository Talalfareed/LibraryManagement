import java.util.*;
import java.io.*;

public class Library {
    List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    // View Available Books method
    public List<Book> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getAvailability()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    // Display Available Books Sample
    public void displayAvailableBooksSample(int sampleSize) {
        List<Book> availableBooks = getAvailableBooks();

        if (availableBooks.isEmpty()) {
            System.out.println("No available books in the library.");
            return;
        }

        System.out.println("\n--- Available Books ---");
        System.out.println("Showing " + Math.min(sampleSize, availableBooks.size()) + " sample books of " + availableBooks.size() + " total available:");

        for (int i = 0; i < Math.min(sampleSize, availableBooks.size()); i++) {
            Book book = availableBooks.get(i);
            System.out.println((i + 1) + ". " +
                    "Title: " + book.getTitle() + ", " +
                    "Author: " + book.getAuthor() + ", " +
                    "ISBN: " + book.getISBN() + ", " +
                    "Year: " + book.getPubYear());
        }

        if (availableBooks.size() > sampleSize) {
            System.out.println("\n... and " + (availableBooks.size() - sampleSize) + " more available books");
        }

        System.out.println("\nSummary:");
        System.out.println("Total books in library: " + books.size());
        System.out.println("Available books: " + availableBooks.size());
        System.out.println("Borrowed books: " + (books.size() - availableBooks.size()));
    }

    // Sorting Methods
    public void sortByTitle() {
        Collections.sort(books, (b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
        System.out.println("✅ Books sorted by title (A-Z)");
    }

    public void sortByTitleDescending() {
        Collections.sort(books, (b1, b2) -> b2.getTitle().compareToIgnoreCase(b1.getTitle()));
        System.out.println("✅ Books sorted by title (Z-A)");
    }

    public void sortByAuthor() {
        Collections.sort(books, (b1, b2) -> {
            String lastName1 = extractLastName(b1.getAuthor());
            String lastName2 = extractLastName(b2.getAuthor());
            return lastName1.compareToIgnoreCase(lastName2);
        });
        System.out.println("✅ Books sorted by author's last name (A-Z)");
    }

    public void sortByAuthorDescending() {
        Collections.sort(books, (b1, b2) -> {
            String lastName1 = extractLastName(b1.getAuthor());
            String lastName2 = extractLastName(b2.getAuthor());
            return lastName2.compareToIgnoreCase(lastName1);
        });
        System.out.println("✅ Books sorted by author's last name (Z-A)");
    }

    public void sortByYear() {
        Collections.sort(books, (b1, b2) -> {
            // First, compare by year
            int yearCompare = b1.getPubYear().compareTo(b2.getPubYear());
            if (yearCompare != 0) {
                return yearCompare; // Different years - sort by year
            }

            // Same year - sort by author's last name
            String lastName1 = extractLastName(b1.getAuthor());
            String lastName2 = extractLastName(b2.getAuthor());

            return lastName1.compareToIgnoreCase(lastName2);
        });
        System.out.println("✅ Books sorted by year (oldest first), then by author's last name");
    }

    public void sortByYearDescending() {
        Collections.sort(books, (b1, b2) -> {
            // First, compare by year (descending)
            int yearCompare = b2.getPubYear().compareTo(b1.getPubYear());
            if (yearCompare != 0) {
                return yearCompare; // Different years - sort by year (newest first)
            }

            // Same year - sort by author's last name
            String lastName1 = extractLastName(b1.getAuthor());
            String lastName2 = extractLastName(b2.getAuthor());

            return lastName1.compareToIgnoreCase(lastName2);
        });
        System.out.println("✅ Books sorted by year (newest first), then by author's last name");
    }

    public void sortByISBN() {
        Collections.sort(books, (b1, b2) -> b1.getISBN().compareTo(b2.getISBN()));
        System.out.println("✅ Books sorted by ISBN (ascending)");
    }

    public void sortByISBNDescending() {
        Collections.sort(books, (b1, b2) -> b2.getISBN().compareTo(b1.getISBN()));
        System.out.println("✅ Books sorted by ISBN (descending)");
    }

    public void sortByAvailability() {
        Collections.sort(books, (b1, b2) -> Boolean.compare(b2.getAvailability(), b1.getAvailability()));
        System.out.println("✅ Books sorted by availability (available first)");
    }

    public void sortByAvailabilityDescending() {
        Collections.sort(books, (b1, b2) -> Boolean.compare(b1.getAvailability(), b2.getAvailability()));
        System.out.println("✅ Books sorted by availability (borrowed first)");
    }

    // Helper method to extract last name from author string
    private String extractLastName(String author) {
        if (author == null || author.isEmpty()) return "";

        // Split by commas to get parts
        String[] parts = author.split(",");
        if (parts.length > 0) {
            return parts[0].trim(); // First part before first comma is usually last name
        }
        return author; // Fallback to entire string
    }

    // File Loading
    public boolean loadBooksFromFile(String filename) {
        try {
            File file = new File(filename);
            System.out.println("Looking for file: " + file.getAbsolutePath());

            if (!file.exists()) {
                System.out.println("❌ File not found: " + filename);
                return false;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int bookCount = 0;

            System.out.println("✅ File found! Reading books...");

            while ((line = reader.readLine()) != null && bookCount < 200) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Book book = parseBookLine(line);
                if (book != null) {
                    books.add(book);
                    bookCount++;
                }
            }

            reader.close();
            System.out.println("✅ Loaded " + bookCount + " books from " + filename);
            return true;

        } catch (IOException e) {
            System.err.println("❌ Error reading file: " + e.getMessage());
            return false;
        }
    }

    // Parsing Logic:
    // Format: "Title, by Author1, Author2, ISBN, Year"
    // Steps: 1. Split by " by " → [Title], [Rest]
    //        2. Split rest by ", " → [Authors..., ISBN, Year]
    //        3. Year = last part, ISBN = second-last, Authors = everything before ISBN
    private Book parseBookLine(String line) {
        try {
            if (line.trim().isEmpty()) return null;

            // 1. Split by " by " to separate title from rest
            int byIndex = line.indexOf(" by ");
            if (byIndex == -1) return null;
            String title = line.substring(0, byIndex).trim();
            String rest = line.substring(byIndex + 4).trim();

            // 2. Split rest by commas to get parts
            String[] parts = rest.split(", ");
            if (parts.length < 3) return null;

            // 3. Extract elements by position:
            //    - Year = last part, ISBN = second-last, Authors = everything before ISBN
            String yearStr = parts[parts.length - 1].trim();
            if (!yearStr.matches("\\d{4}")) return null;
            int year = Integer.parseInt(yearStr);

            String isbnStr = parts[parts.length - 2].trim().replace("-", "");
            if (!isbnStr.matches("\\d+")) return null;
            Long isbn = Long.parseLong(isbnStr);

            StringBuilder author = new StringBuilder();
            for (int i = 0; i < parts.length - 2; i++) {
                if (i > 0) author.append(", ");
                author.append(parts[i].trim());
            }

            return new Book(title, author.toString(), isbn, year, true);

        } catch (Exception e) {
            return null;
        }
    }

    // Empty other methods
    public List<Book> searchByTitle(String title) {
        System.out.println("searchByTitle() - Not yet implemented");
        return new ArrayList<>();
    }

    public List<Book> searchByAuthor(String author) {
        System.out.println("searchByAuthor() - Not yet implemented");
        return new ArrayList<>();
    }

    public Book searchByISBN(Long ISBN) {
        System.out.println("searchByISBN() - Not yet implemented");
        return null;
    }

    public boolean borrowBook(Long ISBN) {
        System.out.println("borrowBook() - Not yet implemented");
        return false;
    }

    public boolean returnBook(Long ISBN) {
        System.out.println("returnBook() - Not yet implemented");
        return false;
    }

    public boolean addBook(Book book) {
        System.out.println("addBook() - Not yet implemented");
        return false;
    }

    public boolean removeBook(Long ISBN) {
        System.out.println("removeBook() - Not yet implemented");
        return false;
    }

    public boolean saveBooksToFile(String filename) {
        System.out.println("saveBooksToFile() - Not yet implemented");
        return false;
    }
}