/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utd.ML.mlid3;

import java.util.ArrayList;

/**
 *
 * @author Sathyanarayanan Srinivasan
 */
public final class IGDecisionTree {

    ArrayList<children> element;
    String featureSame = "featureSame";
    String classSame = "classValueSame";

    public class children 
    {

        IGDecisionTree ptrToChildElement;
        int childData;

    children(IGDecisionTree address, int inputVal) 
    {
            childData = inputVal;
            ptrToChildElement = address;

    }

    }

    MLID3 callingClassObj = new MLID3();

    ArrayList<ParsedClass> parsedElement;
    ArrayList<Integer> reminderFeatures;
    int bestAttrIndex;

    int classCount_Positive;
    int classCount_Negative;

    boolean isLeafNode;

    double entropy;
    String classValue, className;

    public IGDecisionTree() 
    {

    
    
    }

    public IGDecisionTree(ArrayList<ParsedClass> inputRecords, ArrayList<Integer> remainingAttributes) {

        parsedElement = inputRecords;
        reminderFeatures = new ArrayList<>();

        remainingAttributes.stream().forEach((remainingattributes11) -> {
            this.reminderFeatures.add(remainingattributes11);
        });
        classCount_Positive = calculatePositiveClasses(parsedElement);//calculates # of positive class labels for a set of inputLabels
        classCount_Negative = inputRecords.size() - classCount_Positive;
        entropy = calculateEntropy(parsedElement);
        isLeafNode = false;
        element = new ArrayList<>();

    }

    public void constructTree() {

        if (reminderFeatures.isEmpty()) {
            identifyLeaf(featureSame);
        } else if (classCount_Positive == parsedElement.size() || classCount_Negative == parsedElement.size()) {
            identifyLeaf(classSame);
        } else {

            bestAttrIndex = returnIndexMaxFeature();

            if (bestAttrIndex != -1) {

                int temp = reminderFeatures.get(bestAttrIndex);
                className = callingClassObj.returnFeatureLabel(reminderFeatures.get(bestAttrIndex));
                reminderFeatures.remove(bestAttrIndex);
                int j = 0;

                for (; j < 2; j++) {

                    ArrayList<ParsedClass> reminderElements = reminderElementRecord(parsedElement, temp, Integer.toString(j));
                    if (!reminderElements.isEmpty()) {

                        IGDecisionTree tempRecord = new IGDecisionTree(reminderElements, reminderFeatures);
                        element.add(new children(tempRecord, j));
                        tempRecord.constructTree();
                    }

                }
            } else {
                identifyLeaf(featureSame);
            }
        }
    }
    
        public String traverseTree(ParsedClass inputRecord)
        {

        IGDecisionTree node = this;

        String bestFeature = null;
        int bestFeatureIndex = 0;
        int testRecordValue = 0;
        String classLabel = null;

        while (node.isLeafNode != true) {

            bestFeature = node.className;
            bestFeatureIndex = MLID3.stdOutputFeatures.indexOf(bestFeature);
            testRecordValue = Integer.parseInt(inputRecord.getRowInRecord()[bestFeatureIndex]);
            node = node.element.get(testRecordValue).ptrToChildElement;
        }

        classLabel = node.classValue;

        return classLabel;
    }
        
        public double calculateEntropy(ArrayList<ParsedClass> records) {

        double entropyCalculated = 0;
        double probablityOfPositiveInstances = 0;
        double probabilityOfNegativeInstances = 0;

        if (records.isEmpty()) {

            return -1;

        } else {

            int positiveCountInClass = 0;
            int NegativeCountInClass = 0;
            double sizeOfRecord = records.size();

            for (ParsedClass record : records) {
                int a = Integer.parseInt(record.getClassLabel());
                if (a == 1) {
                    positiveCountInClass += 1;
                } else {
                    NegativeCountInClass += 1;
                }
            }

            probablityOfPositiveInstances = (double) positiveCountInClass / (double) sizeOfRecord;
            probabilityOfNegativeInstances = (double) NegativeCountInClass / (double) sizeOfRecord;

            entropyCalculated = -calc_logProbability(probablityOfPositiveInstances) - calc_logProbability(probabilityOfNegativeInstances);

            return entropyCalculated;
        }
    }

    public String print(int delimCount) {

        delimCount++;

        if (isLeafNode) {
            return classValue;
        } else {
            int counter = 0;
            for (; counter < element.size(); counter++) {

                System.out.println();
                int count = 0;

                for (; count < delimCount; count++) {
                    System.out.print("| ");
                }
                System.out.print("|" + className + "= " + element.get(counter).childData + ": ");

                String printFormat = element.get(counter).ptrToChildElement.print(delimCount);

                if (printFormat.equals("0") || printFormat.equals("1")) {
                    System.out.print(printFormat);
                }
            }
            return "null";
        }
    }
    
//========================================= HELPER METHODS =========================================////

        private int calculatePositiveClasses(ArrayList<ParsedClass> records) //calculates the positive count of class labels for the input dataInParsedClass
     {
        int count = 0;
        int i = 0;
        int a = 0;

        ParsedClass dataInParsedClass = null;

        for (; i < records.size(); i++) {
            dataInParsedClass = records.get(i);

            a = Integer.parseInt(dataInParsedClass.getClassLabel());

            if (a == 1) {
                count += 1;
            }

        }
        
    

        return count;

     }
    
        private static double calc_logProbability(double inputValue) 
        {

        if (inputValue == 0) 
        {
        
            return 0;

        }
        double logBase2 = inputValue * (Math.log(inputValue) / Math.log(2));

        if (!Double.isNaN(logBase2)) 
        {
            return logBase2;
        } else 
        {
            return 0;
        }
    }
    
        private ArrayList<ParsedClass> reminderElementRecord(ArrayList<ParsedClass> inputList, int pos_feature, String val_feature) 
        {
     
         ArrayList<ParsedClass> subset = new ArrayList<>();

        for (ParsedClass data : inputList) {
            if (data.getRowInRecord()[pos_feature].equals(val_feature)) {
                subset.add(data);
            }
        }
        return subset;
        }
     
        private double returnCurEntropy(ArrayList<ParsedClass> inputData) 
        {

        double negativeEntropy = 0;
        double positiveEntropy = 0;

        for(ParsedClass inputData1 : inputData) 
        {
            if (inputData1.classLabel.equals("0")) {
                negativeEntropy += 1;
            } else {
                positiveEntropy += 1;
            }
        }
        double temp_size = inputData.size();
        double actPosProbability = -calc_logProbability(positiveEntropy / temp_size);
        double actNegativeProbability = -calc_logProbability(negativeEntropy / temp_size);
        return (actPosProbability + actNegativeProbability);

        }
    
       private int returnIndexMaxFeature() 
        {

        double maximumInformationGain = 0;
        int index = 0;

        for (int i = 0; i < reminderFeatures.size(); i++) {

            double intermidEntropy = 0;
            double currentEntropy = 0;

            for (int j = 0; j < 2; j++) {

                ArrayList<ParsedClass> tempData = reminderElementRecord(parsedElement, reminderFeatures.get(i), Integer.toString(j));
                currentEntropy = returnCurEntropy(tempData);
                intermidEntropy += ((double) (double) tempData.size() / (double) parsedElement.size()) * currentEntropy;

            }
            
            if (maximumInformationGain < (entropy - intermidEntropy)) {

                maximumInformationGain = entropy - intermidEntropy;
                index = i;
            }

        }
        if (maximumInformationGain == 0) {
            return -1;
        }

        return index;
        }
     
       
 private void identifyLeaf(String type) {

        if (type.equals(featureSame)) {
            isLeafNode = true;
            classValue = parsedElement.get(0).classLabel;

        } else if (type.equals(classSame)) {
            isLeafNode = true;

            if (classCount_Positive > classCount_Negative) {

                classValue = "1";
            } else {

                classValue = "0";
            }
        }

    }

//========================================= HELPER METHODS =========================================////
}
