package model;

import static model.Algorithm.algorithmType.SJF;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import model.Algorithm.FCFS;
import model.Algorithm.RR;
import model.Algorithm.SJF;
import model.Algorithm.algorithmType;
import model.Secondary.CPUSTATE;
import model.Secondary.Process;
import model.Secondary.ReadyQueue;
import model.Secondary.WaitQueue;
import model.UsageCalculation.CpuUtilization;
import model.UsageCalculation.ResponseTime;
import model.UsageCalculation.Turnaround;
import model.UsageCalculation.WaitTime;


public class Scheduler {
  //acquire from file reader
  private Integer q;
  private Integer qApplied;
  private Integer numberOfCPU;
  private List<Process> processArrayList;
  private algorithmType algorithmType;

  //Attribute will be created during execution
  private ReadyQueue readyQueue;
  private WaitQueue waitQueue;
  private List<CPU> cpuList;
  private IO io;
  private Thread ioThread;
  private List<Integer> totalExeTime;
  private List executedProcess;



  private Scheduler(Integer q, Integer numberOfCPU, List<Process> processArrayList,
                    algorithmType algorithmType) {
    this.q = q;
    this.numberOfCPU = numberOfCPU;
    this.processArrayList = processArrayList;
    this.algorithmType = algorithmType;
    this.qApplied = q;
  }

  public static Scheduler osGenerator (Integer q, Integer numberOfCPU, List<Process> processArrayList,
                                       algorithmType algorithmType){
    return new Scheduler(q, numberOfCPU, processArrayList, algorithmType);
  }


  //logic for the execution
  public void execute(){
    executePrep();
    algorithmApply();
    // if all processes are executed, stop the program
    while (executedProcess.size() != processArrayList.size()){
      try {
        //check at current time, if any process arrive
        for (Process process : processArrayList){
          if(process.getArrivalTime() == totalExeTime.get(0)){readyQueue.addProcess(process);}
        }
        tasksAssign();
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
    //Final Termination check
    finalTerCheck();
    //finishing everything
    display();
    //usageDisplay
    usageDisplay(cpuList,processArrayList,totalExeTime);
    ioThread.stop();
    System.out.println(executedProcess);
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

    for (int i = 0; i < numberOfCPU; i++) {
      CPU cpu = CPU.CPUGenerator(readyQueue, waitQueue, q, executedProcess, i);
      cpuList.add(cpu);
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

  private void tasksAssign() throws InterruptedException {
    for (CPU cpu : cpuList){
      if(cpu.isInvokeWait()){
        cpu.invokeWait();
      }
      if(cpu.isInvokeTer()){
        cpu.invokeTer();
      }
      if(cpu.isInvokeReady()){
        cpu.exeTerminationCheck();
      }
      System.out.println(cpu.getState());
      if (cpu.getState() == CPUSTATE.OFF){
        if (readyQueue.size() > 0){
          cpu.setCurrentProcess(readyQueue.removeProcess());
          cpu.setState(CPUSTATE.ON);
          cpu.execute();
          //special case handling
          if (qApplied==1){cpu.setState(CPUSTATE.OFF);
            cpu.setInvokeReady(true);
            cpu.setAccumulatedExeTime(0);}

          cpu.setAccumulatedExeTime(cpu.getAccumulatedExeTime() + 1);
        } else {cpu.setState(CPUSTATE.OFF);}
      }else {
        if(cpu.getAccumulatedExeTime() < (qApplied-1)){
          cpu.execute();
          cpu.setAccumulatedExeTime(cpu.getAccumulatedExeTime() + 1);
          cpu.setInvokeReady(false);
        }else {
          if(q != 1){cpu.execute();}
          cpu.setInvokeReady(true);
          cpu.setAccumulatedExeTime(0);
          cpu.setState(CPUSTATE.OFF);
        }
      }
    }
  }
  private void finalTerCheck(){
    for (CPU cpu : cpuList) {

      if (cpu.isInvokeTer()) {
        cpu.invokeTer();
      }
    }
  }
  private void usageDisplay(List<CPU> cpuList, List<Process> processArrayList, List<Integer> totalExeTime){
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
  }

  public void setQApplied(Integer f) {
    this.qApplied = f;
  }

  private void algorithmApply(){
    if (algorithmType == algorithmType.RR){
      RR rr = RR.RRGenerator();
      rr.algorithmPrep(this);
      rr.algorithmApply();
    } else if (algorithmType == SJF){
      SJF sjf = model.Algorithm.SJF.SJFGenerator();
        setQApplied(100000000);
        sjf.algorithmPrep(this);
        sjf.algorithmApply();
    }else {
      FCFS fcfs = FCFS.FCFSGenerator();
      setQApplied(100000000);
      fcfs.algorithmPrep(this);
      fcfs.algorithmApply();
    }
  }

  public ReadyQueue getReadyQueue() {
    return readyQueue;
  }
}
