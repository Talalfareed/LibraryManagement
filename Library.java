import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Library {
   private HashMap<Long, Book> catalog;
   private String FILE_HEADER = "";
   // REMOVED: List<Book> availableBooks;

   // --- CONSTRUCTOR ---
   public Library(String filename){
      this.catalog = new HashMap<>();
      // REMOVED: this.availableBooks = new ArrayList<>();

      File f = new File(filename);
      try(Scanner sc = new Scanner(f)){
         String line;

         while(sc.hasNextLine()){
            line = sc.nextLine().trim();

            if(line.isEmpty()) continue;

            if(line.startsWith("[")){
               int endBracket = line.indexOf("]");
               if(endBracket != -1 && endBracket < line.length() - 1){
                  line = line.substring(endBracket + 1).trim();
               } else {
                  continue;
               }
            }

            int lastComma = line.lastIndexOf(',');
            if(lastComma == line.length() - 1){
               lastComma = line.lastIndexOf(',', lastComma - 1);
            }

            if(lastComma == -1) continue;

            String yearString = line.substring(lastComma + 1).trim();
            String front = line.substring(0, lastComma).trim();

            int isbnComma = front.lastIndexOf(',');
            if(isbnComma == -1) continue;

            String isbnString = front.substring(isbnComma + 1).
                  trim().replaceAll("-", "");
            String titleAndAuthor = front.substring(0, isbnComma).trim();

            int byIndex = titleAndAuthor.indexOf(", by");
            if(byIndex == -1) continue;

            String title = titleAndAuthor.substring(0, byIndex).trim();
            String authorBlock = titleAndAuthor.
                  substring(byIndex + 5).trim();

            // Stores the author block directly (e.g., "Perez, Lee, et al")
            String authorString = authorBlock;

            try{
               Integer releaseYear = Integer.parseInt(yearString);
               Long ISBN = Long.parseLong(isbnString);

               Book temp = new Book(title, authorString, ISBN, releaseYear);
               catalog.put(ISBN, temp);
            }
            catch(NumberFormatException e){
               // Error or incomplete line
            }
         }
         System.out.println("Library loaded successfully. Total books: " + catalog.size());
      }
      catch(FileNotFoundException e){
         System.out.printf("File '%s' not found. Starting with empty library.\n", filename);
      }
      // REMOVED: Logic to populate and sort the now-removed availableBooks field
   }

   // REMOVED: private void updateAvailableBooks() { ... }

   public void addBook(Book book){
      if(catalog.containsKey(book.getISBN())){
         System.out.println("Error: Book with ISBN already exists.");
      }
      else{
         catalog.put(book.getISBN(), book);
         System.out.println(book.getTitle() + " added to library.");
         // REMOVED: updateAvailableBooks();
      }
   }

   public boolean removeBook(Long isbn){
      if(catalog.containsKey(isbn)){
         Book removed = catalog.remove(isbn);
         System.out.println(removed.getTitle() + " removed from catalog.");
         // REMOVED: updateAvailableBooks();
         return true;
      } else {
         System.out.println("Error: Book with ISBN " + isbn + " not found.");
         return false;
      }
   }

   public boolean borrowBook(Long isbn){
      Book book = catalog.get(isbn);
      if(book != null && book.getAvailability() > 0){
         book.setAvailability(0);
         System.out.println(book.getTitle() + " has been borrowed.");
         // REMOVED: updateAvailableBooks();
         return true;
      } else if(book != null){
         System.out.println("Book is currently unavailable");
         return false;
      }
      System.out.println("Book with ISBN " + isbn + " not found.");
      return false;
   }

   public boolean returnBook(Long isbn){
      Book book = catalog.get(isbn);
      if(book != null && book.getAvailability() == 0){
         book.setAvailability(1);
         System.out.println(book.getTitle() + " returned. Thank you.");
         // REMOVED: updateAvailableBooks();
         return true;
      } else if (book != null) {
         System.out.println(book.getTitle() + " was already available.");
         return true;
      }
      System.out.println("Book with ISBN " + isbn + " not found.");
      return false;
   }

   public List<Book> searchBooks(String query) {
      String rawQuery = query.toLowerCase();
      // This is to search
      String isbnQuery = rawQuery.replaceAll("[\\s-]", "");

      List<Book> results = new ArrayList<>();
      for (Book book : catalog.values()) {
         // Check Title/Author (using raw query)
         if (book.getTitle().toLowerCase().contains(rawQuery) ||
               book.getAuthor().toLowerCase().contains(rawQuery) ||
               // Check ISBN (using fixed query)
               String.valueOf(book.getISBN()).contains(isbnQuery)) {
            results.add(book);
         }
      }
      return results;
   }

   //Sorted by Title page view (REFACTORED to filter/sort on demand)
   public void viewAvailableBooks(Scanner scanner) {
      int PAGE_SIZE = 10;
      int startIndex = 0;

      while (true) {
         // REFACTORED: Create, filter, and sort the list every loop iteration
         // This ensures availability changes (like a borrow) are reflected immediately
         List<Book> availableBooks = new ArrayList<>();
         for (Book book : catalog.values()) {
            if (book.getAvailability() > 0) {
               availableBooks.add(book);
            }
         }
         availableBooks.sort(Comparator.comparing(Book::getTitle));
         // End of REFACTORED logic

         int totalBooks = availableBooks.size();
         if (totalBooks == 0) {
            System.out.println("\n--- Available Books ---");
            System.out.println("No books are currently available.");
            System.out.println("------------------------");
            break;
         }

         if (startIndex >= totalBooks) {
            startIndex = Math.max(0, totalBooks - PAGE_SIZE);
         }
         startIndex = Math.max(0, startIndex);


         int endIndex = Math.min(startIndex + PAGE_SIZE, totalBooks);
         System.out.printf("\n--- Available Books " +
                     "(Showing %d to %d of %d) ---\n", startIndex + 1,
               endIndex, totalBooks);

         for (int i = startIndex; i < endIndex; i++) {
            Book book = availableBooks.get(i);
            System.out.printf("%d. %s (%d), Author: %s (ISBN: %d)\n",
                  i + 1, book.getTitle(), book.getPubYear(),
                  book.getAuthor(), book.getISBN());
         }

         String prompt = "Enter book # to borrow, 'N' for next page, " +
               "'P' for previous page, or 'M' for menu: ";
         System.out.print(prompt);
         String input = scanner.nextLine().trim();

         if (input.equalsIgnoreCase("M")) {
            System.out.println("Returning to main menu...");
            break;
         } else if (input.equalsIgnoreCase("N")) {
            if (endIndex < totalBooks) {
               startIndex = endIndex;
            } else {
               System.out.println("Already on the last page.");
            }
         } else if (input.equalsIgnoreCase("P")) {
            if (startIndex > 0) {
               startIndex = Math.max(0, startIndex - PAGE_SIZE);
            } else {
               System.out.println("Already on the first page.");
            }
         } else {
            try {
               int bookNumber = Integer.parseInt(input);

               if (bookNumber >= 1 && bookNumber <= totalBooks) {
                  Book bookToBorrow = availableBooks.get(bookNumber - 1);
                  borrowBook(bookToBorrow.getISBN());
               } else {
                  System.out.println("Invalid book number. " +
                        "Please enter a number between 1 and "
                        + totalBooks + ".");
               }
            } catch (NumberFormatException e) {
               System.out.println("Invalid input. " +
                     "Please enter a valid book number, " +
                     "'N', 'P', or 'M'.");
            }
         }
      }
      System.out.println("--- End of List ---");
   }

   //Views the borrowed books (Already follows the filter-on-demand pattern)
   public void viewBorrowedBooks(Scanner scanner) {
      final int PAGE_SIZE = 10;
      int startIndex = 0;

      while (true) {
         List<Book> borrowedBooks = new ArrayList<>();
         for (Book book : catalog.values()) {
            if (book.getAvailability() == 0) {
               borrowedBooks.add(book);
            }
         }

         // Sort borrowed books by title
         borrowedBooks.sort(Comparator.comparing(Book::getTitle));

         int totalBooks = borrowedBooks.size();
         if (totalBooks == 0) {
            System.out.println("\n--- Borrowed Books ---");
            System.out.println("No books are currently borrowed.");
            System.out.println("------------------------");
            break;
         }

         if (startIndex >= totalBooks) {
            startIndex = Math.max(0, totalBooks - PAGE_SIZE);
         }
         startIndex = Math.max(0, startIndex);


         int endIndex = Math.min(startIndex + PAGE_SIZE, totalBooks);
         System.out.printf("\n--- Borrowed Books " +
                     "(Showing %d to %d of %d) ---\n", startIndex + 1,
               endIndex, totalBooks);

         for (int i = startIndex; i < endIndex; i++) {
            Book book = borrowedBooks.get(i);
            System.out.printf("%d. %s (%d), Author: %s (ISBN: %d)\n",
                  i + 1, book.getTitle(), book.getPubYear(),
                  book.getAuthor(), book.getISBN());
         }

         String prompt = "Enter book # to RETURN, 'N' for next page," +
               " 'P' for previous page, or 'M' for menu: ";

         System.out.print(prompt);
         String input = scanner.nextLine().trim();

         if (input.equalsIgnoreCase("M")) {
            System.out.println("Returning to main menu...");
            break;
         } else if (input.equalsIgnoreCase("N")) {
            if (endIndex < totalBooks) {
               startIndex = endIndex;
            } else {
               System.out.println("Already on the last page.");
            }
         } else if (input.equalsIgnoreCase("P")) {
            if (startIndex > 0) {
               startIndex = Math.max(0, startIndex - PAGE_SIZE);
            } else {
               System.out.println("Already on the first page.");
            }
         } else {
            try {
               int bookNumber = Integer.parseInt(input);

               if (bookNumber >= 1 && bookNumber <= totalBooks) {
                  Book bookToReturn = borrowedBooks.get(bookNumber - 1);
                  returnBook(bookToReturn.getISBN());
               } else {
                  System.out.println("Invalid book number. " +
                        "Please enter a number between 1 and " + totalBooks + ".");
               }
            } catch (NumberFormatException e) {
               System.out.println("Invalid input. " +
                     "Please enter a valid book number, 'N', 'P', or 'M'.");
            }
         }
      }
      System.out.println("--- End of List ---");
   }

   // --- SAVE/LOAD (Saves books sorted by Title)
   public void saveCatalog(String filename) {
      try (PrintWriter writer = new PrintWriter(new File(filename))) {
         writer.println(FILE_HEADER);

         // 1. Get available and unavailable books from the map
         List<Book> books = new ArrayList<>(catalog.values());

         // 2. Sorts the entire list
         books.sort(Comparator.comparing(Book::getTitle));

         // 3. Write the sorted list to the file
         for (Book book : books) {
            writer.println(book.toString());
         }
         System.out.printf("Catalog successfully saved to %s.\n", filename);
      } catch (FileNotFoundException e) {
         System.out.println("Error saving file: " + e.getMessage());
      }
   }

   // --- MAIN SYSTEM RUNNER ---
   public static void main(String[] args) {
      String SAVE_FILE = "library_catalog.txt";

      Scanner scanner = new Scanner(System.in);

      // Ask user for the data file path
      System.out.print("Enter the path to the book data file: ");
      String dataFilePath = scanner.nextLine();

      Library library = new Library(dataFilePath);

      boolean running = true;
      while (running) {
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
         System.out.print("Enter choice: ");

         if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
               case 1:
                  System.out.print("Title: ");
                  String title = scanner.nextLine();
                  System.out.print("Author (e.g., Smith, Jones, et al): ");
                  String author = scanner.nextLine();

                  System.out.print("ISBN (10 digits, no hyphens): ");
                  Long isbn;
                  try {
                     isbn = scanner.nextLong();
                     scanner.nextLine();
                  } catch (InputMismatchException e) {
                     System.out.println("Invalid ISBN. Please enter digits only.");
                     scanner.nextLine(); // Clear the buffer
                     break;
                  }

                  System.out.print("Publication Year: ");
                  Integer year;
                  try {
                     year = scanner.nextInt();
                     scanner.nextLine();
                  } catch (InputMismatchException e) {
                     System.out.println("Invalid year. Please enter digits only.");
                     scanner.nextLine(); // Clear the buffer
                     break;
                  }

                  library.addBook(new Book(title, author, isbn, year));
                  break;

               case 2: // Search Book
                  System.out.print("Enter Title, Author, or ISBN " +
                        "(hyphens allowed for ISBN search): ");
                  String query = scanner.nextLine();
                  List<Book> results = library.searchBooks(query);

                  if (results.isEmpty()) {
                     System.out.println("No books found matching the query.");
                  } else {
                     System.out.printf("\n--- Found %d Book(s) ---\n",
                           results.size());
                     for(int i = 0; i < results.size(); i++) {
                        Book result = results.get(i);
                        System.out.printf("%d. Title: %s, Author: %s " +
                                    "(ISBN: %d, Available: %d)\n",
                              i + 1, result.getTitle(), result.getAuthor(),
                              result.getISBN(), result.getAvailability());
                     }
                  }
                  break;

               case 3: // Borrow Book (by ISBN)
                  System.out.print("Enter ISBN to borrow (digits only): ");
                  try {
                     long borrowIsbn = scanner.nextLong();
                     scanner.nextLine();
                     library.borrowBook(borrowIsbn);
                  } catch (InputMismatchException e) {
                     System.out.println("Invalid ISBN input.");
                     scanner.nextLine();
                  }
                  break;

               case 4: // Return Book (by ISBN)
                  System.out.print("Enter ISBN to return (digits only): ");
                  try {
                     long returnIsbn = scanner.nextLong();
                     scanner.nextLine();
                     library.returnBook(returnIsbn);
                  } catch (InputMismatchException e) {
                     System.out.println("Invalid ISBN input.");
                     scanner.nextLine();
                  }
                  break;

               case 5: // View Available
                  library.viewAvailableBooks(scanner);
                  break;

               case 6: // View Borrowed
                  library.viewBorrowedBooks(scanner);
                  break;

               case 7: // Remove Book
                  System.out.print("Enter ISBN of book to remove " +
                        "(digits only): ");
                  try {
                     long removeIsbn = scanner.nextLong();
                     scanner.nextLine();
                     library.removeBook(removeIsbn);
                  } catch (InputMismatchException e) {
                     System.out.println("Invalid ISBN input.");
                     scanner.nextLine();
                  }
                  break;

               case 8: // Save Catalog (Now Sorted by Title)
                  library.saveCatalog(SAVE_FILE);
                  break;

               case 9: // Exit
                  library.saveCatalog(SAVE_FILE);
                  running = false;
                  System.out.println("Exiting system. Goodbye!");
                  break;

               default:
                  System.out.println("Invalid choice. " +
                        "Please enter a valid number from the menu.");
            }
         } else {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
         }
      }
      scanner.close();
   }
}