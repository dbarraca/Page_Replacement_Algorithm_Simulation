
public interface PageReplacement {

   /**
    * Generic page replace algorithm.
    * 
    * @return next page to be evicted.
    */
   public int pageEviction();
}
