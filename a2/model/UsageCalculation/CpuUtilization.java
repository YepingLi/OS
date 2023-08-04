package model.UsageCalculation;


import java.util.HashMap;
import java.util.List;
import model.CPU;

public class CpuUtilization {
  private CpuUtilization() {
  }
  public static CpuUtilization CpuUtilizationGenerator(){return new CpuUtilization();}

  private HashMap<Integer, Float > cpuUtilization(List<CPU> cpuList, List<Integer>totalExeTime){
    HashMap<Integer, Float> returnHash = new HashMap<>();

    for (CPU cpu : cpuList){
      returnHash.put(cpu.getIndex(), ((float)cpu.getCPUTimeCounter()/(float)totalExeTime.get(0))*100);
    }
    return returnHash;
  }

  public void displayCpuUtilization(List<CPU> cpuList, List<Integer>totalExeTime){
    HashMap<Integer,Float> cpuUtilizationHashMap =  cpuUtilization(cpuList, totalExeTime);
    for(Integer key : cpuUtilizationHashMap.keySet()){
      System.out.println("The CPU"+key+"'s CPU utilization is " + cpuUtilizationHashMap.get(key) + "%");
    }
  }
}
