package model.Secondary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import model.Algorithm.Algorithm;
import model.Algorithm.algorithmType;


public class ReadyQueue {
  private Queue<Process> readyQueue;
  private algorithmType algorithm;


  public ReadyQueue(List<Process> processList) {
    this.readyQueue = new ConcurrentLinkedQueue<>();;

    for(Process process : processList){
      process.getPcb().setState(PCBSTATE.READY);
      readyQueue.add(process);
    }
  }

  // If statement makes sure that TERMINATED process cannot be added to ready queue
  public void addProcess(Process process){
        if (process.getPcb().getState() != PCBSTATE.TERMINATED) {
          process.getPcb().setState(PCBSTATE.READY);
          readyQueue.add(process);
        }
  }

  public Queue<Process> getQueue() {
    return readyQueue;
  }

  public Process removeProcess()
    {
      if(algorithm == algorithmType.SJF){
        List<Process> tempList = new ArrayList<>();
        while (readyQueue.size() != 0) {
          tempList.add(readyQueue.remove());
        }
        Process shortestJob = tempList.stream()
            .min(Comparator
                .comparing(Process::timeLeft)).get();
        tempList.remove(shortestJob);
        readyQueue.addAll(tempList);
        return shortestJob;
      } else {
        if (readyQueue.size() != 0) {
          return readyQueue.remove();
        } else {
          return null;
        }
      }
    }

    public int size(){return readyQueue.size();}

  public void setAlgorithm(algorithmType algorithm) {
    this.algorithm = algorithm;
  }

  @Override
  public String toString() {
    return "ReadyQueue{" +
        "readyQueue=" + readyQueue +
        '}';
  }

}
