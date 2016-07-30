import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;


public class NormalHash {
		
	int valsToHash=0,buckets=0;
	
	int primeNumber=0;
	
	int rndmPrime1=0,rndmPrime2=0;
	
	ArrayList<BigInteger> array = new ArrayList<>();
	
	Random generator = new Random(23);
	
	ArrayList<ArrayList<Integer>> tempArray = new ArrayList<>();
	
        int sizeOfTemp=0; 

	public NormalHash(int valuesToHash_Input)
	{
		valsToHash = valuesToHash_Input;
		buckets=valsToHash; 
		
		BigInteger bigPrimeTemp = new BigInteger(String.valueOf(buckets));
		bigPrimeTemp = bigPrimeTemp.nextProbablePrime();
		primeNumber = bigPrimeTemp.intValue();
		
		
		BigInteger bigIntTemp = new BigInteger("0");
				
		while(bigIntTemp.compareTo(bigPrimeTemp) == -1)
		{
			array.add(bigIntTemp);
			bigIntTemp = bigIntTemp.nextProbablePrime();			
		}
		
		for(int i = 0; i < buckets; i++)
		{
			tempArray.add(new ArrayList<>());
		}
	}
	
	public void hash()
	{

		int length = array.size();
		
		int nextRandomVal = generator.nextInt(length);
		rndmPrime1 = array.get(nextRandomVal).intValue();
		
		int temp = nextRandomVal;
		
		while(temp == nextRandomVal)
		{
		nextRandomVal = generator.nextInt(length);
		}
		rndmPrime2 = array.get(nextRandomVal).intValue();
	}
	
	public int Lookup(int x)
	{
		if(!tempArray.isEmpty())
		{
			if(tempArray.get(hashFunction1(x)).contains(x))
				return 1;
			else
				return 0;
		}
		else
		{
			return 0;
		}
	}

	public int hashFunction1(int x)
	{
		return (((rndmPrime1*x)+rndmPrime2)%primeNumber)%buckets;
	}
		
	public void put(int x)
	{
		int position=0;
		
		if(Lookup(x)==1)
		{
			return;
		}
		else
		{
			position=hashFunction1(x);
			tempArray.get(position).add(x); 			
		}
	}
		
	
}
