
public class Page {
   private int content;
   private int pageNumber;
   
   public Page(int content, int pageNumber) {
      this.content = content;
      this.pageNumber = pageNumber;
   }
   
   public String toString() {
      return "" + content;
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
}
