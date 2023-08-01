package helperClass;

import model.Process;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.*;
public class FileReader {
  String fileName;

  public FileReader(String fileName) {
    this.fileName = fileName;
  }



  /**
   * return a list of process in the textfile
   * @return List<Process>
   * @throws FileNotFoundException e
   */
  public List<Process> processGenerator() throws FileNotFoundException {
    File processesInfo = new File(fileName);
    Scanner fileReader = new Scanner(processesInfo);
    List<Process> returnList = new ArrayList<>();
    int counter = 0; //used to skip the 1st line
    while (fileReader.hasNextLine()){
      if (counter == 0){
        String theHeader = fileReader.nextLine();
        counter++;
      }else {
        String theProcess = fileReader.nextLine();
        //System.out.println(theProcess);
        Integer placeCounter = 0;
        Integer aProcessID = 0;
        int  aNumberOfInstructions = 0;
        List<Integer> aIORequestAtInstruction = new ArrayList<>();
        List<Integer> aIODevicesRequested = new ArrayList<>();

        //handle process ID
        for(int i = 0; i < theProcess.length(); i++){
          String accumulateStr = "";
          Character currentChar = theProcess.charAt(i);
          //System.out.println(currentChar);
          Character nextChar = theProcess.charAt(i+1);
          if (currentChar.equals(' ')){continue;}
          accumulateStr = accumulateStr + currentChar;
          //System.out.println(accumulateStr);
          if ((nextChar).equals(',')){
            //System.out.println(aProcessID);
            aProcessID = Integer.valueOf(accumulateStr);
            placeCounter = i+2; // the character after comma
            break;
          }
        }
        String accumulateStr = "";
        // handle aNumberOfInstructions
        for(int i = placeCounter; i < theProcess.length(); i++){
          Character currentChar = theProcess.charAt(i);
          Character nextChar = theProcess.charAt(i+1);
          if (currentChar.equals(' ')){continue;}
          accumulateStr = accumulateStr + currentChar;
          if ((nextChar).equals(',')){
            aNumberOfInstructions = Integer.valueOf(accumulateStr);
            placeCounter = i + 2;
            break;
          }
        }

        // handle aIORequestAtInstruction
        placeCounter = getPlaceCounter(theProcess, placeCounter, aIORequestAtInstruction);
        // handle aIODevicesRequested
        placeCounter = getPlaceCounter(theProcess, placeCounter, aIODevicesRequested);
        returnList.add(new Process(aProcessID, aNumberOfInstructions, aIORequestAtInstruction, aIODevicesRequested));
      }
    }
    //Print all process for testing
    //for (Process process : returnList){System.out.println(process);}

    return returnList;
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

}
