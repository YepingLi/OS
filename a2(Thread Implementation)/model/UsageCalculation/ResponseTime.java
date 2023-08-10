package model.UsageCalculation;

import java.util.List;
import model.Secondary.Process;

public class ResponseTime {
  private ResponseTime() {
  }
  public static ResponseTime responseTimeGenerator(){return new ResponseTime();}

  public void responseTimeDisplay(List<Process> processes){
    for (Process process : processes){
      System.out.println("The response time of Process " + process.getaProcessID() +
          " is " + process.getResponseTime() + " seconds");
    }
  }
}
