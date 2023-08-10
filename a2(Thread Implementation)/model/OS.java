package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import model.Secondary.Process;
import model.Secondary.ReadyQueue;
import model.Secondary.WaitQueue;
import model.UsageCalculation.CpuUtilization;
import model.UsageCalculation.ResponseTime;
import model.UsageCalculation.Turnaround;
import model.UsageCalculation.WaitTime;


public class OS {
  //acquire from file reader
  private Integer q;
  private Integer numberOfCPU;
  private List<Process> processArrayList;
  private model.Algorithm.algorithm algorithm;

  //Attribute will be created during execution
  private ReadyQueue readyQueue;
  private WaitQueue waitQueue;
  private List<CPU> cpuList;
  private IO io;
  private ExecutorService threadPool;
  private Thread ioThread;
  private List<Integer> totalExeTime;
  private List executedProcess;


  private OS(Integer q, Integer numberOfCPU, List<Process> processArrayList) {
    this.q = q;
    this.numberOfCPU = numberOfCPU;
    this.processArrayList = processArrayList;
  }

  public static OS osGenerator(Integer q, Integer numberOfCPU, List<Process> processArrayList){
    return new OS(q, numberOfCPU, processArrayList);
  }


  private OS(Integer q, Integer numberOfCPU, List<Process> processArrayList,
            model.Algorithm.algorithm algorithm) {
    this.q = q;
    this.numberOfCPU = numberOfCPU;
    this.processArrayList = processArrayList;
    this.algorithm = algorithm;
  }

  public static OS osGenerator (Integer q, Integer numberOfCPU, List<Process> processArrayList,
                                model.Algorithm.algorithm algorithm){
    return new OS(q, numberOfCPU, processArrayList, algorithm);
  }


  //logic for the execution
  public void execute(){
    executePrep();
    // if all processes are executed, stop the program
    while (executedProcess.size() != processArrayList.size()){
      try {
        //check at current time, if any process arrive
        for (Process process : processArrayList){
          if(process.getArrivalTime() == totalExeTime.get(0)){readyQueue.addProcess(process);}
        }

        display();

        //for (CPU cpu: cpuList){System.out.println(cpu.getCPUTimeCounter());}
        waitTimeInc(readyQueue);
        //Inform CPUS the current system time
        informCpuSystemTime(cpuList);

        TimeUnit.SECONDS.sleep(1);

        //Inc the time counter by 1
        totalExeTime.set(0, (totalExeTime.get(0) + 1));

        //Inform CPUS the current system time
        informCpuSystemTime(cpuList);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    //finishing everything
    display();
    //Display CpuUtilization
    CpuUtilization cpuUtilization = CpuUtilization.CpuUtilizationGenerator();
    cpuUtilization.displayCpuUtilization(cpuList, totalExeTime);
    //Display WaitTime
    WaitTime waitTime = WaitTime.WaitTimeCalGenerator();
    waitTime.waitTimeDisplay(processArrayList);
    //Display Turnaround time
    Turnaround turnaroundTime = Turnaround.TurnaroundGenerator();
    turnaroundTime.turnaroundTimeDisplay(processArrayList);
    //Display Response Time
    ResponseTime responseTime = ResponseTime.responseTimeGenerator();
    responseTime.responseTimeDisplay(processArrayList);

    threadPool.shutdown();
    ioThread.stop();
    System.out.println("EXECUTION DONE, GREAT JOB");


  }



  //prepare for the execution
  private void executePrep() {
    this.totalExeTime = new ArrayList<>();
    this.totalExeTime.add(0,0);
    //Assign the initial ready queue and wait queue
    readyQueue = new ReadyQueue(new ArrayList<>());
    waitQueue = new WaitQueue();
    io = IO.IOGenrator(waitQueue, "io", readyQueue);
    cpuList = new ArrayList<>();
    //list contains all executed processes
    executedProcess = new ArrayList<>();

    //create a thread pool with certain number of cpu
    threadPool = Executors.newFixedThreadPool(numberOfCPU);
    for (int i = 0; i < numberOfCPU; i++) {
      CPU cpu = CPU.CPUGenerator(readyQueue, waitQueue, q, executedProcess, i);
      cpuList.add(cpu);
      threadPool.execute(cpu);
    }
    //create the thread for io
    ioThread = new Thread(io);
    ioThread.start();
    //Now all cpus and io are running as threads.
  }

  private void display(){
    System.out.println("==========================DISPLAY START==========================");
    for (Process process : processArrayList){System.out.println(process);}
    System.out.println("the ready queue is " + readyQueue);
    System.out.println("the wait queue is " + waitQueue);
    System.out.println("total execution time is " + totalExeTime);
  }

  private void waitTimeInc(ReadyQueue readyQueue){
    readyQueue.getQueue().forEach(Process::incWaitTime);
  }

  private void informCpuSystemTime(List<CPU> cpus){cpus.forEach(cpu -> cpu.setOsTime(totalExeTime.get(0)));}
}
