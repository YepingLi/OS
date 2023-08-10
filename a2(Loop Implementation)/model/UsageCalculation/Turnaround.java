package model.UsageCalculation;

import java.util.List;
import model.Secondary.Process;

public class Turnaround {
  private Turnaround() {
  }
  public static Turnaround TurnaroundGenerator(){return new Turnaround();}

  public void turnaroundTimeDisplay(List<Process> processes){

    for (Process process:processes){
      System.out.println("The turnaround Time of Process " + process.getaProcessID() + " is "
      + process.getTurnaround() + " seconds");
    }
  }
}
