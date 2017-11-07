import java.util.LinkedList;
import java.util.ListIterator;

public class MFU implements PageReplacement{

   private LinkedList<Page> pages;
   
   public MFU(LinkedList<Page> pages) {
      this.pages = pages;
   }
   
   public int pageEviction() {
      ListIterator<Page> iter = pages.listIterator();
      
      Page currPage;
      int mostUsedTimes = -1;
      int toEvictPageNumber = -1;
      
      while(iter.hasNext()) {
         currPage = iter.next();
         
         if (currPage.getTimesUsed() > mostUsedTimes) {
            toEvictPageNumber = currPage.getPageNumber();
            mostUsedTimes = currPage.getTimesUsed();
         }
      }
      
//      System.out.println(" Page Number " + toEvictPageNumber + " Times Used " + mostUsedTimes);

      return toEvictPageNumber;
   }
   
   public String toString() {
      return "Most Frequently Used Page Replacement Algorithm";
   }

}