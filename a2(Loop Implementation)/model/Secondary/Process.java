package model.Secondary;

import java.util.ArrayList;
import java.util.List;

public class Process {
  private String aProcessID;
  private int  arrivalTime;
  private int  totalExecTime;
  private List<Integer> IO_RequestAtTimes;
  private PCB pcb;
  private int CurrentTime = 0;
  private List ongoingCPU;
  private Integer waitTime = 0;
  private Integer Turnaround=0;
  private Integer responseTime=1000000;
  //uses current to keep in track with the progress of instructions

  public void setOngoingCPU(Integer cpuNum) {
    this.ongoingCPU.set(0,cpuNum);
  }

  public void incWaitTime(){waitTime++;}

  public Process(String aProcessID, int arrivalTime, int totalExecTime,
                 List<Integer> aIORequestAtInstruction) {
    this.aProcessID = aProcessID;
    this.arrivalTime = arrivalTime;
    this.totalExecTime = totalExecTime;
    this.IO_RequestAtTimes = aIORequestAtInstruction;
    this.pcb = new PCB(aProcessID, CurrentTime, totalExecTime);
    this.pcb.setState(PCBSTATE.NEW);
    ongoingCPU = new ArrayList<>();
    ongoingCPU.add(-1);
  }

  public PCB getPcb() {
    return pcb;
  }



  // calculate execution time left, -1 b/c start from 0
  public Integer timeLeft(){
    return (getTotalExecTime() - getCurrentTime());
  }

  public void execute(){
    CurrentTime++;
  }


  @Override
  public String toString() {
    return "Process{" +
        "aProcessID='" + aProcessID + '\'' +
        ", arrivalTime=" + arrivalTime +
        ", totalExecTime=" + totalExecTime +
        ", IO_RequestAtTimes=" + IO_RequestAtTimes +
        ", pcb=" + pcb +
        ", TimeLeft=" +  timeLeft() +
        ", ongoingCPU=" + ongoingCPU.get(0) +
        '}';
  }

  //getters:
  public String getaProcessID() {
    return aProcessID;
  }

  public Integer getWaitTime(){return this.waitTime;}

  public int getArrivalTime() {
    return arrivalTime;
  }

  public int getTotalExecTime() {
    return totalExecTime;
  }

  public List<Integer> getIO_RequestAtTimes() {
    return IO_RequestAtTimes;
  }

  public int getCurrentTime() {
    return CurrentTime;
  }

  public Integer getTurnaround() {
    return Turnaround;
  }
  //+1 b/c start from time 0
  public void setTurnaround(Integer turnaround) {
    Turnaround = turnaround + 1;
  }

  public void setResponseTime(Integer responseTime) {
    this.responseTime = responseTime;
  }

  public Integer getResponseTime() {
    return responseTime;
  }
}
