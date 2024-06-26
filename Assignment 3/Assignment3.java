import java.io.IOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;

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
    private Queue<Integer> queue = new LinkedList<Integer>();
    private ArrayList<Integer> CPUSchedule = new ArrayList<Integer>();
    private ArrayList<Integer> processSchedule = new ArrayList<Integer>();
    private int[][] waitTimes;

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

            if (fistLineStrings.length > 1)
                timeQuantum = Integer.parseInt(fistLineStrings[1]);

            numberOfProcesses = Integer.parseInt(myReader.nextLine());

            processInfo = new int[numberOfProcesses][4]; //[i][0] = ProcessNumber, [i][1] = Arrivaltime, [i][2] = CPUBursttime, [i][3] = Priority
            waitTimes = new int[numberOfProcesses][2]; //i = processID, [i][0] = CPUBurstMAX, [i][1] = waitTime

            for(int i = 0; i < numberOfProcesses; i++){
                String[] processStrings = myReader.nextLine().split(" ");
                processInfo[i][0] = Integer.parseInt(processStrings[0]);
                processInfo[i][1] = Integer.parseInt(processStrings[1]);
                processInfo[i][2] = Integer.parseInt(processStrings[2]);
                processInfo[i][3] = Integer.parseInt(processStrings[3]);

                waitTimes[i][0] = Integer.parseInt(processStrings[2]);
                waitTimes[i][1] = 0;
            }

            myReader.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found. Did you update the rootFilePath variable on line 24?");
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
                outputFile.write(CPUSchedule.get(i) + "    " + processSchedule.get(i));
                outputFile.newLine();
            }

            outputFile.write("AVG Waiting Time: " + String.valueOf(GetAverageWaittime()));

            outputFile.close();
        }catch (IOException e ){
            System.out.println("File not found. Did you update the rootFilePath variable on line 24?");
            e.printStackTrace();
        }
    }

    public void RR(){

        int processDoneCount = 0;
        int time = 0;
        int currentTimeQuantum = 0;
        int processUsingCPU = -1;

        CheckForArrivalsRR(time);
        if(!queue.isEmpty()){
            processUsingCPU = queue.poll();
            UpdateOutputData(time, processUsingCPU);
        }
        
        while (processDoneCount < numberOfProcesses) {
            
            if(processUsingCPU >= 0){
                //System.out.println("time: " + time + ", process: " + processUsingCPU + ", time remaining: " + processInfo[processUsingCPU][2]);
                if(currentTimeQuantum >= timeQuantum){
                    currentTimeQuantum = 0;
    
                    if(processInfo[processUsingCPU][2] > 0){
                        queue.offer(processUsingCPU); 
                    }
                    else{
                        processDoneCount++;
                        LogWaitTime(time, processUsingCPU);
                    }

                    if(!queue.isEmpty()){  
                        processUsingCPU = queue.poll();
                        UpdateOutputData(time, processUsingCPU);                       
                    }
                }
                
                if (processInfo[processUsingCPU][2] <= 0){
                    
                    processDoneCount++;
                    LogWaitTime(time, processUsingCPU);
                    
                    if(!queue.isEmpty()){                        
                        processUsingCPU = queue.poll();
                        currentTimeQuantum = 0;
                        UpdateOutputData(time, processUsingCPU);
                    }
                }
                processInfo[processUsingCPU][2]--;
                //System.out.println("time: " + time + ", process: " + processUsingCPU + ", time remaining: " + processInfo[processUsingCPU][2]);
            }  
            time++;
            currentTimeQuantum++;
            CheckForArrivalsRR(time);                                                  
        }
        
        WriteOutputFile();
    }

    public void SJF(){
        
        int processDoneCount = 0;
        int time = 0;
        int processUsingCPU = -1;
        ArrayList<Integer> processDoneList = new ArrayList<Integer>();

        ArrayList<Integer> arrivalList = new ArrayList<Integer>();
        PriorityQueue<Integer> burstQueue = new PriorityQueue<Integer>();
        //populate burstQueue
        for (int i = 0; i < numberOfProcesses; i++){
            burstQueue.offer(processInfo[i][2]);
        }

        Queue<Integer> queueSJF = new LinkedList<Integer>();
        UpdateQueueSJF(time, queueSJF, arrivalList, burstQueue, processDoneList);
        
        while (processDoneCount < numberOfProcesses) {

            if(processUsingCPU < 0 && !queueSJF.isEmpty()){
                processUsingCPU = queueSJF.poll();
                processDoneList.add(processUsingCPU);
                UpdateOutputData(time, processUsingCPU);
            }
            if(processUsingCPU >= 0){
                
                if (processInfo[processUsingCPU][2] <= 0){
                    
                    processDoneCount++;
                    
                    LogWaitTime(time, processUsingCPU);
                    processUsingCPU = -1;

                    if(!queueSJF.isEmpty()){   
                                            
                        processUsingCPU = queueSJF.poll();
                        processDoneList.add(processUsingCPU);
                        UpdateOutputData(time, processUsingCPU);
                    }
                }
                if (processUsingCPU >= 0){
                    processInfo[processUsingCPU][2]--;
                    //System.out.println("time: " + time + ", process: " + processUsingCPU + ", time remaining: " + processInfo[processUsingCPU][2]);
                }    
            } 
            time++;   
            UpdateQueueSJF(time, queueSJF, arrivalList, burstQueue, processDoneList);                                               
        }
        
        WriteOutputFile();
    }

    public void PR_noPREMP(){
        
        int processDoneCount = 0;
        int time = 0;
        int processUsingCPU = -1;
        ArrayList<Integer> processDoneList = new ArrayList<Integer>();

        ArrayList<Integer> arrivalList = new ArrayList<Integer>();
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>();
        //populate priorityQueue
        for (int i = 0; i < numberOfProcesses; i++){
            priorityQueue.offer(processInfo[i][3]);
        }

        Queue<Integer> queuePR_noPREMP = new LinkedList<Integer>();
        UpdateQueuePR_noPREMP(time, queuePR_noPREMP, arrivalList, priorityQueue, processDoneList);
        
        while (processDoneCount < numberOfProcesses) {

            if(processUsingCPU < 0 && !queuePR_noPREMP.isEmpty()){
                //System.out.println(queuePR_noPREMP.peek());
                processUsingCPU = queuePR_noPREMP.poll();
                processDoneList.add(processUsingCPU);
                UpdateOutputData(time, processUsingCPU);
            }
            if(processUsingCPU >= 0){
                
                if (processInfo[processUsingCPU][2] <= 0){
                    
                    processDoneCount++;
                    
                    LogWaitTime(time, processUsingCPU);
                    processUsingCPU = -1;

                    if(!queuePR_noPREMP.isEmpty()){   
                                            
                        processUsingCPU = queuePR_noPREMP.poll();
                        processDoneList.add(processUsingCPU);
                        UpdateOutputData(time, processUsingCPU);
                    }
                }
                if (processUsingCPU >= 0){
                    processInfo[processUsingCPU][2]--;
                    System.out.println("time: " + time + ", process: " + processUsingCPU + ", time remaining: " + processInfo[processUsingCPU][2]);
                }    
            } 
            time++;   
            UpdateQueuePR_noPREMP(time, queuePR_noPREMP, arrivalList, priorityQueue, processDoneList);                                               
        }
        
        WriteOutputFile();
    }

    public void PR_withPREMP(){
        
        int processDoneCount = 0;
        int time = 0;
        int processUsingCPU = -1;
        ArrayList<Integer> processDoneList = new ArrayList<Integer>();

        ArrayList<Integer> arrivalList = new ArrayList<Integer>();
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>();

        //populate priorityQueue
        for (int i = 0; i < numberOfProcesses; i++){
            priorityQueue.offer(processInfo[i][3]);
        }

        Queue<Integer> queuePR_withPREMP = new LinkedList<Integer>();
        UpdateQueuePR_withPREMP(time, queuePR_withPREMP, arrivalList, priorityQueue, processDoneList);
        
        while (processDoneCount < numberOfProcesses) {

            if(processUsingCPU < 0 && !queuePR_withPREMP.isEmpty()){
                processUsingCPU = queuePR_withPREMP.poll();
                processDoneList.add(processUsingCPU);
                UpdateOutputData(time, processUsingCPU);
            }
            if(processUsingCPU >= 0){
                
                if (processInfo[processUsingCPU][2] <= 0){

                    processDoneCount++;
                    
                    LogWaitTime(time, processUsingCPU);
                    processUsingCPU = -1;

                    if(!queuePR_withPREMP.isEmpty()){                       
                        processUsingCPU = queuePR_withPREMP.poll();
                        processDoneList.add(processUsingCPU);
                        UpdateOutputData(time, processUsingCPU);
                        System.out.println(processUsingCPU);
                    }
                }
                else if (!queuePR_withPREMP.isEmpty() && processInfo[processUsingCPU][3] > processInfo[queuePR_withPREMP.peek()][3])
                {
                    System.out.println("Test2");
                    processDoneList.remove(processDoneList.indexOf(processUsingCPU));

                    processUsingCPU = queuePR_withPREMP.poll();
                    processDoneList.add(processUsingCPU);
                    UpdateOutputData(time, processUsingCPU);
                }
                if (processUsingCPU >= 0){
                    processInfo[processUsingCPU][2]--;
                    System.out.println("time: " + time + ", process: " + processUsingCPU + ", time remaining: " + processInfo[processUsingCPU][2]);
                }    
            } 
            time++;   
            UpdateQueuePR_withPREMP(time, queuePR_withPREMP, arrivalList, priorityQueue, processDoneList);                                               
        }
        
        WriteOutputFile();
    }

    //Helper methods
    public float GetAverageWaittime(){

        float average = 0;

        for (int i = 0; i < waitTimes.length; i++){
            average += waitTimes[i][1];
        }

        average /= waitTimes.length;
        return average;
    }

    public void LogWaitTime(int finishTime, int processID){
        waitTimes[processID][1] = finishTime - processInfo[processID][1] - waitTimes[processID][0];
    }

    public void UpdateOutputData(int time, int processUsingCPU){
        CPUSchedule.add(time);
        processSchedule.add(processInfo[processUsingCPU][0]);
    }

    public void CheckForArrivalsRR(int time){
        
        for (int i = 0; i < numberOfProcesses; i++){

            if(processInfo[i][1] == time && !queue.contains(i)){
                queue.offer(i);
            }
        }
    }

    public void UpdateQueueSJF(int time, Queue<Integer> queueSJF, ArrayList<Integer> arrivalList, PriorityQueue<Integer> burstQueue, ArrayList<Integer> processDoneList){
        queueSJF.clear();
        burstQueue.clear();

        for (int i = 0; i < numberOfProcesses; i++){
            if(processInfo[i][1] <= time && !processDoneList.contains(i)){
                if(!arrivalList.contains(i))
                    arrivalList.add(i);
                burstQueue.offer(processInfo[i][2]);
            }
        }

        for ( int i = 0; i < arrivalList.size(); i++){
            
            if(burstQueue.isEmpty() || burstQueue.peek() == null){
                break;
            } 
            if(processInfo[arrivalList.get(i)][2] == burstQueue.peek()){
                //System.out.println("arrival: " + arrivalList.get(i) + ", burst: " + burstQueue.peek());
                queueSJF.offer(arrivalList.get(i));
                burstQueue.poll();
            }
        }
        
    }

    public void UpdateQueuePR_noPREMP(int time, Queue<Integer> queuePR_noPREMP, ArrayList<Integer> arrivalList, PriorityQueue<Integer> priorityQueue, ArrayList<Integer> processDoneList){
        queuePR_noPREMP.clear();
        priorityQueue.clear();

        for (int i = 0; i < numberOfProcesses; i++){
            if(processInfo[i][1] <= time && !processDoneList.contains(i)){
                if(!arrivalList.contains(i)){
                    arrivalList.add(i);
                    //System.out.println(" processID: " + processInfo[arrivalList.get(0)][0]);
                } 
                priorityQueue.offer(processInfo[i][3]);
            }
        }

        for ( int i = 0; i < arrivalList.size(); i++){
            
            if(priorityQueue.isEmpty() || priorityQueue.peek() == null){
                break;
            } 
            if(processInfo[arrivalList.get(i)][3] == priorityQueue.peek()){
                //System.out.println("arrival: " + processInfo[arrivalList.get(i)][0] + ", priority: " + priorityQueue.peek() + ", i: " + i);
                queuePR_noPREMP.offer(arrivalList.get(i));
                priorityQueue.poll();
            }
        }
        
    }

    public void UpdateQueuePR_withPREMP(int time, Queue<Integer> queuePR_withPREMP, ArrayList<Integer> arrivalList, PriorityQueue<Integer> priorityQueue, ArrayList<Integer> processDoneList){
        queuePR_withPREMP.clear();
        priorityQueue.clear();

        for (int i = 0; i < numberOfProcesses; i++){
            if(processInfo[i][1] <= time && !processDoneList.contains(i)){
                if(!arrivalList.contains(i)){
                    arrivalList.add(i);
                    System.out.println(" processID: " + processInfo[arrivalList.get(0)][0]);
                } 
                priorityQueue.offer(processInfo[i][3]);
            }
        }

        for ( int i = 0; i < arrivalList.size(); i++){
            
            if(priorityQueue.isEmpty() || priorityQueue.peek() == null){
                break;
            } 
            if(processInfo[arrivalList.get(i)][3] == priorityQueue.peek() && !processDoneList.contains(arrivalList.get(i))){
                System.out.println("arrival: " + processInfo[arrivalList.get(i)][0] + ", priority: " + priorityQueue.peek() + ", i: " + i);
                queuePR_withPREMP.offer(arrivalList.get(i));
                priorityQueue.poll();
            }
        }
        
    }
}