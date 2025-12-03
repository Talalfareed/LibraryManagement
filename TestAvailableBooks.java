import java.util.Scanner;

public class TestAvailableBooks {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        System.out.println("=== LIBRARY MANAGEMENT SYSTEM - TEST DRIVER ===");

        // Load books from file
        System.out.println("Loading books from book_donation.txt...");
        boolean loaded = library.loadBooksFromFile("book_donation.txt");

        if (!loaded) {
            System.out.println("❌ Failed to load books. Exiting.");
            scanner.close();
            return;
        }

        System.out.println("✅ Successfully loaded " + library.getAvailableBooks().size() + " available books!");

        // Test Menu Loop
        while (true) {
            System.out.println("\n" + "-".repeat(60));
            System.out.println("Test Menu");
            System.out.println("-".repeat(60));
            System.out.println("1. Test Sorting Methods (View Available Books)");
            System.out.println("2. Test Search Methods");
            System.out.println("3. Test Borrow/Return Methods");
            System.out.println("4. Exit Test");
            System.out.print("Choose an option (1-4): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    testSortingMethods(scanner, library);
                    break;
                case 2:
                    testSearchMethods(scanner, library);
                    break;
                case 3:
                    testBorrowReturnMethods(scanner, library);
                    break;
                case 4:
                    System.out.println("Exiting test driver...");
                    scanner.close();
                    return;
                default:
                    System.out.println("❌ Invalid option. Please choose 1-4.");
            }
        }
    }

    private static void testSortingMethods(Scanner scanner, Library library) {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("TESTING ALL SORTING METHODS");
        System.out.println("-".repeat(60));

        System.out.println("\nAvailable Sorting Options:");
        System.out.println("1. By Title (A-Z)");
        System.out.println("2. By Title (Z-A)");
        System.out.println("3. By Author (A-Z)");
        System.out.println("4. By Author (Z-A)");
        System.out.println("5. By Year (Oldest first)");
        System.out.println("6. By Year (Newest first)");
        System.out.println("7. By ISBN (Ascending)");
        System.out.println("8. By ISBN (Descending)");
        System.out.println("9. By Availability (Available first)");
        System.out.println("10. By Availability (Borrowed first)");
        System.out.print("Choose sorting option to test (1-10, or 0 for all): ");

        int sortChoice = scanner.nextInt();
        scanner.nextLine();

        if (sortChoice == 0) {
            // Test ALL sorting methods
            System.out.println("\n--- Testing ALL Sorting Methods (10 books each) ---");

            System.out.println("\n1. sortByTitle() - Title A-Z:");
            library.sortByTitle();
            library.displayAvailableBooksSample(10);

            System.out.println("\n2. sortByTitleDescending() - Title Z-A:");
            library.sortByTitleDescending();
            library.displayAvailableBooksSample(10);

            System.out.println("\n3. sortByAuthor() - Author A-Z:");
            library.sortByAuthor();
            library.displayAvailableBooksSample(10);

            System.out.println("\n4. sortByAuthorDescending() - Author Z-A:");
            library.sortByAuthorDescending();
            library.displayAvailableBooksSample(10);

            System.out.println("\n5. sortByYear() - Oldest first:");
            library.sortByYear();
            library.displayAvailableBooksSample(10);

            System.out.println("\n6. sortByYearDescending() - Newest first:");
            library.sortByYearDescending();
            library.displayAvailableBooksSample(10);

            System.out.println("\n7. sortByISBN() - ISBN Ascending:");
            library.sortByISBN();
            library.displayAvailableBooksSample(10);

            System.out.println("\n8. sortByISBNDescending() - ISBN Descending:");
            library.sortByISBNDescending();
            library.displayAvailableBooksSample(10);

            System.out.println("\n9. sortByAvailability() - Available first:");
            library.sortByAvailability();
            library.displayAvailableBooksSample(10);

            System.out.println("\n10. sortByAvailabilityDescending() - Borrowed first:");
            library.sortByAvailabilityDescending();
            library.displayAvailableBooksSample(10);

        } else {
            // Test specific sorting method
            System.out.println("\n--- Testing Selected Sorting Method (10 sample books) ---");

            switch (sortChoice) {
                case 1:
                    library.sortByTitle();
                    break;
                case 2:
                    library.sortByTitleDescending();
                    break;
                case 3:
                    library.sortByAuthor();
                    break;
                case 4:
                    library.sortByAuthorDescending();
                    break;
                case 5:
                    library.sortByYear();
                    break;
                case 6:
                    library.sortByYearDescending();
                    break;
                case 7:
                    library.sortByISBN();
                    break;
                case 8:
                    library.sortByISBNDescending();
                    break;
                case 9:
                    library.sortByAvailability();
                    break;
                case 10:
                    library.sortByAvailabilityDescending();
                    break;
                default:
                    System.out.println("⚠️ Invalid option. Testing sortByTitle() by default.");
                    library.sortByTitle();
            }

            library.displayAvailableBooksSample(10);
        }
    }

    private static void testSearchMethods(Scanner scanner, Library library) {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("TESTING SEARCH METHODS");
        System.out.println("-".repeat(60));

        System.out.println("\nSearch Test Options:");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Search by ISBN");
        System.out.print("Choose search type (1-3): ");

        int searchType = scanner.nextInt();
        scanner.nextLine();

        switch (searchType) {
            case 1:
                System.out.print("Enter title or part of title to search: ");
                String title = scanner.nextLine();
                System.out.println("\nTesting searchByTitle(\"" + title + "\")");
                library.searchByTitle(title);
                break;
            case 2:
                System.out.print("Enter author name to search: ");
                String author = scanner.nextLine();
                System.out.println("\nTesting searchByAuthor(\"" + author + "\")");
                library.searchByAuthor(author);
                break;
            case 3:
                System.out.print("Enter ISBN to search: ");
                if (scanner.hasNextLong()) {
                    Long isbn = scanner.nextLong();
                    scanner.nextLine();
                    System.out.println("\nTesting searchByISBN(" + isbn + ")");
                    library.searchByISBN(isbn);
                } else {
                    System.out.println("❌ Invalid ISBN format.");
                    scanner.nextLine();
                }
                break;
            default:
                System.out.println("❌ Invalid option.");
        }
    }

    private static void testBorrowReturnMethods(Scanner scanner, Library library) {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("TESTING BORROW/RETURN METHODS");
        System.out.println("-".repeat(60));

        System.out.println("\nBorrow/Return Test Options:");
        System.out.println("1. Borrow a book");
        System.out.println("2. Return a book");
        System.out.print("Choose option (1-2): ");

        int option = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter ISBN: ");
        if (scanner.hasNextLong()) {
            Long isbn = scanner.nextLong();
            scanner.nextLine();

            if (option == 1) {
                System.out.println("\nTesting borrowBook(" + isbn + ")");
                library.borrowBook(isbn);
            } else if (option == 2) {
                System.out.println("\nTesting returnBook(" + isbn + ")");
                library.returnBook(isbn);
            } else {
                System.out.println("❌ Invalid option.");
            }
        } else {
            System.out.println("❌ Invalid ISBN format.");
            scanner.nextLine();
        }
    }
}