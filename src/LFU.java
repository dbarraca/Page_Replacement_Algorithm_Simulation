import java.util.LinkedList;
import java.util.ListIterator;

public class LFU implements PageReplacement{

   private LinkedList<Page> pages;
   
   public LFU(LinkedList<Page> pages) {
      this.pages = pages;
   }
   
   public int pageEviction() {
//      System.out.println("LFU page eviction");
      ListIterator<Page> iter = pages.listIterator();
      
      Page currPage;
      int leastUsedTimes = -1;
      int toEvictPageNumber = -1;
      
      while(iter.hasNext()) {
         currPage = iter.next();
         
         if (leastUsedTimes == -1 || currPage.getTimesUsed() < leastUsedTimes) {
            toEvictPageNumber = currPage.getPageNumber();
            leastUsedTimes = currPage.getTimesUsed();
         }
      }
      
//      System.out.println(" Page Number " + toEvictPageNumber + " Times Used " + leastUsedTimes);

      return toEvictPageNumber;
   }
   
   public String toString() {
      return "Least Frequently Used Page Replacement Algorithm";
   }

}