public class Book {
   private String title;
   private String author;
   private Long ISBN;
   private Integer pubYear;
   private Integer availability;

   public String getTitle(){ return title; }
   public String getAuthor(){ return author; }
   public Long getISBN(){ return ISBN; }
   public Integer getPubYear(){ return pubYear; }
   public Integer getAvailability(){ return availability; }

   public void setAvailability(Integer availability){
      this.availability = availability;
   }

   public Book(){
      title = "";
      author = "";
      ISBN = 0L;
      pubYear = 0;
      availability = 1;
   }

   public Book(String title, String author, Long ISBN, Integer pubYear){
      this.title = title;
      this.author = author;
      this.ISBN = ISBN;
      this.pubYear = pubYear;
      this.availability = 1; // Default to available
   }

   public Book(String title, String author, Long ISBN, Integer pubYear, Integer availability){
      this.title = title;
      this.author = author;
      this.ISBN = ISBN;
      this.pubYear = pubYear;
      this.availability = availability;
   }

   public String toString(){
      // FIX 1: Use StringBuilder for better performance
      StringBuilder sb = new StringBuilder();

      // FIX 2: Added space after "by"
      sb.append(title).append(", by ");

      // FIX 3: Handle author formatting correctly
      // Assuming author is stored as "First Last", we flip it to "Last, First" for the file,
      // or just keep it simple if it's already formatted.
      // Based on your previous code, let's keep it clean:
      sb.append(author).append(", ");

      // FIX 4: Robust ISBN formatting
      String tempISBN = String.valueOf(ISBN);
      if (tempISBN.length() == 10) {
         sb.append(tempISBN.substring(0, 1)).append("-");
         sb.append(tempISBN.substring(1, 3)).append("-");
         sb.append(tempISBN.substring(3, 9)).append("-");
         sb.append(tempISBN.substring(9));
      } else {
         sb.append(tempISBN);
      }

      sb.append(", ").append(pubYear);

      return sb.toString();
   }
}