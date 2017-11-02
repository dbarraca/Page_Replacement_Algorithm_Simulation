import java.util.LinkedList;

public class Process implements Comparable<Process>{

   private int name;
   private int size;
   private int arrival;
   private int duration;
   private LinkedList<Page> pages;

   public Process(int name, int size, int arrival, int duration) {
      this.name = name;
      this.size = size;
      this.arrival = arrival;
      this.duration = duration;
      this.pages = new LinkedList<Page>();
   }

   public String toString() {
//      return "\nsize " + size;
//      return "\nduration " + duration;
      return "\nProcess " + name + " size " + size + " arrival " + 
      arrival + " duration " + duration;
   }
   
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

   public LinkedList<Page> getPages() {
      return pages;
   }

   public void setPages(LinkedList<Page> pages) {
      this.pages = pages;
   }
}
