import java.util.*;

public class Simulator {

   private Random random;    // random number generator
   private LinkedList<Page> pages;    // all pages in page number order
   private LinkedList<Page> freePages;    // List of free pages
   private LinkedList<Process> workLoad;    // Processes in work load
   private LinkedList<Process> runningProcesses;    // Process currently running
   private int sec;   // current second in time
   private PageReplacement currPageReplacement;   // page replacement algorithm
   private float numHits;   // number of paging hits
   private float numMisses;   // number of paging misses
   private int numStartedProcesses;   // number of processes that have been swapped in
   
   private static int NUM_PROCESS = 150;
   private static int MAX_ARRIVE_TIME = 60;
   private static int NUM_SIZES = 4;
   private static int NUM_DURATIONS = 5;
   private static int NUM_PAGES = 100;
   private static int MIN_FREE_PAGES = 4;
   private static int SEC_PER_MIN = 60;
   
   public static void main(String args[]) {
      Simulator sim = new Simulator();
      
      sim.setUp();                                          // set up data structures
      System.out.println("Processes in Workload");
      System.out.println(sim.workLoad.toString() + "\n");   // print processes in workload
      sim.currPageReplacement = new FIFO();                 // first in first out  page replacement 
      sim.execute();                                        // execute simulation
      
      sim.setUp();                                          // set up data structures
      sim.currPageReplacement = new LRU(sim.pages);         // least recently used page replacement 
      sim.execute();                                        // execute simulation
      
      sim.setUp();                                          // set up data structures
      sim.currPageReplacement = new LFU(sim.pages);         // least frequently used page replacement 
      sim.execute();                                        // execute simulation
      
      sim.setUp();                                          // set up data structures
      sim.currPageReplacement = new MFU(sim.pages);         // most frequently used page replacement 
      sim.execute();                                        // execute simulation
      
      sim.setUp();                                          // set up data structures
      sim.currPageReplacement = new RandomPageReplacement();// set up random page replacement
      sim.execute();                                        // execute simulation

   }
   
   /**
    * Set up data structures needed for paging.
    */
   @SuppressWarnings("unchecked")
   private void setUp() {
      random = new Random(1);                       // initialize RNG
      
      runningProcesses = new LinkedList<Process>(); // initialize running processes list
      
      workLoad = generateWorkLoad(NUM_PROCESS);     // generate workload processes
      
      pages = generatePages(NUM_PAGES);             // generate pages
      freePages = (LinkedList<Page>)pages.clone();  // get reference for free pages list
      
      sec = 0;                                      // reset time
      numHits = 0 ;                                 // reset hit count
      numMisses = 0;                                // reset miss count
      numStartedProcesses = 0;                      // reset started process count
   }
   
   /**
    * Executes paging simulation.
    * 
    */
   private void execute(){
      System.out.println(currPageReplacement.toString());                     // print type of algorithm
      paging();                                                               // start paging
      System.out.println("Number of Hits " + numHits);                        // print hit count
      System.out.println("Number of Misses " + numMisses);                    // print miss count
      System.out.println("Hit Ratio " + numHits / (numHits + numMisses));     // print hit ratio
      System.out.println("Miss Ratio " + numMisses / (numHits + numMisses));  // print miss ratio
      System.out.println("Number of Started Process " + numStartedProcesses); // print start process count

      System.out.println("\n");
   }
   
   /**
    * Runs Paging for process. Enters process at arrival, adds page to 
    * processes, and exits complete processes.
    */
   private void paging() {
      while(sec < SEC_PER_MIN) {   // run for one minute
         if (!workLoad.isEmpty()) // check if there is more workload processes
          runNewProcesses();      //Run new processes
 
         addProcessPages();         //Add new page to running processes every 100ms
 
         checkCompletedProcesses();  //Exit complete processes
         sec++;
      }
   }
   
   /**
    * Run new processes from workload at arrival time.  Processes are only run
    * when there are at least four free pages available.
    */
   private void runNewProcesses() {
      while (freePages.size() >= MIN_FREE_PAGES && !workLoad.isEmpty() && // check arrival time, workload size, 
            workLoad.peek().getArrival() <= sec) {                        // and free page list size 
         numStartedProcesses++;                                           // increment started process count
         Process currProcess = workLoad.pop();                            // get next process in workload
         currProcess.setStart(sec);                                       // mark start time of process
         runningProcesses.add(currProcess);                               // set process to running
         printProcessStatus(currProcess, true);                           // print entering process message
      }
   }
   
   /**
    * Adds pages to running processes every 100 msec.
    */
   private void addProcessPages() {
      System.out.print("Time 0:" + String.format("%02d", sec));                                 // print time stamp
      for (int msec = 0; msec < 1000; msec += 100) {                                            // count msec
         for (ListIterator<Process> iter = runningProcesses.listIterator(); iter.hasNext();) {  // set up iterator
            Process currProcess = iter.next();                                                  // get current process
            if (currProcess.getPages().size() < currProcess.getSize()) {                        // check if process already has all pages
              retrievePage(currProcess);                                                        // retrieve page to be added to process
            }
         }
      }
      System.out.println("");
   }
   
   /**
    * Retrieves a page from free page or steals page from another process.
    * 
    * @param process the process the retrieved page is to be added to.
    */
   private void retrievePage(Process process) {
      Page retrievedPage;
      
      if (!freePages.isEmpty()) {                                    // check if there are no more free pages
         retrievedPage = freePages.pop();                            // get a free page

         if (process.getPages().size() == 0)                         // check if first page for process
            retrievedPage.setContent(0);                             // set to default first page in memory
         else
            retrievedPage.setContent(localityOfRef(process));        // get next page in memory
         
         process.getPages().add(retrievedPage);                      // add page to process
         numHits++;                                                  //increment hit count

         System.out.print(" Process " + process.getName() +          // print no eviction memory reference message
          " getting Page " + retrievedPage.getPageNumber() + " set to "
          + retrievedPage.getContent() + " in memory with no eviction |  ");
      }
      else {
         int evictingPageNumber = currPageReplacement.pageEviction();// get next evicting page number 
         retrievedPage = pages.get(evictingPageNumber);              // get page to be evicted
         Process evictedFrom = evictPage(retrievedPage);             // get process page is to be evicted from
         int oldContent = retrievedPage.getContent();                // get page in memory before page is evicted

         if (process.getPages().size() == 0)                         // check if first page for process
            retrievedPage.setContent(0);                             // set to default first page in memory
         else
            retrievedPage.setContent(localityOfRef(process));        // get next page in memory
         
         process.getPages().add(retrievedPage);                      // add page to process
         numMisses++;                                                // increment miss count

         System.out.print(" Process " + process.getName() +          // print eviction memory reference message
          " getting Page " + retrievedPage.getPageNumber() + " set to "
          + retrievedPage.getContent() + " in memory evicting Process" + 
          evictedFrom.getName() + " Page " + oldContent + " |  ");
      }
      
      retrievedPage.incrementTimesUsed();                            // increment times page was used
      retrievedPage.setLastUsed(sec);                                // set time page was last used
   }
   
   /**
    * Find the process the page is being held and evicts the page.
    * 
    * @param toFind The page to be found
    * @return The process the page was evicted from.
    */
   private Process evictPage(Page toFind) {
      ListIterator<Process> iter = runningProcesses.listIterator();  
      Process evictedFrom = null;
      
      while (iter.hasNext()) {                                                      // iterate through running processes
         Process currProcess = iter.next();                                         // get next process
        
         if(currProcess.getPages().contains(toFind)) {                              // check if process has page
            currProcess.getPages().remove(currProcess.getPages().indexOf(toFind));  // remove the page from the process
            evictedFrom = currProcess;                                              // get process page was evicted from
         }
      }
      
      return evictedFrom;                                                           // return process page was evicted from
   }
   
   /**Returns next page in memory for the given process.
    * 
    * 
    * @param process the process the page is to be added to
    * @return the page in memory
    */
   private int localityOfRef(Process process) {
      int newPageContent = process.getPages().getLast().getContent();
      
      do {
         int processPageSize = process.getSize();                       // get process' page size
         
         if (random.nextInt(10) < 7) {                                  // 70% of pages, page are closer to last page in memory
            newPageContent = newPageContent + random.nextInt(3) - 1;    // set next page in memory
         }
         else {
            newPageContent = newPageContent + random.nextInt(8) + 2;    // set next page in memory
         }
         
         if (newPageContent < 0)                                        // check for underflow
            newPageContent += processPageSize;                          // set page to within page size range
         
         if(newPageContent >= processPageSize)                          // check for overflow
            newPageContent -= processPageSize;                          // set page to within page size range
      } while (hasPageAlready(process, newPageContent));                // check if process already has the page in memory
      
      return newPageContent;                                             // return next age in memory
   }
   
   /**
    * Checks if the given page is already in the given process.
    * 
    * @param process The process to be checked if page is in.
    * @param pageContent The page checked is in process
    * @return true if page is in process, false if page is not in process
    */
   private boolean hasPageAlready(Process process, int pageContent) {
      ListIterator<Page> iter = process.getPages().listIterator();
      boolean hasPage = false;
      
      while (iter.hasNext()) // iterate through pages
        if (pageContent == iter.next().getContent()) // check if page is in process
           hasPage = true; // mark page is in process
      
      return hasPage; // return flag marking presence of page in proces
   }
   
   /**
    * Check if any new processes have reached duration.
    */
   private void checkCompletedProcesses() {
      ListIterator<Process> iter = runningProcesses.listIterator();
      
      while (iter.hasNext()) { // iterate through processes
         Process currProcess = iter.next(); // get current process
         if (currProcess.getStart() + currProcess.getDuration() <= sec) { // check if process has ran for duration
            printProcessStatus(currProcess, false); // print process exiting message
            freePages.addAll(currProcess.getPages()); // set process' pages to free
            iter.remove(); // stop running process
         }
      }
   }
   
   /**
    * Prints process entering or exiting message.
    * 
    * @param process The process entering or exiting
    * @param isEntering Flag marking if process is entering or exiting
    */
   private void printProcessStatus(Process process, boolean isEntering) {
      String enterStr = " Enter";
      
      if (!isEntering)                                                         // check if process is entering
         enterStr = " Exit";                                                   // set process to exiting
      
      System.out.print("Time 0:" + String.format("%02d", sec) +  " Process "   // print process status message
            + process.getName() + enterStr + ", Size: " + 
            process.getSize() +" pages," + " Arrival: " + "0:" + 
            String.format("%02d", process.getArrival()) + ", Duration: " 
            + process.getDuration() + "  ");
      
      printMemoryMap();                                                        // print current memory map
   }
   
   /**
    * Generates new pages.
    * 
    * @param numPages The number of pages to be generated
    * @return A reference to the generated pages
    */
   private LinkedList<Page> generatePages(int numPages) {
      LinkedList<Page> pages = new LinkedList<Page>();
      
      for (int i = 0; i < numPages; i++) // count number of pages to generate
         pages.add(new Page(-1, i)); // generate a anew page
      
      return pages; // return a reference to newly generated pages
   }
   
   /**
    * Print a memory map of the process ID names.
    */
   private void printMemoryMap() {
      ListIterator<Process> iter = runningProcesses.listIterator();
      int memMap[] = new int[NUM_PAGES];
      
      for (int currPageNumber = 0; currPageNumber < NUM_PAGES; currPageNumber++) { // iterate through pages by page number
         memMap[currPageNumber] = -1; // initialize memory map place to impossible process name
      }
            
      while(iter.hasNext()) { // iterate through running processes
         Process currProcess = iter.next(); // get next process
         
         for (Page currPage : currProcess.getPages()) { // iterate through processes pages
            memMap[currPage.getPageNumber()] = currProcess.getName(); // set memeory map page to proces name
         }
      }
      
      // print memeory map
      System.out.print("<");
      for (int currPageNumber = 0; currPageNumber < NUM_PAGES; currPageNumber++) {
         if (memMap[currPageNumber] == -1)  // check if map place is empty
            System.out.print(". ");
         else
            System.out.print("" + memMap[currPageNumber] + " ");
      }
      System.out.println(">");
   }
   
   /**
    * Generate process work load.
    * 
    * @param numProcess Number of processes to be generated
    * @return returns the workload of processes
    */
   public LinkedList<Process> generateWorkLoad(int numProcess) {
      LinkedList<Process> workLoad = new LinkedList<Process>();

      int durations[] = distributeParam(numProcess, NUM_DURATIONS);
      int sizes[] = distributeParam(numProcess, NUM_SIZES);
      
      for (int i = 0; i < numProcess; i++) { // count processes
         workLoad.add(new Process(i, translateSize(sizes[i]), random.nextInt(MAX_ARRIVE_TIME), durations[i] + 1)); // add process to workload;
      }
      
      Collections.sort(workLoad); // sort workload according to arrival time
      
      return workLoad;// return workload of processes
   }
   
   /**
    * Translates generate random numbers to the different page sizes.
    * 
    * @param sizeCode The generate random number
    * @return A page size in MB.
    */
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
      
      return size; // The translate page size in MB.
   }
   
   /**
    * Evenly distributes a parameter (page size or duration) across all processes
    * 
    * @param numProcess Number of Processes
    * @param numParam Number of different possible values for the parameter
    * @return The evenly distributed parameter values.
    */
   private int[] distributeParam(int numProcess,int numParam) {
      int paramValues[] = new int[numProcess];
      int processesPerParam = numProcess / numParam;
      int paramCounts[] = new int[numParam];
      
      for (int i = 0; i < numProcess; i++) { // count number of processes
         int currIndex = random.nextInt(numProcess); // generate random process ID
         int nearFull = 1;
         
         while(paramValues[currIndex] != 0) { // check if parameter value for process is set yet
            currIndex = random.nextInt(numProcess); // generate a random process ID
         }
         
         for (int j = 0; j < numParam; j++) // count number of parameters
            if (paramCounts[j] < processesPerParam) // check if there are enough process set to this parameter value
               nearFull = 0;
         
         processesPerParam += nearFull; // only increment if not near max amount of each parameter value
         
         do {
            paramValues[currIndex] = random.nextInt(numParam);
         } while (paramCounts[paramValues[currIndex]] >= processesPerParam); // check if at max of amount of parameter value
         
         paramCounts[paramValues[currIndex]]++; // increment number of instence a process was set to current paramter value
      }
      
      return paramValues; //returns parameter values
   }
}
