
public class Page {
   private int content;
   private int pageNumber;
   private int lastUsed;
   private int timesUsed;
   
   public Page(int content, int pageNumber) {
      this.content = content;
      this.pageNumber = pageNumber;
      this.lastUsed = 0;
      this.timesUsed = 0;
   }
   
   public String toString() {
      return "" + content;
   }
   
   public void incrementTimesUsed() {
      this.timesUsed++;
   }

   public int getContent() {
      return content;
   }

   public void setContent(int content) {
      this.content = content;
   }

   public int getPageNumber() {
      return pageNumber;
   }

   public void setPageNumber(int pageNumber) {
      this.pageNumber = pageNumber;
   }

   public int getLastUsed() {
      return lastUsed;
   }

   public void setLastUsed(int lastUsed) {
      this.lastUsed = lastUsed;
   }

   public int getTimesUsed() {
      return timesUsed;
   }

   public void setTimesUsed(int timesUsed) {
      this.timesUsed = timesUsed;
   }
}
