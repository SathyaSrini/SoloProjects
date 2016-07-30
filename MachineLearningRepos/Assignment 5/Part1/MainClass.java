import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

public class MainClass {
	public static int K;
	public static int ITERATIONS;
	public static double DEFAULT_LARGE_VALUE;
        public static String inputFileName;
        public static String outputFileName;
        HashMap<Integer, ArrayList<Integer>> clusterPoints = new HashMap<>();
        public static FileWriter fileWriter;
        public static BufferedWriter bufferedWriter;
        

        /////////////////////////////// HELPER METHODS ////////////////////////////////////////////
        
        public int[] getRandom(DataPoints[] inputPoints,int k,int Min,int Max)
	{
		HashSet<Integer> sample = new HashSet<>();
                Random randomNumber;
		int[] result;
		int x;
                
                result = new int[k];
                
		for(int i=0;i<K;i++)
		{
			do
			{
                            randomNumber = new Random();
                            x = randomNumber.nextInt((Max - Min) + 1) + Min;
			} while(sample.contains(x));
			result[i] = x;
			sample.add(x);
		}
		return result;
	}
        
        public int returnMinimum(double[] inputArray)
	{
		double min;
                int index_minVal;
                
                min = inputArray[0];
		index_minVal = 0;
		
                for(int i=1;i<inputArray.length;i++)
		{
			if(inputArray[i] <= min)
			{
				min = inputArray[i];
				index_minVal = i;
			}
		}
		return index_minVal;
	}
        
        
        public boolean containsInCentroid(DataPoints[] inputCentroids,DataPoints inputPoints)
	{
            for (DataPoints centroid1 : inputCentroids) 
            {
                
                if (centroid1.getId() == inputPoints.getId()) {
                    return true;
                }
            }
		return false;
	}
        
        
        private double returnAverage(ArrayList<Double> inputList)
	{
		int count;
                double sum;
                double avg;
                double result;
                
                count = inputList.size();
		sum = 0.0;
		
                for(int i = 0;i<count;i++)
                {
			sum += inputList.get(i);
		
                }

                avg = sum/count;
                
		result = Math.round(avg * 10000) / 10000.0;
		
                return result;
	}
	
	private DataPoints returnAverage_Points(int inputCentroidiD, ArrayList<Integer> inputPointList, DataPoints[] points)
	{
		DataPoints temp = new DataPoints();
                ArrayList<Double> xValues;
		ArrayList<Double> yValues;
                double average_X;
                double average_Y;
                
                temp.setId(inputCentroidiD);
                xValues = new ArrayList<>();
                yValues = new ArrayList<>();
                
                inputPointList.stream().map((listOfPoint) -> listOfPoint).map((Integer tempId) -> {
                    xValues.add(points[tempId-1].getX());
                return tempId;
            }).forEach((tempId) -> {
                yValues.add(points[tempId-1].getY());
            });
		
                average_X = this.returnAverage(xValues);
		average_Y = this.returnAverage(yValues);
		
                temp.setX(average_X);
		temp.setY(average_Y);
		
                return temp;
	}
        
        public boolean doesContainInList(ArrayList<Integer> ListA, ArrayList<Integer> ListB)
	{
		for(int i=0;i<ListA.size();i++)
		{
			if(!ListA.contains(ListB.get(i)))
				return false;
		}
		return true;
	}
	
	public boolean doesContainInHashMaps(HashMap<Integer,ArrayList<Integer>> hashA, HashMap<Integer,ArrayList<Integer>> hashB)
	{

            for (Entry pair : hashA.entrySet()) 
            {
                ArrayList<Integer> s1 = (ArrayList<Integer>) pair.getValue();
                ArrayList<Integer> s2 = (ArrayList<Integer>) pair.getValue();
                
                if(!doesContainInList(s1,s2))
                {
                    return false;
                }
            }
		return true;
	}
        
        /////////////////////////////// HELPER METHODS ////////////////////////////////////////////
	
	public static void writeOutput(String inContent,BufferedWriter outWriter) throws IOException
        {
                
                outWriter.append(inContent);
                  
        }
        
	public DataPoints[] returnCentroids(int[] result,DataPoints[] inputPoints)
	{
		DataPoints[] cetnroids;
                
                cetnroids = new DataPoints[K];
		
                for(int i=0;i<K;i++)
		{
			cetnroids[i] = inputPoints[result[i]-1];
		}
		return cetnroids;
	}
	
	public double calculateEuclidian(double xA,double yA,double xB,double yB)
	{
		double diff_XCoord;
		double diff_YCoord;
		double SquOfX;
		double SquOfY;
		double dist;
                diff_XCoord = xB - xA;
		diff_YCoord = yB - yA;
		SquOfX = Math.pow(diff_XCoord, 2);
		SquOfY = Math.pow(diff_YCoord, 2);
		dist = Math.sqrt(SquOfX+SquOfY);
                return dist;
	}
	
	
	
	public int returnCluster(DataPoints[] inputCentroid, DataPoints inputPoints)
	{
		double[] distances;
                int minElementIndex;
                
                distances = new double[K];
		for(int i=0;i<inputCentroid.length;i++)
                {
			distances[i] = this.calculateEuclidian(inputCentroid[i].getX(), inputCentroid[i].getY(), inputPoints.getX(), inputPoints.getY());
                }
                minElementIndex = this.returnMinimum(distances);
		
                return minElementIndex;
	}
	
	
		
	public void generateCentroids(DataPoints[] inputCentroids,DataPoints[] inputPoints)
	{
            int centroid_index;
            int centroid_id;
            int point_id;
            ArrayList<Integer> listTemp;
            
            for (DataPoints points : inputPoints) {
                centroid_index = this.returnCluster(inputCentroids, points);
                centroid_id = inputCentroids[centroid_index].getId();
                point_id = points.getId();
                
                if(this.clusterPoints.get(centroid_id) != null)
                {
                    listTemp = this.clusterPoints.get(centroid_id);
                    listTemp.add(point_id);
                }
                else
                {
                    listTemp = new ArrayList<>();
                    listTemp.add(point_id);
                }
                this.clusterPoints.put(centroid_id, listTemp);
            }
	}

	
	public DataPoints[] moveCentroids(DataPoints[] inputPoints)
	{
		DataPoints[] finalCentroidPoints;
		HashMap<Integer,ArrayList<Integer>> currentClusters ;
		Iterator iterator;
                DataPoints point_average;
                
                finalCentroidPoints = new DataPoints[K];
                currentClusters = this.clusterPoints;
                iterator = currentClusters.entrySet().iterator();
		int i = 0;
                int point_id;
                
		while(iterator.hasNext())
		{
			Entry keyValuePair = (Entry) iterator.next();
			
			ArrayList<Integer> pointIds = (ArrayList<Integer>) keyValuePair.getValue();
			if(pointIds.size() > 1)
			{
				point_average = this.returnAverage_Points(i, pointIds, inputPoints);
			}
			else
			{
				point_id = pointIds.get(0);
				point_average = inputPoints[point_id-1];
				point_average.setId(i);
			}
			
                        finalCentroidPoints[i] = point_average;
			i++;
		}
		System.out.println();
		
                return finalCentroidPoints;
	}
	
	
	public ArrayList<Double> calculateSSE(DataPoints[] inputCentroids,HashMap<Integer,ArrayList<Integer>> inputCluster,DataPoints[] totalPoints)
	{
		
            ArrayList<Double> finalResult = new ArrayList<>();
            double result = 0.0;
            double centroid_X;
            double centroid_Y;
            int id_point;
            double point_x;
            double point_Y;
            double squaredVal;
            
            ArrayList<Integer> pointIds;
            
		for(int i=0;i<inputCentroids.length;i++)
		{
			
                        centroid_X = inputCentroids[i].getX();
			centroid_Y = inputCentroids[i].getY();
			
                        pointIds = inputCluster.get(i);
                    
                        for (Integer pointId : pointIds) 
                        {
                            id_point = pointId;
                            point_x = totalPoints[id_point-1].getX();
                            point_Y = totalPoints[id_point-1].getY();
                            squaredVal = this.calculateEuclidian(centroid_X, centroid_Y, point_x, point_Y);
                            squaredVal = Math.pow(squaredVal, 2);
                            result = result + squaredVal;
                    }
			finalResult.add(result);
		}
		return finalResult;
	}
	
	public static void main(String[] args) throws IOException 
        {
		
                boolean outputWriterSuccess = false;
                
                
                //UNCOMMENT FOR COMMANDLINE // 
                K = Integer.valueOf(args[0]);
                inputFileName = args[1];
                outputFileName = args[2];
                //UNCOMMENT FOR COMMANDLINE //
                
                
                //K = 25;
                //inputFileName = "test_data.txt";
                //outputFileName = "output_kMeans.txt";        

                ITERATIONS = 25;
                DEFAULT_LARGE_VALUE = 500000.0;
                
                
                 try {
			File file = new File(outputFileName);
			if (!file.exists()) 
                        {
			
                                file.createNewFile();
			
                        }

			fileWriter = new FileWriter(file.getAbsoluteFile());
			bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write("<cluster-id> := <List of tweet ids separated by comma>");
                        bufferedWriter.newLine();
			System.out.println("Done Opening file and file creation succesful");
                        outputWriterSuccess = true;
                        
		} catch(IOException e) 
                {
		
                    System.out.println("File cannot be opened to write the output, please fix this first!");
                    e.printStackTrace();
		
                }
                
            if(outputWriterSuccess)
            {     
                InputParser parser;
                MainClass mainObj;
                HashMap<Integer, ArrayList<Integer>> prev_Clusters;
                HashMap<Integer, ArrayList<Integer>> new_Clusters;
                DataPoints[] resultPoints;
                int[] idArray;
                DataPoints[] centroidPoints;
                DataPoints[] newCentroids;
		Iterator iterator;
                int id_cluster;
                ArrayList<Double> sseValueList;
                ArrayList<Integer> pointIds;
                
                parser = new InputParser(inputFileName);
		mainObj = new MainClass();
                
		resultPoints = parser.getPoints();
		idArray = mainObj.getRandom(resultPoints, K, 1, parser.getPointCount());
		centroidPoints = mainObj.returnCentroids(idArray, resultPoints);
		mainObj.generateCentroids(centroidPoints, resultPoints);
		System.out.println();
                System.out.println("\n");
		prev_Clusters = mainObj.clusterPoints;
		newCentroids = mainObj.moveCentroids(resultPoints);
		
                for(int i=0;i<MainClass.ITERATIONS;i++)
		{
			mainObj.clusterPoints.clear();
			mainObj.generateCentroids(newCentroids, resultPoints);
			new_Clusters = mainObj.clusterPoints;
			
                        if(mainObj.doesContainInHashMaps(prev_Clusters, new_Clusters) == true)
                        {	
                            break;
                        }
			
                        newCentroids = mainObj.moveCentroids(resultPoints);
		}
		
                iterator = mainObj.clusterPoints.entrySet().iterator();
                
		System.out.println("Cluster ID\tList of points");
                
                //writeOutput("Cluster ID\tList of points", bufferedWriter);
                
                String str = null;
                
                while(iterator.hasNext())
		{
			Entry keyValuePair = (Entry) iterator.next();
			id_cluster = (int) keyValuePair.getKey();
			System.out.print(id_cluster+1 + "\t\t");
                        writeOutput(id_cluster+1 + "\t\t", bufferedWriter);
			pointIds = (ArrayList<Integer>) keyValuePair.getValue();
			
                        for(int i=0;i<pointIds.size()-1;i++)
                            str = pointIds.get(i) + ",";
                            System.out.print(str);
                            writeOutput(str, bufferedWriter);
                            str = String.valueOf(pointIds.get(pointIds.size()-1));
                            System.out.print(str);
                            writeOutput(str, bufferedWriter);
                            System.out.println();
                            writeOutput("\n",bufferedWriter);
                        
		}
		
                System.out.println();
                writeOutput("\n",bufferedWriter);
                
		sseValueList = mainObj.calculateSSE(newCentroids, mainObj.clusterPoints, resultPoints);
		System.out.println("\nSSE");
                bufferedWriter.newLine();
                writeOutput("\nSSE",bufferedWriter);
                bufferedWriter.newLine();
		
		double sumSSE=0;
                
		for(int i=0;i<sseValueList.size();i++)
		{
						sumSSE += Double.valueOf(sseValueList.get(i));
                        str = i+1 + " = " + sseValueList.get(i);
                    	System.out.println(str);
                        writeOutput(str, bufferedWriter);
                        bufferedWriter.newLine();
                }
		String sumSSE_String = String.valueOf(sumSSE);
		System.out.println("Total SSE : "+sumSSE_String);
		writeOutput("TOTAL-SSE:",bufferedWriter);
		writeOutput(sumSSE_String,bufferedWriter);
	}
       
        
            bufferedWriter.close();
        
        }
        
}
