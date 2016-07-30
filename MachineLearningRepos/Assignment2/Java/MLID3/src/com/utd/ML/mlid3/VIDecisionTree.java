/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utd.ML.mlid3;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author Sathyanarayanan Srinivasan
 */
public class VIDecisionTree 
{

    ArrayList<children> element;
    String featureSame = "featureSame";
    String classSame = "classValueSame";

    public class children {

        VIDecisionTree ptrToChildElement;
        int childData;

        children(VIDecisionTree address, int v) {
            childData = v;
            ptrToChildElement = address;

        }
    }
    MLID3 callingClassObj = new MLID3();

    ArrayList<ParsedClass> parsedElement;
    ArrayList<Integer> reminderFeatures;

    int bestAttrIndex;
    String classvalue, className;
    boolean isLeafNode;
    int classCount_Positive;
    int classCount_Negative;

    double heuristic_variance;

    public VIDecisionTree() 
    {

    }

    public VIDecisionTree(ArrayList<ParsedClass> inputRecords, ArrayList<Integer> input_remainingFeatures) {

        parsedElement = inputRecords;
        reminderFeatures = new ArrayList<Integer>();

        for (int count = 0; count < input_remainingFeatures.size(); count++) {
            this.reminderFeatures.add(input_remainingFeatures.get(count));
        }

        classCount_Positive = calulatePositiveClasses(parsedElement);
        classCount_Negative = inputRecords.size() - classCount_Positive;
        heuristic_variance = calculateVariance(parsedElement);
        isLeafNode = false;
        element = new ArrayList<VIDecisionTree.children>();

    }

    public void constructTree() throws FileNotFoundException {

        if (reminderFeatures.size() == 0) {
            identifyLeaf(featureSame);

        } else if (classCount_Positive == parsedElement.size() || classCount_Negative == parsedElement.size()) {
            identifyLeaf(classSame);

        } else {

            bestAttrIndex = returnIndexMaxFeature();

            if (bestAttrIndex != -1) {
                className = callingClassObj.returnFeatureLabel(reminderFeatures.get(bestAttrIndex));
                int temp = reminderFeatures.get(bestAttrIndex);
                reminderFeatures.remove(bestAttrIndex);
                int j = 0;
                for (; j < 2; j++) {

                    ArrayList<ParsedClass> reminderElements = reminderElementRecord(parsedElement, temp, Integer.toString(j));
                    if (reminderElements.size() != 0) {

                        VIDecisionTree tempRecord = new VIDecisionTree(reminderElements, reminderFeatures);
                        element.add(new children(tempRecord, j));
                        tempRecord.constructTree();

                    }

                }
            } else {
                identifyLeaf(classSame);
            }

        }
    }

    public String traverseTree(ParsedClass testRecord) {

        VIDecisionTree node = this;
        while (node.isLeafNode != true) {

            String best_attribute_name = node.className;
            int best_attribute_index = MLID3.stdOutputFeatures.indexOf(best_attribute_name);
            int testrec_value = Integer.parseInt(testRecord.getRowInRecord()[best_attribute_index]);
            node = node.element.get(testrec_value).ptrToChildElement;
        }
        String obtained_classlabel = node.classvalue;
        return obtained_classlabel;
    }

    private double calculateVariance(ArrayList<ParsedClass> inputRecord) {

        int total = classCount_Positive + classCount_Negative;

        double positiveVariance = (double) ((double) classCount_Positive / (double) total);
        double negativeVariance = (double) ((double) classCount_Negative / (double) total);

        double variance = positiveVariance * negativeVariance;

        if (!Double.isNaN(variance)) {
            return variance;
        } else {
            return 0;
        }

    }

    public String print(int delimCount) {

        delimCount++;

        if (isLeafNode) {

            return classvalue;
        } else {

            int counter = 0;

            for (; counter < element.size(); counter++) {
                System.out.println();
                int count = 0;

                for (; count < delimCount; count++) {
                    System.out.print("| ");
                }
                System.out.print("|" + className + "= " + element.get(counter).childData + ": ");
                String formatPrint = element.get(counter).ptrToChildElement.print(delimCount);
                if (formatPrint.equals("0") || formatPrint.equals("1")) {
                    System.out.print(formatPrint);
                }
            }
            return "null";
        }
    }

    

//========================================= HELPER METHODS =========================================////
     
     private int calulatePositiveClasses(ArrayList<ParsedClass> records) {
        int count = 0;
        int j = 0;
        int classLabel = 0;
        ParsedClass dataInParsedClass = null;
        for (; j < records.size(); j++) {
            dataInParsedClass = records.get(j);

            classLabel = Integer.parseInt(dataInParsedClass.getClassLabel());

            if (classLabel == 1) {

                count += 1;

            }

        }
        return count;
    }
     
     private ArrayList<ParsedClass> reminderElementRecord(ArrayList<ParsedClass> inputList, int pos_feature, String value_feature) {

        ArrayList<ParsedClass> parsedSubset = new ArrayList<ParsedClass>();

        for (int i = 0; i < inputList.size(); i++) {
            ParsedClass data = inputList.get(i);

            if (data.getRowInRecord()[pos_feature].equals(value_feature)) {

                parsedSubset.add(data);

            }
        }

        return parsedSubset;
    }
     
    private double returnCurVariance(ArrayList<ParsedClass> inputData) {
        int positive_Variance = 0;
        int negative_variance = 0;
        int i = 0;

        for (; i < inputData.size(); i++) {

            ParsedClass parsedRecord = inputData.get(i);

            if (parsedRecord.getClassLabel().equals("1")) {

                positive_Variance += 1;
            }
        }
        negative_variance = inputData.size() - positive_Variance;

        double intermidPositive = (double) ((double) positive_Variance / inputData.size());
        double intermidNegative = (double) ((double) negative_variance / inputData.size());

        if (!Double.isNaN((intermidPositive * intermidNegative))) {

            return (intermidPositive * intermidNegative);

        } else {
            return 0;
        }

    }
    
    
    private int returnIndexMaxFeature() throws FileNotFoundException 
    {
    double maximumInformationGain = 0;
		int index =  0;

		for(int i = 0; i< reminderFeatures.size();i++){

			double intermidVariance = 0;
			for(int j=0; j<2; j++){

				ArrayList<ParsedClass> subdata = reminderElementRecord(parsedElement,reminderFeatures.get(i), Integer.toString(j));
				double  curvalue_variance  =  returnCurVariance(subdata);
		
				intermidVariance  += (subdata.size()/ (double)parsedElement.size() ) * curvalue_variance;
			}

			if(maximumInformationGain < (heuristic_variance - intermidVariance)){

				maximumInformationGain = heuristic_variance - intermidVariance;
				index = i;	

			}
		}
		if(maximumInformationGain == 0){
			return -1;
		}

		return index;
}
     
    private void identifyLeaf(String type) {

        if (type.equals(featureSame)) {
            isLeafNode = true;
            classvalue = parsedElement.get(0).classLabel;

        } else if (type.equals(classSame)) {
            isLeafNode = true;

            if (classCount_Positive > classCount_Negative) {

                classvalue = "1";
            } else {

                classvalue = "0";
            }
        }

    }

//========================================= HELPER METHODS =========================================////
}
