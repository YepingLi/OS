package model.Secondary;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ReadyQueue {
  Queue<Process> readyQueue;


  public ReadyQueue(List<Process> processList) {
    this.readyQueue = new ConcurrentLinkedQueue<>();;

    for(Process process : processList){
      process.getPcb().setState(PCBSTATE.READY);
      readyQueue.add(process);
    }
  }

  // If statement makes sure that TERMINATED process cannot be added to ready queue
  public void addProcess(Process process)
    {if (process.getPcb().getState() != PCBSTATE.TERMINATED)
      {
        process.getPcb().setState(PCBSTATE.READY);
        readyQueue.add(process);
      }
    }

  public Queue<Process> getQueue() {
    return readyQueue;
  }

  public Process removeProcess()
    {
      if (readyQueue.size() != 0)
      {return readyQueue.remove();} else
      {return null;}
    }

    public int size(){return readyQueue.size();}


  @Override
  public String toString() {
    return "ReadyQueue{" +
        "readyQueue=" + readyQueue +
        '}';
  }

}
