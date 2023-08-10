package model.Algorithm;

import model.Scheduler;

public class SJF implements Algorithm{
  private Scheduler scheduler;
  private SJF() {
  }

  public static SJF SJFGenerator(){return new SJF();}

  @Override
  public void algorithmApply() {
    scheduler.setQApplied(1000000);
    scheduler.getReadyQueue().setAlgorithm(algorithmType.SJF);
  }

  @Override
  public void algorithmPrep(Scheduler scheduler) {
    this.scheduler = scheduler;
  }
}
