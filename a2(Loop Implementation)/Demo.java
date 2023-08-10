import helperClass.FileReader;
import java.io.FileNotFoundException;
import model.Algorithm.algorithmType;
import model.Scheduler;


public class Demo {

  public static void main(String[] args) throws FileNotFoundException, InterruptedException {
    FileReader theFileReader = new FileReader("processInfo.txt");
    theFileReader.processGenerator();
    //System.out.println(theFileReader.getProcessArrayList());

    System.out.println("The algorithm right now is SJF");
    //Create the os
    Scheduler scheduler1 = Scheduler.osGenerator(theFileReader.getQ(), theFileReader.getNumberOfCPU(), theFileReader.getProcessArrayList(),
        algorithmType.SJF);

    //OS execution
    scheduler1.execute();

    System.out.println("The algorithm right now is RR");
    //Create the os
    FileReader theFileReader1 = new FileReader("processInfo.txt");
    theFileReader1.processGenerator();
    Scheduler scheduler2 = Scheduler.osGenerator(theFileReader.getQ(), theFileReader.getNumberOfCPU(), theFileReader1.getProcessArrayList(),
        algorithmType.RR);

    //OS execution
    scheduler2.execute();


    System.out.println("The algorithm right now is FCFS");
    //Create the os
    FileReader theFileReader2 = new FileReader("processInfo.txt");
    theFileReader2.processGenerator();
    Scheduler scheduler3 = Scheduler.osGenerator(theFileReader2.getQ(), theFileReader2.getNumberOfCPU(), theFileReader2.getProcessArrayList(),
        algorithmType.FCFS);

    //OS execution
    scheduler3.execute();

  }
}
