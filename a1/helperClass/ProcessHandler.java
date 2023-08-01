package helperClass;

import model.CPU;


public class ProcessHandler {
  CPU cpu;

  private ProcessHandler(CPU cpu) {
    this.cpu = cpu;
  }

  /**
   * Generate a process handler
   * @param cpu
   * @return
   */
  public static ProcessHandler setCpu(CPU cpu) {
    return new ProcessHandler(cpu);
  }

  /**
   * start to process the processes
   */
  public void execute() throws InterruptedException {
    cpu.execute();
  }
}
