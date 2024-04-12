import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class Assignment3{

    public static void main(String[] args){
        SchedulerSimulation schedulerSimulation = new SchedulerSimulation();
    }
}

class SchedulerSimulation{
    //Text file paths
    private String rootFilePath = "G:\\Repos_New\\CSC-139\\Assignment 3"; //Update to the root file path for your input and output files
    private String inputFilePath = rootFilePath + "\\input.txt";
    private String outputFilePath = rootFilePath + "\\output.txt";

    //input variables
    private String roundRobinName = "RR";
    private String shortestJobFirstName = "SJF";
    private String priorityNoPreemptionName = "PR_noPREMP";
    private String priorityWithPreemptionName = "PR_withPREMP";

    private String selectedAlgorithmName;
    private int timeQuantum;
    private int numberOfProcesses;
    private int[][] processInfo;

    //output variables
    private Queue<Integer> processQueue = new LinkedList<Integer>();
    private ArrayList<Integer> CPUSchedule = new ArrayList<Integer>();
    private ArrayList<Integer> processSchedule = new ArrayList<Integer>();
    private ArrayList<Integer> waittimes = new ArrayList<Integer>();

    //RR variables
    //Stack<Integer> processStack = new Stack<Integer>();
    private int lowestProcess;
    private ArrayList<Integer> alreadyQueued = new ArrayList<Integer>();

    public static void main(String[] args){
        
    }

    public SchedulerSimulation(){
        ReadInputFile();
    }

    public void ReadInputFile(){ 

        try{
            File inputFile = new File(inputFilePath);
            Scanner myReader = new Scanner(inputFile);
            
            String[] fistLineStrings = myReader.nextLine().split(" ");

            selectedAlgorithmName = fistLineStrings[0];

            if (fistLineStrings[1] != null)
                timeQuantum = Integer.parseInt(fistLineStrings[1]);

            numberOfProcesses = Integer.parseInt(myReader.nextLine());

            processInfo = new int[numberOfProcesses][4]; //[i][0] = ProcessNumber, [i][1] = Arrivaltime, [i][2] = CPUBursttime, [i][3] = Priority

            for(int i = 0; i < numberOfProcesses; i++){
                String[] processStrings = myReader.nextLine().split(" ");
                processInfo[i][0] = Integer.parseInt(processStrings[0]);
                processInfo[i][1] = Integer.parseInt(processStrings[1]);
                processInfo[i][2] = Integer.parseInt(processStrings[2]);
                processInfo[i][3] = Integer.parseInt(processStrings[3]);
            }

            myReader.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found. Did you update the rootFilePath variable?");
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
            if(timeQuantum > 0 ){
                String timeQuantumWithSpace = " " + timeQuantum;
                outputFile.write(timeQuantumWithSpace);
            }   
            outputFile.newLine();


            for(int i = 0; i < CPUSchedule.size(); i++){

                outputFile.write(CPUSchedule.get(i) + " ");
                outputFile.write(processSchedule.get(i));
                outputFile.newLine();
            }

            outputFile.write(String.valueOf(GetAverageWaittime()));

            outputFile.close();
        }catch (IOException e ){
            System.out.println("File not found. Did you update the rootFilePath variable?");
            e.printStackTrace();
        }
    }
    /*
    public void BuildArrivalQueue(){
        
        while (processStack.size() < numberOfProcesses){
            for(int i = 0; i < numberOfProcesses; i++){
                if (alreadyQueued.contains(lowestProcess)){
                    if ((lowestProcess + 1) >= numberOfProcesses)
                        lowestProcess = 0;
                    else
                        lowestProcess++;
                }

                if (alreadyQueued.contains(i))
                    continue;
                else if( processInfo[lowestProcess][1] < processInfo[i][1]){
                    lowestProcess = i;
                }
                else if( processInfo[lowestProcess][1] == processInfo[i][1]
                &&  processInfo[lowestProcess][0] < processInfo[i][0]){
                    lowestProcess = i;
                }
            }

            alreadyQueued.add(lowestProcess);
            processStack.push(lowestProcess);
        }
    }*/

    public void RR(){
        /*
        lowestProcess = 0;
        BuildArrivalQueue();
        
        while (!processStack.empty()) {
            processQueue.add(processStack.pop());
        }*/

        int processDoneCount = 0;
        int time = 0;
        int currentTimeQuantum = 0;
        int processUsingCPU = -1;

        while (processDoneCount < numberOfProcesses) {

            if(currentTimeQuantum >= timeQuantum 
            || processInfo[processUsingCPU][2] <= 0){
                currentTimeQuantum = 0;

                if(processInfo[processUsingCPU][2] > 0){
                    processQueue.add(processInfo[processUsingCPU][0]);
                    processDoneCount++;
                }
                    
                    for (int i = 0; i < numberOfProcesses; i++){
                        if(processInfo[processQueue.peek()][1] <= time)
                            processUsingCPU = processQueue.poll();
                    }
            }

            if (processUsingCPU > 0)
                processInfo[processUsingCPU][2]--;
            
            time++;
            currentTimeQuantum++;
        }
        
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

    public float GetAverageWaittime(){

        float average = 0;

        for (int i = 0; i < waittimes.size(); i++){
            average += waittimes.get(i);
        }

        average /= waittimes.size();
        return average;
    }
}