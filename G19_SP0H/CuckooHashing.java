import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;


public class CuckooHashing {
	       
	int valsToHash=0,buckets=0;
        int RehashCount=0;
	int primeNumbers=0;
        int[] tempArray;
	int sizeOfTempArray=0; 
        int rndmP1=0,rndmP2=0,rndmP3=0,rndmP4=0;
	
	ArrayList<BigInteger> storeList = new ArrayList<>();
	
	Random rndm = new Random(23);

	public CuckooHashing(int valuesToHash_Input)
	{
		valsToHash = valuesToHash_Input;
		buckets=6*valsToHash;
		tempArray = new int[buckets];
                
		BigInteger bigPrimeTemp = new BigInteger(String.valueOf(buckets));
		bigPrimeTemp = bigPrimeTemp.nextProbablePrime();
		primeNumbers = bigPrimeTemp.intValue();
		
		BigInteger bigIntTemp = new BigInteger("0");
				
		while(bigIntTemp.compareTo(bigPrimeTemp) == -1)
		{
			storeList.add(bigIntTemp);
			bigIntTemp = bigIntTemp.nextProbablePrime();			
		}
	}
	
	public void hash()
	{
		int length = storeList.size();
	
		int nextRandomVal = rndm.nextInt(length);
                
		rndmP1 = storeList.get(nextRandomVal).intValue();
		
		int firstRandomTemp = nextRandomVal;
		
		while(firstRandomTemp == nextRandomVal)
		{
		nextRandomVal = rndm.nextInt(length);
		}
		rndmP2 = storeList.get(nextRandomVal).intValue();
		
		int secondRandomTemp = nextRandomVal;
		
		while(secondRandomTemp == nextRandomVal || firstRandomTemp == nextRandomVal)
		{
		nextRandomVal = rndm.nextInt(length);
		}
		rndmP3 = storeList.get(nextRandomVal).intValue();
		
		int thirdRandomTemp = nextRandomVal;
		
		while(thirdRandomTemp == nextRandomVal || secondRandomTemp == nextRandomVal || firstRandomTemp == nextRandomVal)
		{
		nextRandomVal = rndm.nextInt(length);
		}
		rndmP4 = storeList.get(nextRandomVal).intValue();
                
	}
	
	public int find(int x)
	{
		if(tempArray[firstHashFn(x)] == x || tempArray[secondHashFn(x)] == x)
			return 1;
		else
			return 0;
	}

	public int interchange(int x, int pos)
	{
		int temp=x;
		x=tempArray[pos];
		tempArray[pos]=temp;
		return x;
	}
	
	public void rehash()
	{
		RehashCount++;
		System.out.println("Rehashing");
		hash();
		int size_Local=sizeOfTempArray+1;
		sizeOfTempArray=0;
		int[] TempArray_Local = new int[size_Local];
		int index = 0;
		
		for(int i = 0; i < buckets; i++)
		{
			if(this.tempArray[i] != 0)
			{
				TempArray_Local[index] = this.tempArray[i];
				this.tempArray[i]=0;
				index++;
			}
		}
		for(int j = 0; j < size_Local-1; j++)
		{
			put(TempArray_Local[j]);
		}
		
		return;
		
	}
	
	public int firstHashFn(int x)
	{
		return ((rndmP1*x)+rndmP3)%buckets;
	}
	public int secondHashFn(int x)
	{
		return ((rndmP2*x)+rndmP4)%buckets;
	}
	
	public void put(int x)
	{
		int position=0;
                int loopCntr=0;
		
		if(find(x)==1)
		{
			return;
		}
		else
		{
			position=firstHashFn(x);
			
			while(loopCntr<10)
			{
				if( tempArray[position] == 0 )
				{ 
					tempArray[position]=x;
					sizeOfTempArray++;
					return;
				}
				else
				{
					x = interchange(x,position);
				}
				
				if(position==firstHashFn(x))
				{
					position=secondHashFn(x);
				}
				else
				{
					position=firstHashFn(x);
				}
				loopCntr++;
			}
			rehash();
			put(x);
		}
	}
	
	
}
