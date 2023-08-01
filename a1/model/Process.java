package model;

import java.util.List;

public class Process {
  public int aProcessID;
  public int  aNumberOfInstructions;
  public List<Integer> aIORequestAtInstruction;
  public List<Integer> aIODevicesRequested;
  public PCB pcb;
  public int numInsExe = 0;

  //uses current to keep in track with the progress of instructions
  public int currentPos;

  /**
   * Constructor
   * @param aProcessID aProcessID
   * @param aNumberOfInstructions aNumberOfInstructions
   * @param aIORequestAtInstruction aIORequestAtInstruction
   * @param aIODevicesRequested aIODevicesRequested
   */
  public Process(int aProcessID, int aNumberOfInstructions,
                 List<Integer> aIORequestAtInstruction,
                 List<Integer> aIODevicesRequested) {
    this.aProcessID = aProcessID;
    this.aNumberOfInstructions = aNumberOfInstructions;
    this.aIORequestAtInstruction = aIORequestAtInstruction;
    this.aIODevicesRequested = aIODevicesRequested;
    this.currentPos = 0;
    this.pcb = new PCB(aProcessID, currentPos, aNumberOfInstructions);
    this.pcb.setState(PCBSTATE.NEW);

  }


  @Override
  public String toString() {
    return "Process{" +
        "aProcessID=" + aProcessID +
        ", aNumberOfInstructions=" + aNumberOfInstructions +
        ", aIORequestAtInstruction=" + aIORequestAtInstruction +
        ", aIODevicesRequested=" + aIODevicesRequested +
        '}';
  }

  public PCB getPcb() {
    return pcb;
  }

  public int getaProcessID() {
    return aProcessID;
  }

  public int getaNumberOfInstructions() {
    return aNumberOfInstructions;
  }


  public int getCurrentPos() {
    return currentPos;
  }

  public List<Integer> getaIORequestAtInstruction() {
    return aIORequestAtInstruction;
  }

  public List<Integer> getaIODevicesRequested() {
    return aIODevicesRequested;
  }

  /**
   * used to make sure that one process is execute 2 instructions at one time
   */
  public void execute(){
    this.pcb.setState(PCBSTATE.RUNNING);
    numInsExe++;
    currentPos++;
    this.pcb.setNumInsExe(currentPos);
  }

  public void numInsExeReset(){numInsExe = 0;}

  public int getNumInsExe() {
    return numInsExe;
  }
}
