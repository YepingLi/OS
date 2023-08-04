package model.UsageCalculation;
import model.Secondary.Process;

import java.util.List;

public class WaitTime {
  private WaitTime() {
  }
  public static WaitTime WaitTimeCalGenerator(){return new WaitTime();}

  public void waitTimeDisplay(List<Process> processes){
    processes.forEach(process -> System.out.println("The process "+ (process.getaProcessID())
        +"'s Wait time is " + process.getWaitTime() + " seconds"));
    float totalWaitTime = 0;
    for (Process process : processes){totalWaitTime = totalWaitTime + (float) process.getWaitTime();}
    float averageWaitTime = totalWaitTime/processes.size();
    System.out.println("The average wait time is " + averageWaitTime + " seconds");
  }

}
