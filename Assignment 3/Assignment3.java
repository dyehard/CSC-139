import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Assignment3{

    public static void main(String[] args){
        SchedulerSimulation schedulerSimulation = new SchedulerSimulation();
        schedulerSimulation.RunSimulation();
    }
}

class SchedulerSimulation{
    //Text file paths
    private String rootFilePath = "G:\\Repos_New\\CSC-139\\Assignment 3"; //Update to the root file path for your input and output files
    private String inputFilePath = rootFilePath + "\\input.txt";
    private String outputFilePath = rootFilePath + "\\output.txt";

    //Mass testing
    /*
    private String rootFilePath = "G:\Repos_New\CSC-139\Assignment 3"; //Update to the root file path for your input and output files
    private String inputFilePath = rootFilePath + "\input" + i + ".txt";
    private String outputFilePath = rootFilePath + "\output" + i + ".txt";
    */

    //input output variables
    private String roundRobinName = "RR";
    private String shortestJobFirstName = "SJF";
    private String priorityNoPreemptionName = "PR_noPREMP";
    private String priorityWithPreemptionName = "PR_withPREMP";

    private String selectedAlgorithmName;
    private int numberOfProcesses;
    private float averageWaitingTime = 0;

    public static void main(String[] args){
        
    }

    public SchedulerSimulation(){
        
    }

    public void RunSimulation(){
        ReadInputFile();
    }

    public void ReadInputFile(){ 

        try{
            File inputFile = new File(inputFilePath);
            Scanner myReader = new Scanner(inputFile);
            
            selectedAlgorithmName = myReader.nextLine();
            numberOfProcesses = Integer.parseInt(myReader.nextLine());

            int[][] processInfo = new int[numberOfProcesses][4]; //[i][0] = ProcessNumber, [i][1] = ArrivalTime, [i][2] = CPUBurstTime, [i][3] = Priority

            for(int i = 0; i < numberOfProcesses; i++){
                String[] processStrings = myReader.nextLine().split(" ");
                processInfo[i][0] = Integer.parseInt(processStrings[0]);
                processInfo[i][1] = Integer.parseInt(processStrings[1]);
                processInfo[i][2] = Integer.parseInt(processStrings[2]);
                processInfo[i][3] = Integer.parseInt(processStrings[3]);
            }

            myReader.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found!");
            e.printStackTrace();
        }

        if (selectedAlgorithmName.equals(roundRobinName))
            RR();
        else if (selectedAlgorithmName.equals(shortestJobFirstName))
            SJF();   
        else if (selectedAlgorithmName.equals(priorityNoPreemptionName))
            PR_noPREMP();
        else if (selectedAlgorithmName.equals(priorityWithPreemptionName))
            PR_withPREMP();
    }

    public void WriteOutputFile(){

        try{
            BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputFilePath));
            
            outputFile.write(selectedAlgorithmName);
            outputFile.newLine();

            outputFile.write(String.valueOf(averageWaitingTime));

            outputFile.close();
        }catch (IOException e ){
            System.out.println("File not found!");
            e.printStackTrace();
        }
    }

    public void RR(){

        WriteOutputFile();
    }

    public void SJF(){
        
        WriteOutputFile();
    }

    public void PR_noPREMP(){
        
        WriteOutputFile();
    }

    public void PR_withPREMP(){
        
        WriteOutputFile();
    }
}