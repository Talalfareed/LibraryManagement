public class Book {
   private String title;
   private String author;
   private Integer ISBN;
   private Integer pubYear;
   private boolean availability;

   public String getTitle(){
      return title;
   }

   public String getAuthor(){
      return author;
   }

   public Integer getISBN(){
      return ISBN;
   }

   public Integer getPubYear(){
      return pubYear;
   }

   public boolean getAvailability(){
      return availability;
   }

   public void setAvailability(boolean availability){
      this.availability = availability;
   }

   public Book(){
      title = "";
      author = "";
      ISBN = 0;
      pubYear = 0;
      availability = false;
   }

   public Book(String title, String author, Integer ISBN,
               Integer pubYear, boolean availability){
      this.title = title;
      this.author = author;
      this.ISBN = ISBN;
      this.pubYear = pubYear;
      this.availability = availability;
   }




}
