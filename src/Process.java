import java.util.LinkedList;

public class Process implements Comparable<Process>{

   private int name;
   private int size;
   private int arrival;
   private int duration;
   private int start;
   private LinkedList<Page> pages;

   /**
    * Process Constructor.  
    * 
    * @param name
    * @param size
    * @param arrival
    * @param duration
    */
   public Process(int name, int size, int arrival, int duration) {
      this.name = name;
      this.size = size;
      this.arrival = arrival;
      this.duration = duration;
      this.pages = new LinkedList<Page>();
   }

   /**
    * String format for process. Gives process ID name, size in MB, arrival time, and duration
    */
   public String toString() {
      return "\nProcess " + name + " size " + size + " arrival 0:" + 
      String.format("%02d", arrival) + " duration " + duration;
   }
   
   /**
    * Comapred process according to arrival time.
    */
   public int compareTo(Process process) {
      int ret = 0;
      
      if (this.arrival > process.arrival)
         ret = 1;
      else if (this.arrival < process.arrival)
         ret = -1;
      
      return ret;
   }

   public int getName() {
      return name;
   }

   public void setName(int name) {
      this.name = name;
   }

   public int getSize() {
      return size;
   }

   public void setSize(int size) {
      this.size = size;
   }

   public int getArrival() {
      return arrival;
   }

   public void setArrival(int arrival) {
      this.arrival = arrival;
   }

   public int getDuration() {
      return duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public int getStart() {
      return start;
   }

   public void setStart(int start) {
      this.start = start;
   }

   public LinkedList<Page> getPages() {
      return pages;
   }

   public void setPages(LinkedList<Page> pages) {
      this.pages = pages;
   }
}
