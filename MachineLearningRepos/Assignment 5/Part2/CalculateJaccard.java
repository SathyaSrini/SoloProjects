import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class CalculateJaccard {
	
	HashMap<Integer,ArrayList<Double>> JaccardSet = new HashMap<>();
	HashMap<Integer,Long> JaccardKeySet = new HashMap<>();
	HashMap<Integer,ArrayList<Long>> cluster = new HashMap<>();
        
	public CalculateJaccard()
	{
		
	}
	

	public double computeJaccard(String setA,String setB)
	{
		
                double intersection;
                double union;
                double distance;
                
                String[] setAs;
                String[] setBs;
                
                setA = setA.toLowerCase();
		setB = setB.toLowerCase();
                
		intersection = this.intersection_Jaccard(setA, setB) * 1.0;
		setAs = setA.split(" ");
		setBs = setB.split(" ");
                union = (setAs.length * 1.0) + (setBs.length * 1.0) - intersection;
		
                distance = 1.0 - (intersection/union);
		distance = Math.round(distance * 10000) / 10000.0;
		
                return distance;
	}
	
        private int intersection_Jaccard(String setA, String setB)
	{
		int count_intersections;
                String[]setAs;
                String[]setBs;
                
                count_intersections = 0;
		setAs = setA.split(" ");
		setBs = setB.split(" ");
            
                for (String strA : setAs) 
                {
                    for (String strB : setBs) 
                    {
                        if (strA.equals(strB)) 
                        {
                            count_intersections++;
                            break;
                        }
                    }
            }
		return count_intersections;
	}
        
	public int getMinValIndex(ArrayList<Double> listOfValues)
	{
		int minIndex = 0;
		
                double minValue;
                minValue = listOfValues.get(0);
		
                for(int i=1;i<listOfValues.size();i++)
		{
			if(listOfValues.get(i) <= listOfValues.get(minIndex)) 
                        {
                            minIndex = i;
                        } 
                        else 
                        {
                        
                        }
		}
		return minIndex;
	}
	
	private double getMinValue(ArrayList<Double> listOfValues)
	{
		double minValue = listOfValues.get(0);
		for(int i=1;i<listOfValues.size();i++)
		{
			if(listOfValues.get(i) <= minValue)
				minValue = listOfValues.get(i);
		}
		return minValue;		
	}
	public void initialse(ArrayList<Long> inputCentroidId, HashMap<Long,String> tweetText,int noOfClusters)
	{
			this.JaccardSet.clear();
			this.JaccardKeySet.clear();
			Iterator mp;
                        String tweet;
                       
                        mp = tweetText.entrySet().iterator();
			int j=0;
			while(mp.hasNext())
			{
				Entry pair;
                               
                                pair = (Entry) mp.next();
                                
                                long id = (Long) pair.getKey();
			
                                tweet = (String) pair.getValue();
				
                                for(int i=0;i<noOfClusters;i++)
				{
					String centroidTweet = tweetText.get(inputCentroidId.get(i)); 
					ArrayList<Double> temp;
					double res = this.computeJaccard(centroidTweet, tweet);
					if(this.JaccardSet.get(j) == null) 
					{
						temp = new ArrayList<>();
						temp.add(res);
					}
					else
					{
						temp = this.JaccardSet.get(j);
						temp.add(res);
					}
					this.JaccardSet.put(j, temp);
				}
				this.JaccardKeySet.put(j, id);
				j++;
			}
	}

	public void cluster()
	{
            this.JaccardSet.entrySet().stream().forEach((pair) -> 
            {
                int key = (int) pair.getKey();
                long id;
                id = this.JaccardKeySet.get(key);
                ArrayList<Double> values = (ArrayList<Double>) pair.getValue();
                int minIndex = this.getMinValIndex(values);
                ArrayList<Long> temp;
                if(this.cluster.get(minIndex) == null)
                {
                    temp = new ArrayList<>();
                    temp.add(id);
                }
                else
                {
                    temp = this.cluster.get(minIndex);
                    temp.add(id);
                }
                this.cluster.put(minIndex, temp);
            });
	}
	
	public ArrayList<Long> repositionCentroid(HashMap<Long,String> tweetData)
	{
		//For that cluster, find the tweet that has the minimum distance to all other tweets in the cluster
		HashMap<Integer,ArrayList<Long>> prevCluster = this.cluster;
		ArrayList<Long> newCentroids = new ArrayList<Long>();
		Iterator mp = prevCluster.entrySet().iterator();
		while(mp.hasNext())
		{
			Entry pair = (Entry) mp.next();
			int key = (int) pair.getKey();
			ArrayList<Long> ids = (ArrayList<Long>) pair.getValue();
			ArrayList<Double> jaccardValues = new ArrayList<Double>();
			HashMap<Double,Integer> indexMapping = new HashMap<Double,Integer>();
			for(int i=0;i<ids.size();i++)
			{
				for(int j=i+1;j<ids.size();j++)
				{
					long s1 = ids.get(i);
					long s2 = ids.get(j);
					String str1 = tweetData.get(s1);
					String str2 = tweetData.get(s2);
					double res = this.computeJaccard(str1, str2);
					indexMapping.put(res, i);
					jaccardValues.add(res);
				}
			}
			double minValue = this.getMinValue(jaccardValues);
			int index = indexMapping.get(minValue);
			long val = ids.get(index);
			newCentroids.add(val);
		}
		return newCentroids;
	}
	
	public boolean compareArrayLists(ArrayList<Long> l1, ArrayList<Long> l2)
	{
		for(int i=0;i<l1.size();i++)
		{
			if(!l1.contains(l2.get(i)))
				return false;
		}
		return true;
	}

	public boolean compareClusters(HashMap<Integer,ArrayList<Long>> h1, HashMap<Integer,ArrayList<Long>> h2)
	{
            for (Entry pair : h1.entrySet()) {
                ArrayList<Long> s1 = (ArrayList<Long>) pair.getValue();
                ArrayList<Long> s2 = (ArrayList<Long>) pair.getValue();
                if(!compareArrayLists(s1,s2))
                    return false;
            }
		return true;
	}

}
