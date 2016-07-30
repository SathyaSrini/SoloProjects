import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KMeansTweetClustering 
{

    /**
     *
     */
    public static FileWriter fileWriter;
    public static BufferedWriter bufferedWriter;
    
    KMeansTweetClustering()
    {
            
    
        
        
    }
    ////////////////////////////////////////////////////////// SSE CALCULATION /////////////////////////////////////////////////////////////////////////////
	
        public ArrayList<Double>calculateSSE(ArrayList<Long> inputCentroids, HashMap<Integer,ArrayList<Long>> inputCluster, HashMap<Long, String> inputData)
	{
		ArrayList<Double> resultList = new ArrayList<>();
		for(int i=0;i<inputCentroids.size();i++)
		{
			double SSE = 0.0;
                        double squaredError = 0.0;
                        String value = "";
                        
			String centroid = inputData.get(inputCentroids.get(i));
			ArrayList<Long> idList = inputCluster.get(i);
			
                    for (Long idList1 : idList) 
                    {
                        value = inputData.get(idList1);
                        CalculateJaccard jd = new CalculateJaccard();
                        squaredError = jd.computeJaccard(centroid, value);
                        squaredError = Math.pow(squaredError, 2);
                        SSE += squaredError;
                    }
			resultList.add(SSE);
		}
		return resultList;
	}
	
    ////////////////////////////////////////////////////////// SSE CALCULATION /////////////////////////////////////////////////////////////////////////////
       
        public static void writeOutput(String inContent,BufferedWriter outWriter) throws IOException
        {
                outWriter.append(inContent);
                  
        }
	
        
        
        public static void main(String[] args) throws IOException
        {
            
            boolean outputWriterSuccess = false;
            
            //UNCOMMENT TO RUN FROM COMMAND LINE //
            
                int K = Integer.valueOf(args[0]);
                String initialSeedFile = args[1];
                String tweetsDataFile = args[2];
                String outputFile = args[3];
            
            //UNCOMMENT TO RUN FROM COMMAND LINE //
            
                System.out.println("Please enter the input in the following format : <numberOfClusters> <initialSeedsFile> <TweetsDataFile> <outputFile>");
                
                //UNCOMMENT TO RUN FROM NETBEANS or IDE//
                //int K = 25;
                //String initialSeedFile = "InitialSeeds.txt";
                //String tweetsDataFile = "Tweets.json";
                //String outputFile = "tweets-k-means-output.txt";
                //UNCOMMENT TO RUN FROM NETBEANS or IDE//
                
             
                    try {
                            File file = new File(outputFile);
                            if (!file.exists()) {
                                    file.createNewFile();
                            }

                            fileWriter = new FileWriter(file.getAbsoluteFile());
                            bufferedWriter = new BufferedWriter(fileWriter);
                            bufferedWriter.write("<cluster-id> := <List of tweet ids separated by comma>");
                            bufferedWriter.newLine();
                            System.out.println("Done Opening file and file creation succesful");
                            outputWriterSuccess = true;
                    } catch (IOException e) 
                    {

                        System.out.println("File cannot be opened to write the output, please fix this first!");
                        e.printStackTrace();
                    }
                
                       
                if(outputWriterSuccess)
                {
                    HashMap<Integer,ArrayList<Long>> newCluster;
                    ArrayList<Long> computedCentroid;
                    ArrayList<Double> sseValues;
                    KMeansTweetClustering obj;
                    int i;
                    int count;
                    InputParser jsonParser = new InputParser(tweetsDataFile);
                    ArrayList<Long> seedsInitialList = jsonParser.parseInitialCentroids(initialSeedFile);
                    jsonParser.createJsonObjects();
                    CalculateJaccard jaccardObj = new CalculateJaccard();
                    jaccardObj.initialse(seedsInitialList, jsonParser.tweetDataList, K);
                    jaccardObj.cluster();
                    HashMap<Integer,ArrayList<Long>> previousCluster = jaccardObj.cluster;
                    count = 0;

             
                do
		{
			computedCentroid = jaccardObj.repositionCentroid(jsonParser.tweetDataList);
			jaccardObj.initialse(computedCentroid, jsonParser.tweetDataList, K);
			jaccardObj.cluster();
			newCluster = jaccardObj.cluster;
			count++;
		}
		while(!jaccardObj.compareClusters(previousCluster,newCluster) && count <= 25);
                
                jaccardObj.cluster.entrySet().stream().map((Map.Entry<Integer, ArrayList<Long>> pair) -> {
                System.out.print(pair.getKey() + " :- " );
                    try {
                        writeOutput(pair.getKey() + " := ",bufferedWriter);
                    } catch (IOException ex) {
                        Logger.getLogger(KMeansTweetClustering.class.getName()).log(Level.SEVERE, null, ex);
                    }
                return pair;
            }).map((Map.Entry<Integer, ArrayList<Long>> pair) ->  
            {
                    return (ArrayList<Long>) pair.getValue();
                }).map((ArrayList<Long> values) -> {
                    values.stream().forEach((Long value) -> {
                    
                        try {
                            
                            System.out.print(value + ",");
                            writeOutput(value+",",bufferedWriter);
                        } catch (IOException ex) {
                            Logger.getLogger(KMeansTweetClustering.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    });
                return values;
            }).forEach((ArrayList<Long> _item) -> {
                    try {
                        writeOutput("\n",bufferedWriter);
                    } catch (IOException ex) {
                        Logger.getLogger(KMeansTweetClustering.class.getName()).log(Level.SEVERE, null, ex);
                    }
                System.out.println();
            });
		
          
                ///////////// PRINT SSE //////////////////////////////////
               
                obj = new KMeansTweetClustering();
                
                sseValues = obj.calculateSSE(computedCentroid, jaccardObj.cluster, jsonParser.tweetDataList);
                
                System.out.println("\n");
                bufferedWriter.newLine();
                System.out.println("Sum of Squared Error");
		bufferedWriter.write("SSE");
                System.out.println("\n");
                bufferedWriter.newLine();
        double sumSSE=0;       
                for(i=0;i<sseValues.size();i++)
		{
						sumSSE += Double.valueOf(sseValues.get(i));
                        System.out.println("Cluster "+i + ": = " + sseValues.get(i));
                        bufferedWriter.write("Cluster "+i + ": = " + sseValues.get(i));
                         bufferedWriter.newLine();
                
                }
		String sumSSE_String = String.valueOf(sumSSE);
		System.out.println("Total SSE : "+sumSSE_String);
		bufferedWriter.write("TOTAL-SSE:");
		bufferedWriter.write(sumSSE_String);
                ///////////// PRINT SSE //////////////////////////////////
                
	}
                            // CLOSING BUFFERED WRITER AFTER WRITE
                
                try {
                        bufferedWriter.close();
                    } catch (IOException ex) {
                        Logger.getLogger(KMeansTweetClustering.class.getName()).log(Level.SEVERE, null, ex);
                    }
            
                // CLOSING BUFFERED WRITER AFTER WRITE
        }
        
        }
