import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class MainDriver 
{

    // Print Method	
	public static<T> void print(Iterator<T> input)
	{
		int index = 0;
		
		while(index < 10 && input.hasNext())
		{
			System.out.print(input.next()+" ");
			index++;
		}
		System.out.println();
	}
	
	// Generate Random Numbers
	
		private static Integer[] generateRandomIntegers(int count, int count_limit)
	{
		Integer[] randomInput = new Integer[count];
		Random rndm = new Random();
		int index = 0;
		
		while(index < count)
		{
			randomInput[index] = rndm.nextInt(count_limit);
			index++;
		}
		return randomInput;
	}	
		
	public static void main(String[] args) 
	{
		
		long start;
		long end;
		int option;
		long duration;
		
		if(args == null || args.length < 2)
		{
			System.out.println("<Merge/Heap> <Input size>");
			System.out.println("Eg : Merge 2000000");
			System.exit(-1);
		}
		
		option = Integer.parseInt(args[1]);
		Integer[] array = generateRandomIntegers(option,Integer.MAX_VALUE); 
		
		if(args[0].equalsIgnoreCase("Heap"))
		{
			ArrayList<Integer> randomList = new ArrayList<Integer>(Arrays.asList(array));
			start = System.currentTimeMillis();
			SortMethodClass.heapSort(randomList);
			end = System.currentTimeMillis();
			print(randomList.iterator());
			duration = end - start;
			System.out.println("Heap sort takes :"+duration);

			
		}
		else
		{
			start = System.currentTimeMillis();
			SortMethodClass.mergeSort(array,0, array.length-1);
			end = System.currentTimeMillis();			
			print(Arrays.asList(array).iterator());
			duration = end - start;
			System.out.println("Time taken for merge sort :"+duration);			

		}
	}

}
