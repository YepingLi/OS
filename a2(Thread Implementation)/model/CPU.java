package model;

import java.util.List;
import java.util.concurrent.TimeUnit;
import model.Secondary.CPUSTATE;
import model.Secondary.PCBSTATE;
import model.Secondary.Process;
import model.Secondary.ReadyQueue;
import model.Secondary.WaitQueue;
import java.util.concurrent.Semaphore;

public class CPU implements Runnable{
  private CPUSTATE state = CPUSTATE.OFF;
  private ReadyQueue readyQueue;


  private WaitQueue waitQueue;
  private Process currentProcess;
  private Integer expectedExeTime = 10000;
  private Integer CPUTimeCounter;
  private List executedProcess;
  private Integer index;
  private Integer osTime=0;
  private static Semaphore semaphore = new Semaphore(1);

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

  /**
   * Runs this operation.
   */
  @Override
  public void run() {
    while (true){
      try {
      semaphore.acquire();
      // get current currentProcess
      if (readyQueue.size() != 0){currentProcess = readyQueue.getQueue().remove();}
      semaphore.release();
      // currentProcess execution
      if (currentProcess != null){
        //Telling process which cpu is running rn
        currentProcess.setOngoingCPU(index);
        execute(currentProcess);
      }
      currentProcess = null;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }



  private void execute(Process currentProcess) throws InterruptedException{
    Integer actualExeTime = expectedExeTime;

    //if time left time executed less than q
    if (currentProcess.timeLeft() < expectedExeTime){actualExeTime = currentProcess.timeLeft();}

    //Check if this is the 1st time the process is executed
    responseTimeSet(currentProcess);

    //check for termination

    for (int i = 0; i <= actualExeTime; i++){

      //execution time
      currentProcess.getPcb().setState(PCBSTATE.RUNNING);
      CPUTimeCounter ++;
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      // check for IO
      if(currentProcess.getIO_RequestAtTimes().contains(currentProcess.getCurrentTime())){
        currentProcess.execute();
        currentProcess.getPcb().setState(PCBSTATE.WAITING);

        semaphore.acquire();
        waitQueue.addProcess(currentProcess);
        semaphore.release();

        return;
      }else {
        // no IO execute normally
        currentProcess.execute();
      }
    }
    if (currentProcess.timeLeft()<=0){
      currentProcess.getPcb().setState(PCBSTATE.TERMINATED);
      //Make sure that no cpu is handling this processes
      currentProcess.setOngoingCPU(-2);
      //set Turnaround Time
      setTurnaround(currentProcess);
      semaphore.acquire();
      executedProcess.add(currentProcess);
      semaphore.release();
      return;
    }
    if(currentProcess.getPcb().getState() == PCBSTATE.RUNNING){
      currentProcess.getPcb().setState(PCBSTATE.READY);
      readyQueue.addProcess(currentProcess);
    }
  }
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
}
