import java.util.Random;

public class RandomPageReplacement implements PageReplacement{

   private int nextEvictPageNumber = 0;
   private Random random;
   
   public RandomPageReplacement() {
      this.random = new Random(0);
   }
   
   public int pageEviction() {
      int toEvictPageNumber = nextEvictPageNumber;
      
      nextEvictPageNumber = random.nextInt(100);
      
//      System.out.println(nextEvictPageNumber);
         
      return toEvictPageNumber;
   }
   
   public String toString() {
      return "Random Page Replacement Algorithm";
   }
}
