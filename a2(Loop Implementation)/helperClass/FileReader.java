package helperClass;

import model.Secondary.Process;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.*;
public class FileReader {
  private String fileName;
  private int numberOfCPU = 0;
  private int q = 0;
  private List<Process> processArrayList = new ArrayList<>();

  public FileReader(String fileName) {
    this.fileName = fileName;
  }

  /**
   * return a list of process in the textfile
   * @return List<Process>
   * @throws FileNotFoundException e
   */
  public void processGenerator() throws FileNotFoundException {
    File processesInfo = new File(fileName);
    Scanner fileReader = new Scanner(processesInfo);
    int counter = 0; //used to skip the 1st line
    while (fileReader.hasNextLine()){
      String aProcessID = "";
      int  arrivalTime = 0;
      int  totalExecTime = 0;

      if (counter == 0){
        String theHeader = fileReader.nextLine();
        // Handling numOfCPUs=4
        String accumulateStr = "";
        //find the position of the number of CPU
        int insideCounter = 0;

        for(int i = 0; i < theHeader.length(); i++){
          Character currentChar = theHeader.charAt(i);
          if (currentChar.equals('=')){insideCounter = i + 1;}
        }

        for (int i = insideCounter; i< theHeader.length(); i ++){
          Character currentChar = theHeader.charAt(i);
          accumulateStr = accumulateStr + currentChar;}
        this.numberOfCPU  = Integer.valueOf(accumulateStr);
        counter++;
      }
      else if(counter == 1){
        String lineTwo = fileReader.nextLine();
        // Handling numOfCPUs=4
        String accumulateStr = "";
        //find the position of the number of CPU
        int insideCounter = 0;

        for(int i = 0; i < lineTwo.length(); i++){
          Character currentChar = lineTwo.charAt(i);
          if (currentChar.equals('=')){insideCounter = i + 1;}
        }

        for (int i = insideCounter; i< lineTwo.length(); i ++){
          Character currentChar = lineTwo.charAt(i);
          accumulateStr = accumulateStr + currentChar;}
        this.q  = Integer.valueOf(accumulateStr);
        counter++;
      }
      else if (counter == 2){
        String thirdLine = fileReader.nextLine();
        counter++;
      }
      else if (counter == 3){
        String fourthLine = fileReader.nextLine();
        counter++;
      }
      else {
        String theProcess = fileReader.nextLine();
        //System.out.println(theProcess);
        Integer placeCounter = 0;
        List<Integer> IO_RequestAtTimes = new ArrayList<>();

        //handle process ID
        String accumulateStr = "";
        for(int i = 0; i < theProcess.length(); i++){

          Character currentChar = theProcess.charAt(i);
          //System.out.println(currentChar);
          accumulateStr = accumulateStr + currentChar;
          Character nextChar = theProcess.charAt(i+1);
          if (nextChar.equals(' ')){
            //System.out.println(aProcessID);
            aProcessID = accumulateStr;
            placeCounter = i+2; // the character after space
            break;
          }
        }
        accumulateStr = "";
        // handle aNumberOfInstructions
        for(int i = placeCounter; i < theProcess.length(); i++){
          Character currentChar = theProcess.charAt(i);
          Character nextChar = theProcess.charAt(i+1);
          accumulateStr = accumulateStr + currentChar;
          if ((nextChar).equals(' ')){
            arrivalTime = Integer.valueOf(accumulateStr);
            placeCounter = i + 2;
            break;
          }
        }

        // handle totalExecTime
        accumulateStr = "";
        // handle aNumberOfInstructions
        for(int i = placeCounter; i < theProcess.length(); i++){
          Character currentChar = theProcess.charAt(i);
          Character nextChar = theProcess.charAt(i+1);
          accumulateStr = accumulateStr + currentChar;
          if ((nextChar).equals(' ')){
            totalExecTime = Integer.valueOf(accumulateStr);
            placeCounter = i + 2;
            break;
          }
        }


        // handle IO_RequestAtTimes
        placeCounter = getPlaceCounter(theProcess, placeCounter, IO_RequestAtTimes);
        processArrayList.add(new Process(aProcessID, arrivalTime, totalExecTime, IO_RequestAtTimes));
      }
    }

  }

  /**
   * Helper method used to extract the last two list
   * @param theProcess
   * @param placeCounter
   * @param aIORequestAtInstruction
   * @return
   */
  private int getPlaceCounter(String theProcess, int placeCounter,
                              List<Integer> aIORequestAtInstruction) {
    String accumulateStr = "";
    for(int i = placeCounter; i < theProcess.length(); i++){
      Character currentChar = theProcess.charAt(i);
      if (currentChar.equals(' ') || currentChar.equals('[')){
        accumulateStr = "";
        continue;}
      else if(currentChar.equals(',')){
        aIORequestAtInstruction.add(Integer.valueOf(accumulateStr));
        accumulateStr = "";
        continue;
      }
      else if ((currentChar).equals(']')){

        if (accumulateStr.length() == 0){break;} // handle empty []

        aIORequestAtInstruction.add(Integer.valueOf(accumulateStr));
        placeCounter = i + 2;
        break;
      }
      else
        {accumulateStr = accumulateStr + currentChar;}
    }
    return placeCounter;
  }


  @Override
  public String toString() {
    return "FileReader{" +
        "fileName='" + fileName + '\'' +
        ", numberOfCPU=" + numberOfCPU +
        ", q=" + q +
        ", processArrayList=" + "\n" +processArrayList +
        '}';
  }

  public int getNumberOfCPU() {
    return numberOfCPU;
  }

  public int getQ() {
    return q;
  }

  public List<Process> getProcessArrayList() {
    return processArrayList;
  }
}
