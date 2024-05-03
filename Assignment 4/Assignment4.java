//package Assignment 4;

import java.io.IOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.text.Style;

import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;

public class Assignment4{

    public static void main(String[] args){
        VirtualMemorySimulation VirtualMemorySimulation = new VirtualMemorySimulation();
    }
}

class VirtualMemorySimulation{
    //Text file paths
    private String rootFilePath = "C:\\Users\\dyeha\\Documents\\GitHub\\CSC-139\\Assignment 4"; //Update to the root file path for your input and output files
    private String inputFilePath = rootFilePath + "\\input.txt";
    private String outputFilePath = rootFilePath + "\\output.txt";

    //Test variables
    private int testNumber = 1;//update for test case
    private String testInputFilePath = rootFilePath + "\\test" + testNumber + ".txt";
    private String testOutputFilePath = rootFilePath + "\\test" + testNumber + "o.txt";

    private int pageCount = 0;
    private int[] requests;

    private int[] frames;

    private int pageFaultCount = 0;

    private ArrayList<String> log = new ArrayList<String>();

    private int fifoIndex = 0;

    public static void main(String[] args){
        
    }

    public VirtualMemorySimulation(){
        //ReadInputFile();

        for (int i = 1; i <= 5; i++){
            testNumber = i;
            ReadInputFile();
        }
    }

    public void ReadInputFile(){ 

        try{
            File inputFile = new File(testInputFilePath/*inputFilePath*/);
            Scanner myReader = new Scanner(inputFile);
            
            String[] fistLineStrings = myReader.nextLine().split("\\s+");
            
            pageCount = Integer.parseInt(fistLineStrings[0]);
            frames = new int[Integer.parseInt(fistLineStrings[1])];
            requests = new int[Integer.parseInt(fistLineStrings[2])];

            ResetVariables();

            for (int i = 0; i < requests.length; i++){
                String[] nextLine = myReader.nextLine().split("\\s+");
                requests[i] = Integer.parseInt(nextLine[0]);
            }
            
            myReader.close();
        
            FIFO();

            TestCode();
        }
        catch (FileNotFoundException e){
            System.out.println("File not found. Did you update the rootFilePath variable on line 24?");
            e.printStackTrace();
        }
    }

    public void WriteOutputFile(){

        try{
            BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputFilePath));

            for(int i = 0; i < log.size(); i++){
                outputFile.write(log.get(i));
                outputFile.newLine();
            }

            outputFile.close();
        }
        catch (IOException e ){
            System.out.println("File not found. Did you update the rootFilePath variable on line 24?");
            e.printStackTrace();
        }
    }

    public void ResetVariables(){
        for(int i = 0; i < frames.length; i++){
            frames[i] = -1;
        }

        pageFaultCount = 0;
        log = new ArrayList<String>();
    }

    public boolean CheckIfPageLoaded(int page ){
        for (int i = 0; i < frames.length; i++)
        {
            if(frames[i] == page){
                log.add("Page " + page + " already in Frame " + i);
                return true;
            }
        }
        pageFaultCount++;
        return false;
    }

    public boolean CheckIfFrameIsEmpty(int page){

        for (int i = 0; i < frames.length; i++)
        {
            if(frames[i] == -1 ){
                frames[i] = page;
                log.add("Page " + page + " loaded into Frame " + i);
                return true;
            }
        }

        return false;
    }

    public void FIFO(){

        ResetVariables();
        fifoIndex = 0;
        log.add("FIFO");

        for (int i = 0; i < requests.length; i++)
        { 
            if (!CheckIfPageLoaded(requests[i]) && !CheckIfFrameIsEmpty(requests[i])){
                
                log.add("Page " + frames[fifoIndex] + " unloaded from Frame " + fifoIndex 
                + ", Page " + requests[i] + " loaded into Frame " + fifoIndex);
                frames[fifoIndex] = requests[i];
                
                if (fifoIndex > frames.length)
                    fifoIndex = 0;
                fifoIndex++;
            }
        }

        log.add(pageFaultCount + " page faults");
        
        WriteOutputFile();
    }

    public void TestCode(){

        ArrayList<String> correctOutput = new ArrayList<String>();

        try{
            File testfile = new File(testOutputFilePath);
            Scanner myReader = new Scanner(testfile);
            
            while (myReader.hasNext()) {
                correctOutput.add(myReader.nextLine());
            }
            
            myReader.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Test file not found!");
            e.printStackTrace();
        }

        for ( int i = 0; i < log.size(); i++ ){
            if( !log.get(i).toLowerCase().equals(correctOutput.get(i).toLowerCase())){
                System.out.println("Test" + testNumber + " ERROR:");
                System.out.println("MyOutput: " + log.get(i));
                System.out.println("CorrectOutput: " + correctOutput.get(i));
            }
        }
        System.out.println("Test" + testNumber + ": Passed");
    }
} 