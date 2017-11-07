import java.util.LinkedList;
import java.util.ListIterator;

public class LFU implements PageReplacement{

   private LinkedList<Page> pages; // reference to pages
   
   public LFU(LinkedList<Page> pages) {
      this.pages = pages; // get reference to pages
   }
   
   public int pageEviction() {
      ListIterator<Page> iter = pages.listIterator();
      
      Page currPage;
      int leastUsedTimes = -1;
      int toEvictPageNumber = -1;
      
      while(iter.hasNext()) {  // iterate through pages
         currPage = iter.next(); // get current page
         
         if (leastUsedTimes == -1 || currPage.getTimesUsed() < leastUsedTimes) { // check if page is used less
            toEvictPageNumber = currPage.getPageNumber(); // get page number
            leastUsedTimes = currPage.getTimesUsed(); // set new least used time
         }
      }
      
//      System.out.println(" Page Number " + toEvictPageNumber + " Times Used " + leastUsedTimes);

      return toEvictPageNumber; // return page number of next page to be evicted
   }
   
   /**
    * Prints type of page replacement algorithm
    */
   public String toString() {
      return "Least Frequently Used Page Replacement Algorithm";
   }

}