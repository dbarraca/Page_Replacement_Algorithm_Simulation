import java.util.LinkedList;
import java.util.ListIterator;

public class MFU implements PageReplacement{

   private LinkedList<Page> pages; // reference to pages
   
   public MFU(LinkedList<Page> pages) {
      this.pages = pages; // get reference to pages
   }
   
   public int pageEviction() {
      ListIterator<Page> iter = pages.listIterator();
      
      Page currPage;
      int mostUsedTimes = -1;
      int toEvictPageNumber = -1;
      
      while(iter.hasNext()) {  // iterate through pages
         currPage = iter.next(); // get current page
         
         if (currPage.getTimesUsed() > mostUsedTimes) { // check if page is used more
            toEvictPageNumber = currPage.getPageNumber(); // get page number
            mostUsedTimes = currPage.getTimesUsed(); // set new most used times
         }
      }
      
//      System.out.println(" Page Number " + toEvictPageNumber + " Times Used " + mostUsedTimes);

      return toEvictPageNumber; // return page number of next page to be evicted
   }
   
   /**
    * Prints type of page replacement algorithm
    */
   public String toString() {
      return "Most Frequently Used Page Replacement Algorithm";
   }

}