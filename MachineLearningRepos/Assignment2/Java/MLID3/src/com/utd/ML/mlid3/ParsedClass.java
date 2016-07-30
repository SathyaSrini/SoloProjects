/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.utd.ML.mlid3;

import java.util.Arrays;

/**
 *
 * @author SathyaNarayanan Srinivasan
 */

public class ParsedClass 
{
	
    
        // Instantiation for the class and rows in record variables in the Parsed Class
    
        public String classLabel;
        
       	public String[] rowInRecord;
        
        //Overload Constructor to set the count of features from the calling class
        
	public ParsedClass(int featureCountInput)
        {
		
		rowInRecord = new String[featureCountInput];
	
        }
        
        
        // GETTERS AND SETTERS FOR CLASS VARIABLES
        
	public String[] getRowInRecord() 
        {
		return rowInRecord;
	}
        
	public void setRowInRecord(String[] inputRecordInRow) 
        {
	
                this.rowInRecord = inputRecordInRow;
	
        }
        
	public String getClassLabel() 
        {
            
                return classLabel;
	
        }
        
	public void setClassLabel(String inputClassLabel) 
        {
	
                this.classLabel = inputClassLabel;
	
        }
	
	
        // Overriding toString Method to display the classLabels and rowInRecord
	
        @Override
	
        public String toString() 
        {
	
            return "Record[rowInRecord=" + Arrays.toString(rowInRecord)
				+ ",classLabel=" + classLabel + "]";

        }
	
	
	
}
