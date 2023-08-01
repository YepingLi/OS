package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CPU implements Device {
  ReadyQueue readyQueue;
  int timeUnit = 2;
  List<IO> IOList = new ArrayList<>();
  Process currentProcess;
  List<Process> processList;
  Boolean duringExecution = false;
  int CPUTimeCounter = 0;
  /**
   *
   * @param readyQueue provided to cpu
   */
  private CPU(ReadyQueue readyQueue) {
    this.readyQueue = readyQueue;
  }

  /**
   * generate a new CPU
   * @param readyQueue provided to cpu
   * @return
   */
  public static CPU CpuGenerator(ReadyQueue readyQueue){
    return new CPU(readyQueue);
  }

  /**
   * Let CPU know what the IOS are there.
   * @param io
   */
  public void addIO(IO io){IOList.add(io);}

  /**
   * To have reference to all processes from the txt file.
   * @param theProcessList
   */
  public void setProcessList(List<Process> theProcessList){
    this.processList = theProcessList;
  }

  public Boolean getDuringExecution() {
    return duringExecution;
  }

  public void setDuringExecution(Boolean duringExecution) {
    this.duringExecution = duringExecution;
  }

  /**
   * start to execute the process
   */
  public void execute() throws InterruptedException {
    System.out.println("The exciting Processing Handling is started!!!");

    //Here we are certain that there are 2 IO devices
    IO io1 = IOList.get(0);
    IO io2 = IOList.get(1);
    Thread t1 = new Thread(io1);
    Thread t2 = new Thread(io2);
    t1.start();
    t2.start();
    while (readyQueue.size() != 0 || io1.waitQueue.size() != 0 || io2.waitQueue.size() != 0)
    {
      //We take the very first process in the front
      currentProcess =  readyQueue.removeProcess();

      // In  such case, ready queue is empty and waiting queue still has element
      if (currentProcess == null)
      {
        display(null, readyQueue, io1, io2, processList);
        try {
          CPUTimeCounter = CPUTimeCounter + 1;
          System.out.println("The current time is " + CPUTimeCounter);
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      continue;
      }


      // we only allow to execute 2 instructions for one process at a time
      while (currentProcess.getNumInsExe() < 2 && currentProcess.pcb.getState() != PCBSTATE.WAITING ){
        //If all instruction of a process finished, remove it from ready queue and set the process state TERMINATED
        //We start from instruction 0, so -1 here.
        if (currentProcess.getCurrentPos() > (currentProcess.getaNumberOfInstructions()-1))
        {
          currentProcess.getPcb().setState(PCBSTATE.TERMINATED);
          break;
        }

        else if (currentProcess.getaIORequestAtInstruction() // If we required IO request
            .contains(currentProcess.getCurrentPos()))
        {
          // Here to check which IO will be used
          int index = currentProcess.getaIODevicesRequested()
              .get(currentProcess.getaIORequestAtInstruction()
                  .indexOf(currentProcess.getCurrentPos()));
          CPUTimeCounter = CPUTimeCounter + 1;
          if(index == 1)
          {
            cpuExecution(currentProcess, io1, io2, processList);
            System.out.println("The current time is " + CPUTimeCounter);
            io1.addProcess(currentProcess);
            currentProcess.numInsExeReset();
          }
          else
          {
            cpuExecution(currentProcess, io1, io2, processList);
            System.out.println("The current time is " + CPUTimeCounter);
            io2.addProcess(currentProcess);
            currentProcess.numInsExeReset();
          }

        }else {
          CPUTimeCounter = CPUTimeCounter + 1;
          cpuExecution(currentProcess, io1, io2, processList);
          System.out.println("The current time is " + CPUTimeCounter);
          System.out.println("+++++++++++++++++++++++END TO DISPLAY MESSAGE+++++++++++++++++++++++");
        }
      }
      //The process is put back into the ready queue for context switch
      //We reset the instruction been executed
      //TERMINATED process cannot be added to ready queue
      currentProcess.numInsExeReset();
      //if(currentProcess.getCurrentPos() >= (currentProcess.getaNumberOfInstructions()-1)){currentProcess.getPcb().setState(PCBSTATE.TERMINATED);}
      if (currentProcess.pcb.getState() != PCBSTATE.WAITING){readyQueue.addProcess(currentProcess);}
    }
    finalAnnouncement(readyQueue, io1, io2, processList);
    System.out.println("The terminated time is " + CPUTimeCounter);
    System.out.println("All Processes are being processed, Great work");
    t1.stop();
    t2.stop();
  }

  public void cpuExecution(Process process, IO io1, IO io2, List<Process> theProcessList)
      throws InterruptedException {
    //increase the num of instructions executed and currentPos by 1
    process.execute();
    TimeUnit.SECONDS.sleep(1);
    display(currentProcess.getaProcessID(), readyQueue, io1, io2, theProcessList);
  }

  public  void finalAnnouncement(ReadyQueue theReadyQueue,
                              IO io1, IO io2, List<Process> theProcessList)
  {
    System.out.println("**********************START TO DISPLAY Final Announcement**********************");
    String message1 = "All processes have benn executed";
    System.out.println(message1);
    System.out.println(theReadyQueue);

    String message2 = "The content of the wait queue of IO1 is " + "\n";
    System.out.println(message2);
    System.out.println(io1.waitQueue);

    String message3 = "The content of the wait queue of IO2 is " + "\n";
    System.out.println(message3);
    System.out.println(io2.waitQueue);

    String message4 = "The PCB of every process in the system is " + "\n";
    System.out.println(message4);

    for(Process process : theProcessList){
      System.out.println(process.pcb);
    }
  }
}
