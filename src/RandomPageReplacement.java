import java.util.Random;

public class RandomPageReplacement implements PageReplacement{

   private int nextEvictPageNumber = 0; // page number of next page to be evicted
   private Random random; // RNG
   
   public RandomPageReplacement() {
      this.random = new Random(0); // initialize RNG
   }
   
   public int pageEviction() {
      int toEvictPageNumber = nextEvictPageNumber;
      
      nextEvictPageNumber = random.nextInt(100); // get random page numer
      
//      System.out.println(nextEvictPageNumber);
         
      return toEvictPageNumber; // return page number of next page to be evicted
   }
   
   /**
    * Prints type of page replacement algorithm
    */
   public String toString() {
      return "Random Page Replacement Algorithm";
   }
}
