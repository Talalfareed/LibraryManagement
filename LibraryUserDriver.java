import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LibraryUserDriver {
    private static final String SAVE_FILE = "library_catalog.txt";
    private static Library library;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        // Ask user for the data file path
        System.out.print("Enter the path to the book data file: ");
        String dataFilePath = scanner.nextLine();

        library = new Library(dataFilePath);

        boolean running = true;
        while (running) {
            displayMainMenu();

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        addBook();
                        break;
                    case 2:
                        searchBook();
                        break;
                    case 3:
                        borrowBook();
                        break;
                    case 4:
                        returnBook();
                        break;
                    case 5:
                        viewAvailableBooks();
                        break;
                    case 6:
                        viewBorrowedBooks();
                        break;
                    case 7:
                        removeBook();
                        break;
                    case 8:
                        saveCatalog();
                        break;
                    case 9:
                        running = exitSystem();
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid number from the menu.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // clear invalid input
            }
        }
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n=============================");
        System.out.println("--- Library System Menu ---");
        System.out.println("1. Add New Book");
        System.out.println("2. Search Book");
        System.out.println("3. Borrow Book (by ISBN)");
        System.out.println("4. Return Book (by ISBN)");
        System.out.println("5. View Available Books");
        System.out.println("6. View Borrowed Books");
        System.out.println("7. Remove Book (by ISBN)");
        System.out.println("8. Save Catalog");
        System.out.println("9. Exit");
        System.out.println("=============================");
        System.out.println("Total Books: " + library.getTotalBooks() +
                " | Available: " + library.getAvailableBooksCount() +
                " | Borrowed: " + library.getBorrowedBooksCount());
        System.out.print("Enter choice (1-9): ");
    }

    private static void addBook() {
        System.out.println("\n--- Add New Book ---");
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author (e.g., Smith, Jones, et al): ");
        String author = scanner.nextLine();

        System.out.print("ISBN (10 digits, no hyphens): ");
        Long isbn;
        try {
            isbn = scanner.nextLong();
            scanner.nextLine();

            // Validate ISBN length
            if (String.valueOf(isbn).length() != 10) {
                System.out.println("Error: ISBN must be 10 digits.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid ISBN. Please enter digits only.");
            scanner.nextLine();
            return;
        }

        System.out.print("Publication Year: ");
        Integer year;
        try {
            year = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid year. Please enter digits only.");
            scanner.nextLine();
            return;
        }

        library.addBook(new Book(title, author, isbn, year));
    }

    private static void searchBook() {
        System.out.println("\n--- Search Book ---");
        System.out.print("Enter Title, Author, or ISBN (hyphens allowed for ISBN search): ");
        String query = scanner.nextLine();
        List<Book> results = library.searchBooks(query);

        if (results.isEmpty()) {
            System.out.println("No books found matching the query.");
        } else {
            System.out.printf("\n--- Found %d Book(s) ---\n", results.size());
            for (int i = 0; i < results.size(); i++) {
                Book result = results.get(i);
                System.out.printf("%d. Title: %s\n", i + 1, result.getTitle());
                System.out.printf("   Author: %s\n", result.getAuthor());
                System.out.printf("   ISBN: %d, Year: %d\n", result.getISBN(), result.getPubYear());
                System.out.printf("   Status: %s\n\n",
                        result.getAvailability() > 0 ? "Available" : "Borrowed");
            }
        }
    }

    private static void borrowBook() {
        System.out.println("\n--- Borrow Book ---");
        System.out.print("Enter ISBN to borrow (digits only): ");
        try {
            long borrowIsbn = scanner.nextLong();
            scanner.nextLine();
            library.borrowBook(borrowIsbn);
        } catch (InputMismatchException e) {
            System.out.println("Invalid ISBN input.");
            scanner.nextLine();
        }
    }

    private static void returnBook() {
        System.out.println("\n--- Return Book ---");
        System.out.print("Enter ISBN to return (digits only): ");
        try {
            long returnIsbn = scanner.nextLong();
            scanner.nextLine();
            library.returnBook(returnIsbn);
        } catch (InputMismatchException e) {
            System.out.println("Invalid ISBN input.");
            scanner.nextLine();
        }
    }

    private static void viewAvailableBooks() {
        System.out.println("\n--- View Available Books ---");
        library.viewAvailableBooks(scanner);
    }

    private static void viewBorrowedBooks() {
        System.out.println("\n--- View Borrowed Books ---");
        library.viewBorrowedBooks(scanner);
    }

    private static void removeBook() {
        System.out.println("\n--- Remove Book ---");
        System.out.print("Enter ISBN of book to remove (digits only): ");
        try {
            long removeIsbn = scanner.nextLong();
            scanner.nextLine();
            library.removeBook(removeIsbn);
        } catch (InputMismatchException e) {
            System.out.println("Invalid ISBN input.");
            scanner.nextLine();
        }
    }

    private static void saveCatalog() {
        System.out.println("\n--- Save Catalog ---");
        library.saveCatalog(SAVE_FILE);
    }

    private static boolean exitSystem() {
        System.out.println("\n--- Exit System ---");
        System.out.print("Save catalog before exiting? (Y/N): ");
        String saveChoice = scanner.nextLine().trim();

        if (saveChoice.equalsIgnoreCase("Y")) {
            library.saveCatalog(SAVE_FILE);
        }

        System.out.println("Exiting system. Goodbye!");
        return false;
    }
}