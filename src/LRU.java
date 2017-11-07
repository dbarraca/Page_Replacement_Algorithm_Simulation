import java.util.LinkedList;
import java.util.ListIterator;

public class LRU implements PageReplacement{

   private LinkedList<Page> pages;
   
   public LRU(LinkedList<Page> pages) {
      this.pages = pages;
   }
   
   public int pageEviction() {
      ListIterator<Page> iter = pages.listIterator();
      
      Page currPage;
      int lastPageTime = 60;
      int toEvictPageNumber = -1;
      
      while(iter.hasNext()) {
         currPage = iter.next();

         if (currPage.getLastUsed() < lastPageTime) {
            toEvictPageNumber = currPage.getPageNumber();
            lastPageTime = currPage.getLastUsed();
         }
      }

//    System.out.println(" Page Number " + toEvictPageNumber + " lastUsed " + lastPageTime);
      return toEvictPageNumber;
   }
   
   public String toString() {
      return "Least Recently Used Page Replacement Algorithm";
   }

}