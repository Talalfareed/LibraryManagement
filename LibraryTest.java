import java.util.List;

public class LibraryTest {
    public static void main(String[] args) {
        System.out.println("=== Library Test ===");
        System.out.println("Loading library from file...\n");

        // You need to provide the file path here
        String dataFile = "book_donation.txt"; // Change this to your file path

        Library library = new Library(dataFile);

        // Run all tests
        runAllTests(library);

        System.out.println("\n=== All Tests Completed ===");
    }

    private static void runAllTests(Library library) {
        System.out.println("=== Test 1: Display Library Statistics ===");
        displayStatistics(library);

        System.out.println("\n=== Test 2: Testing all search methods ===");
        testSearchMethods(library);

        System.out.println("\n=== Test 3: Test Borrow/Return methods ===");
        testBorrowReturnOperations(library);

        System.out.println("\n=== Test 4: Test View Methods ===");
        testViewMethodsSimulated(library);

        System.out.println("\n=== Test 5: Test Edge Cases ===");
        testEdgeCases(library);

        System.out.println("\n=== Test 6: Performance Test ===");
        performanceTest(library);
    }

    private static void displayStatistics(Library library) {
        System.out.println("Library Statistics:");
        System.out.println("Total books: " + library.getTotalBooks());
        System.out.println("Available books: " + library.getAvailableBooksCount());
        System.out.println("Borrowed books: " + library.getBorrowedBooksCount());

        // Display first 5 books as sample
        System.out.println("\nSample of books in library (first 5):");
        List<Book> allBooks = library.searchBooks("");
        int sampleSize = Math.min(5, allBooks.size());
        for (int i = 0; i < sampleSize; i++) {
            Book book = allBooks.get(i);
            System.out.printf("  %d. %s by %s (ISBN: %d) %s\n",
                    i + 1, book.getTitle(), book.getAuthor(),
                    book.getISBN(), book.getAvailability() > 0 ? "[Available]" : "[Borrowed]");
        }
    }

    private static void testSearchMethods(Library library) {
        System.out.println("\n1. Searching by title keywords:");
        String[] titleKeywords = {"the", "and", "of", "a", "in"};
        for (String keyword : titleKeywords) {
            List<Book> results = library.searchBooks(keyword);
            System.out.printf("  Search for '%s': Found %d books\n", keyword, results.size());

            // Show first 2 results if any
            if (!results.isEmpty()) {
                for (int i = 0; i < Math.min(2, results.size()); i++) {
                    Book book = results.get(i);
                    System.out.printf("    - %s (ISBN: %d)\n", book.getTitle(), book.getISBN());
                }
            }
        }

        System.out.println("\n2. Searching by author keywords:");
        String[] authorKeywords = {"Smith", "Jones", "et al", ","};
        for (String keyword : authorKeywords) {
            List<Book> results = library.searchBooks(keyword);
            System.out.printf("  Search for author containing '%s': Found %d books\n",
                    keyword, results.size());
        }

        System.out.println("\n3. Searching by ISBN (partial and full):");
        // Get some ISBNs from the library to test with
        List<Book> sampleBooks = library.searchBooks("");
        if (!sampleBooks.isEmpty()) {
            Book firstBook = sampleBooks.get(0);
            String firstISBN = String.valueOf(firstBook.getISBN());

            // Test with full ISBN
            List<Book> fullISBNResults = library.searchBooks(firstISBN);
            System.out.printf("  Search for full ISBN '%s': Found %d book(s)\n",
                    firstISBN, fullISBNResults.size());

            // Test with partial ISBN (first 3 digits)
            if (firstISBN.length() >= 3) {
                String partialISBN = firstISBN.substring(0, 3);
                List<Book> partialResults = library.searchBooks(partialISBN);
                System.out.printf("  Search for partial ISBN '%s': Found %d book(s)\n",
                        partialISBN, partialResults.size());
            }
        }

        System.out.println("\n4. Search for non-existent items:");
        String[] nonExistent = {"xyz123nonexistent", "0987654321", "NonExistentAuthorName"};
        for (String query : nonExistent) {
            List<Book> results = library.searchBooks(query);
            System.out.printf("  Search for '%s': Found %d books (expected: 0)\n",
                    query, results.size());
        }
    }

    private static void testBorrowReturnOperations(Library library) {
        System.out.println("\n1. Testing borrow operations:");

        // Find an available book to borrow
        List<Book> availableBooks = library.searchBooks("");
        Book bookToBorrow = null;

        for (Book book : availableBooks) {
            if (book.getAvailability() > 0) {
                bookToBorrow = book;
                break;
            }
        }

        if (bookToBorrow != null) {
            System.out.printf("  Borrowing book: %s (ISBN: %d)\n",
                    bookToBorrow.getTitle(), bookToBorrow.getISBN());

            // Test borrowing
            boolean borrowSuccess = library.borrowBook(bookToBorrow.getISBN());
            System.out.printf("  Borrow result: %s\n", borrowSuccess ? "SUCCESS" : "FAILED");

            // Try to borrow same book again (should fail)
            System.out.println("  Trying to borrow same book again:");
            library.borrowBook(bookToBorrow.getISBN());

            // Test returning
            System.out.printf("\n  Returning book: %s (ISBN: %d)\n",
                    bookToBorrow.getTitle(), bookToBorrow.getISBN());
            boolean returnSuccess = library.returnBook(bookToBorrow.getISBN());
            System.out.printf("  Return result: %s\n", returnSuccess ? "SUCCESS" : "FAILED");

            // Try to return again (should show already available)
            System.out.println("  Trying to return same book again:");
            library.returnBook(bookToBorrow.getISBN());
        } else {
            System.out.println("  No available books found to test borrow/return operations.");
        }

        System.out.println("\n2. Testing invalid ISBN operations:");
        long invalidISBN = 9999999999L; // Unlikely to exist
        System.out.printf("  Trying to borrow non-existent ISBN %d:\n", invalidISBN);
        library.borrowBook(invalidISBN);

        System.out.printf("  Trying to return non-existent ISBN %d:\n", invalidISBN);
        library.returnBook(invalidISBN);
    }

    private static void testViewMethodsSimulated(Library library) {
        System.out.println("\n1. Simulating viewAvailableBooks functionality:");

        // Manually implement what viewAvailableBooks does
        List<Book> allBooks = library.searchBooks("");

        // Count available books
        int availableCount = 0;
        for (Book book : allBooks) {
            if (book.getAvailability() > 0) {
                availableCount++;
            }
        }

        System.out.printf("  Total available books: %d\n", availableCount);

        if (availableCount > 0) {
            // Show first 3 available books sorted by title
            List<Book> availableBooksSorted = new java.util.ArrayList<>();
            for (Book book : allBooks) {
                if (book.getAvailability() > 0) {
                    availableBooksSorted.add(book);
                }
            }

            availableBooksSorted.sort(java.util.Comparator.comparing(Book::getTitle));

            System.out.println("  First 3 available books (sorted by title):");
            for (int i = 0; i < Math.min(3, availableBooksSorted.size()); i++) {
                Book book = availableBooksSorted.get(i);
                System.out.printf("    %d. %s by %s\n",
                        i + 1, book.getTitle(), book.getAuthor());
            }
        }

        System.out.println("\n2. Simulating viewBorrowedBooks functionality:");

        // Count borrowed books
        int borrowedCount = 0;
        for (Book book : allBooks) {
            if (book.getAvailability() == 0) {
                borrowedCount++;
            }
        }

        System.out.printf("  Total borrowed books: %d\n", borrowedCount);

        if (borrowedCount > 0) {
            // Show first 3 borrowed books sorted by title
            List<Book> borrowedBooksSorted = new java.util.ArrayList<>();
            for (Book book : allBooks) {
                if (book.getAvailability() == 0) {
                    borrowedBooksSorted.add(book);
                }
            }

            borrowedBooksSorted.sort(java.util.Comparator.comparing(Book::getTitle));

            System.out.println("  First 3 borrowed books (sorted by title):");
            for (int i = 0; i < Math.min(3, borrowedBooksSorted.size()); i++) {
                Book book = borrowedBooksSorted.get(i);
                System.out.printf("    %d. %s by %s\n",
                        i + 1, book.getTitle(), book.getAuthor());
            }
        }
    }

    // Tests edge cases where user enters in special or unusual characters, or blank
    private static void testEdgeCases(Library library) {
        System.out.println("\n1. Testing edge cases:");

        // Test empty search
        System.out.println("  Testing empty string search:");
        List<Book> allBooks = library.searchBooks("");
        System.out.printf("    Empty search returns: %d books (should be all books)\n", allBooks.size());

        // Test search with special characters
        System.out.println("  Testing search with special characters:");
        List<Book> specialCharResults = library.searchBooks("@#$%");
        System.out.printf("    Special chars search returns: %d books\n", specialCharResults.size());

        // Test search with spaces
        System.out.println("  Testing search with multiple spaces:");
        List<Book> spaceResults = library.searchBooks("   a   ");
        System.out.printf("    Multi-space search returns: %d books\n", spaceResults.size());

        // Test ISBN with hyphens (search should handle this)
        if (!allBooks.isEmpty()) {
            Book sampleBook = allBooks.get(0);
            String isbnWithHyphens = formatISBNWithHyphens(sampleBook.getISBN());
            System.out.printf("  Testing ISBN search with hyphens '%s':\n", isbnWithHyphens);
            List<Book> hyphenResults = library.searchBooks(isbnWithHyphens);
            System.out.printf("    Found: %d book(s)\n", hyphenResults.size());
        }
    }

    // Summary of how long operations take
    private static void performanceTest(Library library) {
        System.out.println("\n1. Performance testing:");

        long startTime, endTime;
        int iterations = 100;

        // Test search performance
        System.out.println("  Testing search performance:");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            library.searchBooks("the");
        }
        endTime = System.currentTimeMillis();
        System.out.printf("    %d searches for 'the': %d ms\n",
                iterations, endTime - startTime);

        // Test getting all books performance
        System.out.println("  Testing get all books performance:");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            library.searchBooks("");
        }
        endTime = System.currentTimeMillis();
        System.out.printf("    %d get-all operations: %d ms\n",
                iterations, endTime - startTime);

        // Show operation speed
        int totalBooks = library.getTotalBooks();
        if (totalBooks > 0) {
            long avgTimePerSearch = (endTime - startTime) / iterations;
            System.out.printf("\n  Performance summary:\n");
            System.out.printf("    Total books in library: %d\n", totalBooks);
            System.out.printf("    Average search time: %d ms\n", avgTimePerSearch);
            System.out.printf("    Operations per second: ~%.0f\n",
                    1000.0 / Math.max(1, avgTimePerSearch));
        }
    }

    private static String formatISBNWithHyphens(Long isbn) {
        String isbnStr = String.valueOf(isbn);
        if (isbnStr.length() == 10) {
            return isbnStr.substring(0, 1) + "-" +
                    isbnStr.substring(1, 3) + "-" +
                    isbnStr.substring(3, 9) + "-" +
                    isbnStr.substring(9);
        }
        return isbnStr;
    }
}