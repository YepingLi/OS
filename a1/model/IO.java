package model;


import java.util.List;
import java.util.concurrent.TimeUnit;

public class IO<Public> implements Device,Runnable{
  ReadyQueue readyQueue;
  WaitQueue waitQueue;
  String name;
  Process currentProcess;
  Boolean duringExecution = false;

  int timeUnit = 5;

  /**
   *
   * @param waitQueue provied to IO
   */

  private IO(WaitQueue waitQueue, String name, ReadyQueue readyQueue) {
    this.waitQueue = waitQueue;
    this.name = name;
    this.readyQueue = readyQueue;
  }

  /**
   * generate a new IO
   * @param waitQueue provided to IO
   * @return
   */

  public static IO IOGenrator (WaitQueue waitQueue, String name, ReadyQueue readyQueue){
    return new IO(waitQueue, name, readyQueue);
  }

  public int size(){return waitQueue.size();}

  public void addProcess(Process process){waitQueue.addProcess(process);
    process.pcb.setState(PCBSTATE.WAITING);}


  /**
   * Runs this operation.
   */
  @Override
  public void run() {
    while (true){
      if (waitQueue.size() != 0){
        currentProcess = waitQueue.waitQueue.peek();
        //waitQueue.addProcess(currentProcess);
        try {
          TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        waitQueue.removeProcess();
        readyQueue.addProcess(currentProcess);
      }
    }
  }
}
