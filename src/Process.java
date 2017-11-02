public class Process implements Comparable<Process>{

   private int name;
   private int numPages;
   private int arrival;
   private int duration;


//   
   
   public Process(int name, int numPages, int arrival, int duration) {
      this.name = name;
      this.numPages = numPages;
      this.arrival = arrival;
      this.duration = duration;
   }
   

   public String toString() {
//      return "\nnumPages " + numPages;
//      return "\nduration " + duration;
      return "\nProcess " + name + " numPages " + numPages + " arrival " + 
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
}
