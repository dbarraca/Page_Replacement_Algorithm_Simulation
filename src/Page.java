
public class Page {
   private int content;
   
   public Page(int content) {
      this.content = content;
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
}
