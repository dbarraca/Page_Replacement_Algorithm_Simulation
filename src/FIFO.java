public class FIFO implements PageReplacement{

   private int nextEvictPageNumber; // page number of next page to be evicted
   
   public FIFO() {
      this.nextEvictPageNumber = 0; // set next page to be evicted to first page
   }
   
   /**
    * Returns page number of next page to be evicted in First In First Out 
    * page replacement algorithm.
    */
   public int pageEviction() {
      int toEvictPageNumber = nextEvictPageNumber;
      
      nextEvictPageNumber++; // move to next page to be evicted
      
      if (nextEvictPageNumber > 99) // check if at last page created
         nextEvictPageNumber = 0; // go back to first page
      
//      System.out.println(nextEvictPageNumber);
         
      return toEvictPageNumber; // return page number of next page to be evicted
   }
   
   /**
    * Prints type of page replacement algorithm
    */
   public String toString() {
      return "First In First Out Page Replacement Algorithm";
   }

}