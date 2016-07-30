import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class JaccardCreator {
	
	HashMap<Integer,ArrayList<Double>> JaccardHashMap = new HashMap<>();
	HashMap<Integer,Long> JaccardKeyHashMap = new HashMap<>();
	HashMap<Integer,ArrayList<Long>> clusters = new HashMap<>();
	
        public JaccardCreator()
	{
		
	}
        
        
        /////////////////// HELPER METHODS /////////////////////////////////////
        
        public boolean doesContainInList(ArrayList<Long> setA, ArrayList<Long> setB)
	{
		for(int i=0;i<setA.size();i++)
		{
			if(!setA.contains(setB.get(i)))
				return false;
		}
		return true;
	}

	public boolean doesContainInHashMaps(HashMap<Integer,ArrayList<Long>> setA, HashMap<Integer,ArrayList<Long>> setB)
	{
            ArrayList<Long> setAList;
            ArrayList<Long> setBList;
            
            for (Entry pair : setA.entrySet()) {
                 setAList= (ArrayList<Long>) pair.getValue();
                 setBList = (ArrayList<Long>) pair.getValue();
                
                 if(!doesContainInList(setAList,setBList))
                 {   
                     
                     return false;
                 
                 }
            }
		return true;
	}
        
        
        public int returnMinIndexValue(ArrayList<Double> inputList)
	{
		int min_indexVal = 0;
		
                for(int i=1;i<inputList.size();i++)
		{
                
                    if(inputList.get(i) <= inputList.get(min_indexVal))
                    {	
                        min_indexVal = i;
                    }
                
                }
		return min_indexVal;
	}
	
	private double returnMinVal(ArrayList<Double> inputList)
	{
		double minValue;
                
                minValue= inputList.get(0);
		
                for(int i=1;i<inputList.size();i++)
		{
			if(inputList.get(i) <= minValue)
				minValue = inputList.get(i);
		}
		return minValue;		
	}
	
        /////////////////// HELPER METHODS /////////////////////////////////////
        
	
        public void initializeJaccard(ArrayList<Long> inputCentroidId, HashMap<Long,String> tweetData,int K)
	{
			
                        Iterator iterator;
                        int j;
                        
                        this.JaccardHashMap.clear();
			this.JaccardKeyHashMap.clear();
			
                        j=0;
                        iterator = tweetData.entrySet().iterator();
			
			while(iterator.hasNext())
			{
                                long cluster_id;
                                String tweetForid;
                                String tweet_centroidVal;
                                ArrayList<Double> alt;   
                                double result;
                                
				Entry keyValuePair = (Entry) iterator.next();
				cluster_id = (Long) keyValuePair.getKey();
				tweetForid = (String) keyValuePair.getValue();
				
                                for(int i=0;i<K;i++)
				{
					tweet_centroidVal= tweetData.get(inputCentroidId.get(i)); 
					
					result = this.mainJaccardMethod(tweet_centroidVal, tweetForid);
					
                                        if(this.JaccardHashMap.get(j) == null) 
					{
						alt = new ArrayList<>();
						alt.add(result);
					}
					else
					{
						alt = this.JaccardHashMap.get(j);
						alt.add(result);
					}
					this.JaccardHashMap.put(j, alt);
				}
				
                                this.JaccardKeyHashMap.put(j, cluster_id);
				j++;
			}
	}

       
        private int calculateIntersection(String setA, String setB)
	{
                int index;
		String[] setAWords;
                String[] setBWords;
                
                index = 0x0;
                setAWords = setA.split(" ");
		setBWords = setB.split(" ");
           
            for (String singleWordA : setAWords) {
                    for (String singleWordB : setBWords) {
                        if (singleWordA.equals(singleWordB)) {
                            index++;
                            break;
                        } else {
                        }
                    }
            }
		return index;
	}
        
        
	
	public double mainJaccardMethod(String setA,String setB)
	{
		
		double union;
                double distance;
                double intersection;
                
                setA = setA.toLowerCase();
		setB = setB.toLowerCase();
                intersection = this.calculateIntersection(setA, setB) * 1.0;
		
                String[] setAWords = setA.split(" ");
		String[] setBWords = setB.split(" ");
		
                union = (setAWords.length * 1.0) + (setBWords.length * 1.0) - intersection;
		
                distance = 1.0 - (intersection/union);
		distance = Math.round(0x2710 * distance) / 10000.0;
		
                return distance;
	}
	
	
	public void generateClusters()
	{

                this.JaccardHashMap.entrySet().stream().forEach((Entry<Integer, ArrayList<Double>> pair) -> 
                {
             
                int keyDerived;
                long idDerived;
                ArrayList<Double>tweetValue;
                int index_min;
                ArrayList<Long> alt;
                
                keyDerived = (int) pair.getKey();
                tweetValue = (ArrayList<Double>) pair.getValue();
                idDerived = this.JaccardKeyHashMap.get(keyDerived);
                index_min = this.returnMinIndexValue(tweetValue);
             
                
                if(this.clusters.get(index_min) == null)
                {
                    alt = new ArrayList<>();
                    alt.add(idDerived);
                }
                else
                {
                    alt = this.clusters.get(index_min);
                    alt.add(idDerived);
                }
                
                this.clusters.put(index_min, alt);
            });
	}
	
	public ArrayList<Long> moveCentroids(HashMap<Long,String> inputTweetData)
	{
		
		HashMap<Integer,ArrayList<Long>> previous_Cluster; 
                ArrayList<Long> next_Centroid;
                Iterator iterator;
                
                previous_Cluster = this.clusters;
		next_Centroid = new ArrayList<>();
		iterator = previous_Cluster.entrySet().iterator();
		
                while(iterator.hasNext())
		{
			ArrayList<Long> idList;
                        ArrayList<Double> computedJaccard;
                        HashMap<Double,Integer> mappedIndex;
                        long setA_Id;
                        long setB_Id;
                        String setAValue;
                        String setBValue;
                        double result;
                        double minimum_Val;
                        int index;
                        long valueComputed;
                        
                        Entry keyValuePair = (Entry) iterator.next();
			idList = (ArrayList<Long>) keyValuePair.getValue();
			computedJaccard = new ArrayList<>();
			mappedIndex = new HashMap<>();
			
                        for(int i=0;i<idList.size();i++)
			{
				for(int j=i+1;j<idList.size();j++)
                                {
					setA_Id = idList.get(i);
					setB_Id = idList.get(j);
					setAValue = inputTweetData.get(setA_Id);
					setBValue = inputTweetData.get(setB_Id);
					result = this.mainJaccardMethod(setAValue, setBValue);
					mappedIndex.put(result, i);
					computedJaccard.add(result);
				}
			}
			 minimum_Val = this.returnMinVal(computedJaccard);
			 index = mappedIndex.get(minimum_Val);
			 valueComputed = idList.get(index);
			 next_Centroid.add(valueComputed);
		}
		return next_Centroid;
	}
	
}
