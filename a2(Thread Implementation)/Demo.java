import helperClass.FileReader;
import java.io.FileNotFoundException;
import model.OS;


public class Demo {

  public static void main(String[] args) throws FileNotFoundException, InterruptedException {
    FileReader theFileReader = new FileReader("processInfo.txt");
    theFileReader.processGenerator();
    //System.out.println(theFileReader.getProcessArrayList());

    //Create the os
    OS os = OS.osGenerator(theFileReader.getQ(), theFileReader.getNumberOfCPU(), theFileReader.getProcessArrayList());

    //OS execution
    os.execute();



























//    //Assign the initial ready queue and wait queue
//    ReadyQueue readyQueue = new ReadyQueue(processList);
//    WaitQueue waitQueue1 = new WaitQueue();
//    WaitQueue waitQueue2 = new WaitQueue();
//
//    // create the CPU
//    CPU cpu = CPU.CpuGenerator(readyQueue);
//
//    //To have reference to all processes from the txt file.
//    cpu.setProcessList(processList);
//
//    //create the 2 IOs
//    IO io1 = IO.IOGenrator(waitQueue1, "io1", readyQueue);
//    IO io2 = IO.IOGenrator(waitQueue2, "io2", readyQueue);
//
//    // add ios to cpu
//    cpu.addIO(io1);
//    cpu.addIO(io2);
//
//    //create the process handler
//    ProcessHandler processhandler = ProcessHandler.setCpu(cpu);
//
//    // execute the process handler
//    processhandler.execute();
  }
}
