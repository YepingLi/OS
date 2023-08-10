package model.Secondary;

public class PCB {
  // set to new by default
  PCBSTATE state = PCBSTATE.NEW;
  String processNumber;


  public PCB(String processNumber, Integer numInsExe, Integer totalInsNum) {
    this.processNumber = processNumber;

  }
  public void setState(PCBSTATE state) {
    this.state = state;
  }

  public PCBSTATE getState() {
    return state;
  }

  @Override
  public String toString() {
    return "PCB{" +
        "state=" + state +
        ", processNumber='" + processNumber + '\'' +
        '}';
  }
}
