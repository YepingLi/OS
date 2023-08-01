package model;

public class PCB {
  // set to new by default
  PCBSTATE state = PCBSTATE.NEW;
  Integer processNumber;
  Integer numInsExe;
  Integer totalInsNum;

  public void setState(PCBSTATE state) {
    this.state = state;
  }

  public PCB(Integer processNumber, Integer numInsExe, Integer totalInsNum) {
    this.processNumber = processNumber;
    this.numInsExe = numInsExe;
    this.totalInsNum = totalInsNum;
  }

  public void setNumInsExe(Integer numInsExe) {
    this.numInsExe = numInsExe;
  }

  public PCBSTATE getState() {
    return state;
  }

  @Override
  public String toString() {
  return "PCB{" +
        "state=" + state +
        ", processNumber=" + processNumber +
        ", the Program counter is " + numInsExe + '/' + totalInsNum +
        '}';}
}
