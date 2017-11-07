import java.util.*;

public class Simulator {

   private Random random;    // random number generator
   private LinkedList<Page> pages;    // all pages in page number order
   private LinkedList<Page> freePages;    // List of free pages
   private LinkedList<Process> workLoad;    // Processes in work load
   private LinkedList<Process> runningProcesses;    // Process currently running
   private int sec;
   private PageReplacement currPageReplacement;
   private float numHits;
   private float numMisses;
   private int numStartedProcesses;
   
   private static int NUM_PROCESS = 150;
   private static int MAX_ARRIVE_TIME = 60;
   private static int NUM_SIZES = 4;
   private static int NUM_DURATIONS = 5;
   private static int NUM_PAGES = 100;
   private static int MIN_FREE_PAGES = 4;
   private static int SEC_PER_MIN = 60;
   
   public static void main(String args[]) {
      Simulator sim = new Simulator();
      
      sim.setUp();
      System.out.println("Process Workload");
      System.out.println(sim.workLoad.toString() + "\n\n");
      sim.currPageReplacement = new FIFO();
      sim.execute();
      
      sim.setUp();
      sim.currPageReplacement = new LRU(sim.pages);
      sim.execute();
      
      sim.setUp();
      sim.currPageReplacement = new LFU(sim.pages);
      sim.execute();
      
      sim.setUp();
      sim.currPageReplacement = new MFU(sim.pages);
      sim.execute();
      
      sim.setUp();
      sim.currPageReplacement = new RandomPageReplacement();
      sim.execute();
   }
   
   @SuppressWarnings("unchecked")
   private void setUp() {
      random = new Random(1);
      
      runningProcesses = new LinkedList<Process>();
      
      workLoad = generateWorkLoad(NUM_PROCESS);
      
      pages = generatePages(NUM_PAGES);
      freePages = (LinkedList<Page>)pages.clone();
      
      sec = 0;
      numHits = 0 ;
      numMisses = 0;
      numStartedProcesses = 0;
   }
   
   private void execute() throws ClassCastException{
      System.out.println(currPageReplacement.toString());   //print type of algorithm
      paging();
      System.out.println("Number of Hits " + numHits);
      System.out.println("Number of Misses " + numMisses);
      System.out.println("Hit Ratio " + numHits / (numHits + numMisses));
      System.out.println("Miss Ratio " + numMisses / (numHits + numMisses));
      System.out.println("Number of Started Process " + numStartedProcesses);

      System.out.println("\n\n");
   }
   
   private void paging() {
      while(sec < SEC_PER_MIN) {
       if (!workLoad.isEmpty()) {
          //Run new processes
          runNewProcesses();
       }
       
       //Add new page to running processes every 100ms
       addProcessPages();
       
       //Exit complete processes
      checkCompletedProcesses();
       sec++;
    }
   }
   
   private void runNewProcesses() {
      while (freePages.size() >= MIN_FREE_PAGES && !workLoad.isEmpty() &&
            workLoad.peek().getArrival() <= sec) {
         numStartedProcesses++;
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

              retrievePage(currProcess);
            }
         }
      }
   }
   
   private void retrievePage(Process process) {
      Page retrievedPage;
      System.out.print("0:" + String.format("%02d", sec));
      
      if (!freePages.isEmpty()) {
         retrievedPage = freePages.pop();

         if (process.getPages().size() == 0)
            retrievedPage.setContent(0);
         else
            retrievedPage.setContent(localityOfRef(process)); 
         
         process.getPages().add(retrievedPage);
         numHits++;

         System.out.println(" Process " + process.getName() + 
          " getting Page Number " + retrievedPage.getPageNumber() + " as Page "
          + retrievedPage.getContent() + " with no eviction");
      }
      else {
         int evictingPageNumber = currPageReplacement.pageEviction();
         retrievedPage = pages.get(evictingPageNumber);
         Process evictedFrom = evictPage(retrievedPage);
         int oldContent = retrievedPage.getContent();

         if (process.getPages().size() == 0)
            retrievedPage.setContent(0);
         else
            retrievedPage.setContent(localityOfRef(process)); 
         
         process.getPages().add(retrievedPage);
         numMisses++;

         System.out.println(" Process " + process.getName() + 
          " getting Page Number " + retrievedPage.getPageNumber() + " as Page "
          + retrievedPage.getContent() + " evicting from Process " + 
          evictedFrom.getName() + " Page " + oldContent);
      }
      
      retrievedPage.incrementTimesUsed();
      retrievedPage.setLastUsed(sec);
   }
   
   private Process evictPage(Page toFind) {
      ListIterator<Process> iter = runningProcesses.listIterator();
      Process evictedFrom = null;
      
      while (iter.hasNext()) {
         Process currProcess = iter.next();
        
         if(currProcess.getPages().contains(toFind)) {
            currProcess.getPages().remove(currProcess.getPages().indexOf(toFind));
            evictedFrom = currProcess;
         }
      }
      
      return evictedFrom;
   }
   
   private int localityOfRef(Process process) {
      int newPageContent = process.getPages().getLast().getContent();
      
      do {
         int processPageSize = process.getSize();
         
         if (random.nextInt(10) < 7) {
            newPageContent = newPageContent + random.nextInt(3) - 1;
         }
         else {
            newPageContent = newPageContent + random.nextInt(8) + 2;
         }
         
         if (newPageContent < 0)
            newPageContent += processPageSize;
         
         if(newPageContent >= processPageSize)
            newPageContent -= processPageSize;
      } while (hasPageAlready(process, newPageContent));
      
      return newPageContent;
   }
   
   private boolean hasPageAlready(Process process, int pageContent) {
      ListIterator<Page> iter = process.getPages().listIterator();
      boolean hasPage = false;
      
      while (iter.hasNext())
        if (pageContent == iter.next().getContent()) 
           hasPage = true;
      
      return hasPage;
   }
   
   private void checkCompletedProcesses() {
      ListIterator<Process> iter = runningProcesses.listIterator();
      
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
      String enterStr = " Enter";
      
      if (!isEntering)
         enterStr = " Exit";
      
      System.out.print("0:" + String.format("%02d", sec) +  " Process "
            + process.getName() + enterStr + ", Size: " + 
            process.getSize() +" pages," + " Arrival: " + "0:" + 
            String.format("%02d", process.getArrival()) + ", Duration: " 
            + process.getDuration() + "  ");
      
      printMemoryMap();
   }
   
   private LinkedList<Page> generatePages(int numPages) {
      LinkedList<Page> pages = new LinkedList<Page>();
      
      for (int i = 0; i < numPages; i++)
         pages.add(new Page(-1, i));
      
      return pages;
   }
   
   private void printMemoryMap() {
      ListIterator<Process> iter = runningProcesses.listIterator();
      int memMap[] = new int[NUM_PAGES];
      
      for (int currPageNumber = 0; currPageNumber < NUM_PAGES; currPageNumber++) {
         memMap[currPageNumber] = -1;
      }
            
      while(iter.hasNext()) {
         Process currProcess = iter.next();
         
         for (Page currPage : currProcess.getPages()) {
            memMap[currPage.getPageNumber()] = currProcess.getName();
         }
      }
      
      System.out.print("<");
      for (int currPageNumber = 0; currPageNumber < NUM_PAGES; currPageNumber++) {
         if (memMap[currPageNumber] == -1) 
            System.out.print(". ");
         else
            System.out.print("" + memMap[currPageNumber] + " ");
      }
      System.out.println(">");
   }
   
   public LinkedList<Process> generateWorkLoad(int numProcess) {
      LinkedList<Process> workLoad = new LinkedList<Process>();

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
