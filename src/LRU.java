import java.util.LinkedList;
import java.util.ListIterator;

public class LRU implements PageReplacement{

   private LinkedList<Page> pages; // reference to pages
   
   public LRU(LinkedList<Page> pages) {
      this.pages = pages; // get reference to pages
   }
   
   public int pageEviction() {
      ListIterator<Page> iter = pages.listIterator();
      
      Page currPage;
      int lastPageTime = 60; // get max time
      int toEvictPageNumber = -1;
      
      while(iter.hasNext()) {  // iterate through pages
         currPage = iter.next(); // get current page

         if (currPage.getLastUsed() < lastPageTime) { // check for more recent page use
            toEvictPageNumber = currPage.getPageNumber(); // get page number
            lastPageTime = currPage.getLastUsed(); // get page's last used time
         }
      }

//    System.out.println(" Page Number " + toEvictPageNumber + " lastUsed " + lastPageTime);
      
      return toEvictPageNumber; // return page number of next page to be evicted
   }
   
   /**
    * Prints type of page replacement algorithm
    */
   public String toString() {
      return "Least Recently Used Page Replacement Algorithm";
   }

}