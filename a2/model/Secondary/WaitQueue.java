package model.Secondary;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WaitQueue  {
  Queue<Process> waitQueue;

  public WaitQueue() {
    this.waitQueue = new ConcurrentLinkedQueue<>();

  }

  public void addProcess(Process process)
  {
    process.getPcb().setState(PCBSTATE.WAITING);
    waitQueue.add(process);
  }

  public Queue<Process> getWaitQueue() {
    return waitQueue;
  }

  public Process removeProcess()
  {
    if (waitQueue.size() != 0)
    {return waitQueue.remove();} else
    {return null;}
  }

  @Override
  public String toString() {
    return "WaitQueue{" +
        "waitQueue=" + waitQueue +
        '}';
  }

  public int size(){return waitQueue.size();}

}
