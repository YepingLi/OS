package model;


import java.util.List;

public abstract interface Device {

  public default void display(Integer processNum, ReadyQueue theReadyQueue,
                              IO io1, IO io2, List<Process> theProcessList)
  {
    System.out.println("=======================START TO DISPLAY MESSAGE=======================");
    String message1;
    if (processNum == null)
    {message1 = "No element in ready Queue right now, waiting for IO handling IO requests";}else {
      message1 = "The ProcessID is " + processNum + "\n" + "The ready queue contains ";
    }

    System.out.println(message1);
    System.out.println(theReadyQueue);

    String message2 = "The content of the wait queue of IO1 is " ;
    System.out.println(message2);
    System.out.println(io1.waitQueue);

    String message3 = "The content of the wait queue of IO2 is ";
    System.out.println(message3);
    System.out.println(io2.waitQueue);

    String message4 = "The PCB of every process in the system is ";
    System.out.println(message4);

    for(Process process : theProcessList){
      System.out.println(process.pcb);
    }


  }

}
