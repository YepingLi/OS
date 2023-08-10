package model;

import java.util.List;
import java.util.concurrent.TimeUnit;
import model.Secondary.CPUSTATE;
import model.Secondary.PCBSTATE;
import model.Secondary.Process;
import model.Secondary.ReadyQueue;
import model.Secondary.WaitQueue;


public class CPU {
  private CPUSTATE state = CPUSTATE.OFF;
  private ReadyQueue readyQueue;


  private WaitQueue waitQueue;
  private Process currentProcess;
  private Integer expectedExeTime = 10000;
  private Integer CPUTimeCounter;
  private List executedProcess;
  private Integer index;
  private Integer osTime=0;
  private Integer accumulatedExeTime=0;
  private Process previousProcess = null;
  private boolean invokeWait = false;
  private boolean invokeTer = false;
  private boolean invokeReady = false;

  private CPU(ReadyQueue readyQueue, WaitQueue waitQueue, Integer expectedExeTime, List executedProcess, Integer index) {
    this.readyQueue = readyQueue;
    this.waitQueue = waitQueue;
    this.expectedExeTime = expectedExeTime;
    this.executedProcess = executedProcess;
    this.index = index;
    this.CPUTimeCounter = 0;
  }


  public static CPU CPUGenerator(ReadyQueue readyQueue, WaitQueue waitQueue, Integer expectedExeTime, List executedProcess, Integer index) {
    return new CPU(readyQueue, waitQueue,  expectedExeTime, executedProcess, index);
  }




  public void execute()  {

    //Check if this is the 1st time the process is executed
    responseTimeSet(currentProcess);

      //execution time
    currentProcess.getPcb().setState(PCBSTATE.RUNNING);
    CPUTimeCounter ++;

    // check for IO
    if(currentProcess.getIO_RequestAtTimes().contains(currentProcess.getCurrentTime())){
      currentProcess.execute();
      state = CPUSTATE.OFF;
      invokeWait = true;
      invokeReady = false;
    }else {
        // no IO execute normally
        currentProcess.execute();
        invokeReady = false;
      }
    previousProcess = currentProcess;
    if (currentProcess.timeLeft()<=0){
      invokeTer = true;
      invokeReady = false;
      invokeWait = false;
      //set Turnaround Time
      setTurnaround(currentProcess);
      executedProcess.add(currentProcess);
      state = CPUSTATE.OFF;
    }
      currentProcess.setOngoingCPU(index);

  }

  public void exeTerminationCheck(){
    if(previousProcess.getPcb().getState() == PCBSTATE.RUNNING){
    previousProcess.getPcb().setState(PCBSTATE.READY);
    readyQueue.addProcess(currentProcess);
    setInvokeReady(false);
    this.setState(CPUSTATE.OFF);
    }}

  public Integer getIndex() {
    return index;
  }

  public Integer getCPUTimeCounter() {
    return CPUTimeCounter;
  }

  public void setOsTime(Integer osTime) {
    this.osTime = osTime;
  }
  public void setTurnaround(Process process){process.setTurnaround(getOsTime() - process.getArrivalTime());}

  public Integer getOsTime() {
    return osTime;
  }

  private void responseTimeSet(Process process){
    Integer unSureResTime= getOsTime() - process.getArrivalTime();
    if (process.getResponseTime() > unSureResTime && process.getPcb().getState() == PCBSTATE.READY){process.setResponseTime(unSureResTime);}
  }

  public Process getCurrentProcess() {
    return currentProcess;
  }

  public void setAccumulatedExeTime(Integer accumulatedExeTime) {
    this.accumulatedExeTime = accumulatedExeTime;
  }

  public Integer getAccumulatedExeTime() {
    return accumulatedExeTime;
  }

  public void setState(CPUSTATE state) {
    this.state = state;
  }

  public void setCurrentProcess(Process currentProcess) {
    this.currentProcess = currentProcess;
  }

  public CPUSTATE getState() {
    return state;
  }

  public boolean isInvokeWait() {
    return invokeWait;
  }
  public boolean isInvokeTer() {
    return invokeTer;
  }
  public void invokeWait(){
    waitQueue.addProcess(previousProcess);
    invokeWait = false;
    invokeReady = false;
    this.setState(CPUSTATE.OFF);
    this.setAccumulatedExeTime(0);
  }
  public void invokeTer(){
    currentProcess.getPcb().setState(PCBSTATE.TERMINATED);
    //Make sure that no cpu is handling this processes
    currentProcess.setOngoingCPU(-2);
    invokeTer = false;
    invokeReady = false;
    this.setState(CPUSTATE.OFF);
    this.setAccumulatedExeTime(0);
  }

  public void setInvokeReady(boolean invokeReady) {
    this.invokeReady = invokeReady;
  }
  public boolean isInvokeReady(){return invokeReady;}
}

