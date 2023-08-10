package model.Algorithm;

import model.Scheduler;

public class RR implements Algorithm {
  Scheduler scheduler;

  private RR() {
  }

  public static RR RRGenerator(){return new RR();}

  @Override
  public void algorithmApply() {
    scheduler.getReadyQueue().setAlgorithm(algorithmType.RR);
  }

  @Override
  public void algorithmPrep(Scheduler scheduler) {
    this.scheduler = scheduler;
  }
}
