/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utd.ML.mlid3;

import java.io.*;
import java.io.BufferedReader;
import java.util.ArrayList;
/**
 *
 * @author Sathyanarayanan Srinivasan
 */
public class MLID3 
{
    	public static ArrayList<Integer> features = new ArrayList<Integer>();
        
	public static ArrayList<String> featuresParsed = new ArrayList<String>();
	public static ArrayList<String> stdOutputFeatures = new ArrayList<String>();
        
	public static ArrayList<ParsedClass> inputLabels = new ArrayList<ParsedClass>();
	public static ArrayList<ParsedClass> testInput = new ArrayList<ParsedClass>();
    /**
     * @param args the command line arguments
     * 0 - K  Value
     * 1 - L Value
     * 2 - Training File
     * 3 - Validation File
     * 4 - Testing File
     * 5 - Standard output Choice - Y/N
     */
    public static void main(String[] args) throws Exception 
	{
	
            int L;
            int K;
            String trainingFile=null;
            String validationFile=null;
            String testFile=null;
            String stdOutputChoice=null;
            String optionCheck=null;
            double actualAccuracy=0;
            MLID3 obj = new MLID3(); 
                
        if(args.length==6) // Parsing the input from the command line or properties tab in IDE during runtime.
        {
                System.out.println("Entered 6 arguments");
                L = Integer.parseInt(args[0]);
		K = Integer.parseInt(args[1]);
                trainingFile = args[2];
		validationFile = args[3];
		testFile = args[4];
		stdOutputChoice = args[5];
                optionCheck = "yes";
        }
        else if(args.length!= 6) //Checking for proper input parsing from  args
        {
	System.out.println(".\\program <L> <K> <training-set> <validation-set> <test-set> <to-print>\n" +
        "     L: integer (used in the post-pruning algorithm)\n" +
        "     K: integer (used in the post-pruning algorithm)\n" +
        "     to-print:{yes,no}");
	
        return;
	
        }

//Exclusive Parsing method for extract and storing the atrributes from  the Training File         
               
                MLID3.extractAttrFromTrainingFile(trainingFile);
                
//Construction of a decisionTree using INFORMATION GAIN ALGORITHM
                
                IGDecisionTree decisTree_IG = new IGDecisionTree(inputLabels, features);
		decisTree_IG.constructTree();

//Construction of a decisionTree using VARIANCE IMPURITY HEURISTIC ALGORITHM

                VIDecisionTree decisTree_VI = new VIDecisionTree(inputLabels, features);
		decisTree_VI.constructTree();

//Parsing Print Options                

	if(stdOutputChoice.equalsIgnoreCase(optionCheck))
        {
            
        //Printing the required output in STD OUTPUT with Impurity Variance
                        System.out.println();
			System.out.println("//////==============================//////////////////");
			System.out.println("Tree with Impurity variance as the splitting method :");
			decisTree_VI.print(-1);    
                        
        //Printing the required output in STD OUTPUT with Information Gain
                        System.out.println();
                        System.out.println("//////==============================//////////////////");
			System.out.println("Tree with Information Gain as the splitting method :");
                        decisTree_IG.print(-1);
           

        }
        else
        {
            System.out.println("You have chosen, not to print the tree in standard output");
        }

// Exclusive Parsing method for extract and storing the atrributes from  the Test File
      
                extractAttrFromTestFile(testFile);
                
//Calculating accuracy for the tree with Variance Impurity  
		
                int accuracyVI = 0;
		
                for( int i = 0; i< testInput.size();i++)
                {
                        String VI_ClassValue = decisTree_VI.traverseTree(testInput.get(i));
			
                        if(VI_ClassValue.equals(testInput.get(i).classLabel))
                        {
				accuracyVI +=1;
			}	
		}
		
		actualAccuracy = (double)(((double)accuracyVI/(double)testInput.size())*100);
		System.out.println("\n Variance impurity as heuristic : "+ actualAccuracy);

//Calculating accuracy for the tree with Information Gain  
                
		int accuracyIG = 0;
		for( int i = 0; i < testInput.size();i++)
                {
			String IG_ClassValue = decisTree_IG.traverseTree(testInput.get(i));
		
                        if(IG_ClassValue.equals(testInput.get(i).classLabel))
                        {
				accuracyIG +=1;
			}	
		
                }
		actualAccuracy = (double)(((double)accuracyIG/(double)testInput.size())*100);
                System.out.println("\n Information Gain as heuristic : "+ actualAccuracy);		
	}


//HELPER METHODS
    
    
  public static void extractAttrFromTrainingFile(String filename) throws Exception 
  {
      
            int index = 0;
            
            String delimiter = ",";
            int feature_Count = 0;
            
            String splitCurrentLine;
            String[] tempArray1;
            String[] tempArray2;
            
            String currentLine = null;
            String[] trainingFeatureList = null;
            ParsedClass parsedLine = null;
            
        //Initialising to read from Training File
		BufferedReader br = new BufferedReader(new FileReader(filename));
		
                currentLine = br.readLine();

		trainingFeatureList =  currentLine.split(delimiter);
		
                feature_Count = trainingFeatureList.length - 1; 

		for(String s: currentLine.split(delimiter) )
                {
			stdOutputFeatures.add(s);
                        features.add(index);
		
			index++;
		}
		
                stdOutputFeatures.remove(feature_Count);
                features.remove(feature_Count); 
	
        //Storing the parsed data           
                
		while(currentLine!=null)
                {

		currentLine = br.readLine();
		
                if(currentLine !=null)
		{

		parsedLine = new ParsedClass(feature_Count);
		splitCurrentLine = currentLine.substring(0,2*(feature_Count));
		
                tempArray1 = splitCurrentLine.split(delimiter);
                tempArray2 = currentLine.split(delimiter);
		
                parsedLine.setRowInRecord(tempArray1);
		parsedLine.setClassLabel(tempArray2[feature_Count]);
		
                inputLabels.add(parsedLine);

		}
		
                }
		br.close();
  
  
  }  

  public static void extractAttrFromTestFile(String testfilename) throws Exception
  {
      String delimiter = ",";
                
                String splitCurrentLine;
                String[] tempArray1;
                String[] tempArray2;
            
                String currentLine = null;
                String[] testingFeatureList = null;
                
                int countTestingFeatures = 0;
                ParsedClass parsedLine = null;
            
        //Initialising to read from Test File
                
                BufferedReader br = new BufferedReader(new FileReader(testfilename));
                
                
		currentLine = br.readLine();
                
		testingFeatureList =  currentLine.split(delimiter);
                
		countTestingFeatures = testingFeatureList.length - 1; 
		
        //Storing the parsed data
            
                while(currentLine!=null){

			currentLine = br.readLine();
			if(currentLine !=null)
			{
				parsedLine = new ParsedClass(countTestingFeatures);
				
                                splitCurrentLine = currentLine.substring(0,2*(countTestingFeatures));
				tempArray1 = splitCurrentLine.split(delimiter);
				tempArray2 = currentLine.split(delimiter);
				
                                parsedLine.setRowInRecord(tempArray1);
				parsedLine.setClassLabel(tempArray2[countTestingFeatures]);
				
                                testInput.add(parsedLine);
			}
		}
		br.close();

  }

  public String returnFeatureLabel(int inputAttributeValue){
		return stdOutputFeatures.get(inputAttributeValue);
	}

}
