package model.Algorithm;

import model.Scheduler;

public class FCFS implements Algorithm{
  private Scheduler scheduler;

  private FCFS() {
  }

  public static FCFS FCFSGenerator(){return new FCFS();}

  @Override
  public void algorithmPrep(Scheduler scheduler) {
    this.scheduler = scheduler;
  }
  @Override
  public void algorithmApply() {
    scheduler.setQApplied(1000000);
    scheduler.getReadyQueue().setAlgorithm(algorithmType.FCFS);
  }


}
