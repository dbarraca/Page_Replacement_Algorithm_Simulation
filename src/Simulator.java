import java.util.*;

public class Simulator {

   private Random random;
   private LinkedList<Page> pages;
   private LinkedList<Page> freePages;
   private LinkedList<Process> workLoad;
   private LinkedList<Process> runningProcesses;
   private int sec;
   
   private static int NUM_PROCESS = 150;
   private static int MAX_ARRIVE_TIME = 60;
   private static int NUM_SIZES = 4;
   private static int NUM_DURATIONS = 5;
   private static int NUM_PAGES = 100;
   private static int MIN_FREE_PAGES = 4;
   private static int SEC_PER_MIN = 60;
   
   public static void main(String args[]) {
      Simulator sim = new Simulator();
      sim.runningProcesses = new LinkedList<Process>();
      
      sim.workLoad = sim.generateWorkLoad(NUM_PROCESS);
      System.out.println(sim.workLoad.toString());
      
      sim.pages = sim.generatePages(NUM_PAGES);
      sim.freePages = (LinkedList<Page>)sim.pages.clone();
      
      sim.sec = 0;
      
      while(sim.sec < SEC_PER_MIN) {
//         System.out.println("Time 0:" + String.format("%02d", sim.sec));
         if (!sim.workLoad.isEmpty()) {
            //Run new processes
            sim.runNewProcesses();
         }
         
         //Add new page to running processes every 100ms
         sim.addProcessPages();
         
         //Exit complete processes
         sim.checkCompletedProcesses();
         sim.sec++;
      }
   }
   
   private void runNewProcesses() {
      while (freePages.size() >= MIN_FREE_PAGES && !workLoad.isEmpty() &&
            workLoad.peek().getArrival() <= sec) {
         Process currProcess = workLoad.pop();
         currProcess.setStart(sec);
         runningProcesses.add(currProcess);
         printProcessStatus(currProcess, true);
      }
   }
   
   private void addProcessPages() {
      for (int msec = 0; msec < 1000; msec += 100) {

         for (ListIterator<Process> iter = runningProcesses.listIterator(); iter.hasNext();) {
            Process currProcess = iter.next();
            if (currProcess.getPages().size() < currProcess.getSize()) {
               System.out.print("0:" + String.format("%02d", sec));
              getProcessPage(currProcess);
            }
         }
      }
   }
   
   private void getProcessPage(Process process) {
      if (freePages.size() > MIN_FREE_PAGES) {
         Page freePage = freePages.pop();
         if (process.getPages().size() == 0)
            freePage.setContent(0);
         else
            freePage.setContent(process.getPages().getLast().getContent() + 1); // increment content now, change to locality alg later
   
         process.getPages().add(freePage);
         
   //      System.out.println(process.getPages().toString());
         System.out.println(" Process " + process.getName() + 
          " getting Page Number " + freePage.getPageNumber() +
          " as Page " + freePage.getContent() + " in memory with no eviction");
      }
//      else {
//         Page Replacement choosing here
//      }
   }
   
   private void checkCompletedProcesses() {
      ListIterator<Process> iter = runningProcesses.listIterator();
//      System.out.println(runningProcesses.toString());
      
      while (iter.hasNext()) {
         Process currProcess = iter.next();
         if (currProcess.getStart() + currProcess.getDuration() <= sec) {
            printProcessStatus(currProcess, false);
            freePages.addAll(currProcess.getPages());
            iter.remove();
         }
      }
   }
   
   private void printProcessStatus(Process process, boolean isEntering) {
      String enterStr = " Entering";
      
      if (!isEntering)
         enterStr = " Exiting";
      
      System.out.println("0:" + String.format("%02d", sec) +  " Process "
            + process.getName() + enterStr + ", Size: " + 
            process.getSize() +" pages," + " Arrival: " + "0:" + 
            String.format("%02d", process.getArrival()) + ", Duration: " 
            + process.getDuration());
   }
   
   private LinkedList<Page> generatePages(int numPages) {
      LinkedList<Page> pages = new LinkedList<Page>();
      
      for (int i = 0; i < numPages; i++)
         pages.add(new Page(-1, i));
      
      return pages;
   }
   
   public LinkedList<Process> generateWorkLoad(int numProcess) {
      LinkedList<Process> workLoad = new LinkedList<Process>();
      random = new Random(1);
      int durations[] = distributeParam(numProcess, NUM_DURATIONS);
      int sizes[] = distributeParam(numProcess, NUM_SIZES);
      
      for (int i = 0; i < numProcess; i++) {
         workLoad.add(new Process(i, translateSize(sizes[i]), random.nextInt(MAX_ARRIVE_TIME), durations[i] + 1));
      }
      
      Collections.sort(workLoad);
      
      return workLoad;
   }
   
   private int translateSize(int sizeCode) {
      int size = 5;
      
      if (sizeCode == 0) {
         size = 5;
      }
      else if (sizeCode == 1) {
         size = 11;
      }
      else if (sizeCode == 2) {
         size = 17;
      }
      else if (sizeCode == 3) {
         size = 31;
      }
      
      return size;
   }
   
   private int[] distributeParam(int numProcess,int numParam) {
      int paramValues[] = new int[numProcess];
      int processesPerParam = numProcess / numParam;
      int paramCounts[] = new int[numParam];
      
      for (int i = 0; i < numProcess; i++) {
         int currIndex = random.nextInt(numProcess);
         int nearFull = 1;
         
         while(paramValues[currIndex] != 0) {
            currIndex = random.nextInt(numProcess);
         }
         
         for (int j = 0; j < numParam; j++)
            if (paramCounts[j] < processesPerParam)
               nearFull = 0;
         
         processesPerParam += nearFull;
         
         do {
            paramValues[currIndex] = random.nextInt(numParam);
         } while (paramCounts[paramValues[currIndex]] >= processesPerParam);
         
         paramCounts[paramValues[currIndex]]++;
      }
      
      return paramValues;
   }
}
