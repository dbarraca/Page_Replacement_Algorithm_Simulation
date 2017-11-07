public class FIFO implements PageReplacement{

   private int nextEvictPageNumber;
   
   public FIFO() {
      this.nextEvictPageNumber = 0;
   }
   
   public int pageEviction() {
      int toEvictPageNumber = nextEvictPageNumber;
      
      nextEvictPageNumber++;
      
      if (nextEvictPageNumber > 99)
         nextEvictPageNumber = 0;
      
//      System.out.println(nextEvictPageNumber);
         
      return toEvictPageNumber;
   }
   
   public String toString() {
      return "First In First Out Page Replacement Algorithm";
   }

}