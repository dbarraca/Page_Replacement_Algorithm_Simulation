import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class Simulator {

   private Random random;
   
   private static int NUM_PROCESS = 150;
   private static int MAX_ARRIVE_TIME = 60;
   private static int NUM_PAGE_SIZES = 4;
   private static int NUM_DURATIONS = 5;
   
   public static void main(String args[]) {

      Simulator sim = new Simulator();
      
      LinkedList<Process> processes = sim.generateWorkLoad(NUM_PROCESS);
      Collections.sort(processes);
      System.out.println(processes.toString());
   }
   
   public LinkedList<Process> generateWorkLoad(int numProcess) {
      LinkedList<Process> workLoad = new LinkedList<Process>();
      random = new Random(1);
      int durations[] = distributeParam(numProcess, NUM_DURATIONS);
      int pageSizes[] = distributeParam(numProcess, NUM_PAGE_SIZES);
      
      for (int i = 0; i < numProcess; i++) {
         workLoad.add(new Process(i, translatePageSize(pageSizes[i]), random.nextInt(MAX_ARRIVE_TIME), durations[i] + 1));
      }
      
      return workLoad;
   }
   
   private int translatePageSize(int pageSizeCode) {
      int pageSize = 5;
      
      if (pageSizeCode == 0) {
         pageSize = 5;
      }
      else if (pageSizeCode == 1) {
         pageSize = 11;
      }
      else if (pageSizeCode == 2) {
         pageSize = 17;
      }
      else if (pageSizeCode == 3) {
         pageSize = 31;
      }
      
      return pageSize;
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
