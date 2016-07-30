import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class SimulateRecursion {
	
	/**
	 * Set the states of the operation to be performed. 
	 * @param state - processing state of the array
	 * @param left - starting position in the array being processed
	 * @param right - ending position in the array being processed
	 * @return
	 */
	private static Integer[] setState(int state, int left, int right)
	{
		final Integer[] sortState = new Integer[3];
		sortState[0] = state;
		sortState[1] = left;
		sortState[2] = right;
		
		return sortState;
	}
	
	/**
	 * Generates an Integer array of given size and values between 0 & given limit
	 * @param size - Size of the array to be generated
	 * @param limit - Max value for the numbers in the array
	 * @return - An Integer array of random numbers
	 * @throws IllegalArgumentException
	 */
	private static Integer[] getRandomIntegerArray(int size, int limit) 
		throws IllegalArgumentException
	{
		if(size < 0 || limit < 0)
		{
			throw new IllegalArgumentException("Both size and limit can take only positive values");
		}
		Integer[] randomArray = new Integer[size];
		Random randomGenerator = new Random();
		int count = 0;
		while(count < size)
		{
			randomArray[count] = randomGenerator.nextInt(limit);
			count++;
		}
		return randomArray;
	}	
	
	/**
	 * Sort the given generic array which supports Comparable apis using Merge Sort
	 * @param array - The array to be sorted
	 * @param left - Beginning index of the array to be sorted
	 * @param right - Last index of the array to be sorted
	 */
	public static<T extends Comparable<? super T>> void mergeSort(T[] array, 
			int left, 
			int right)
	{
		Deque<Integer[]> stack = new ArrayDeque<Integer[]>();
		
		Integer[] sortState = setState(0,left,right);		
		stack.push(sortState);
		
		//Invariants: stack contains the processing state,starting index
		//and ending index of each sub array being processed.
		//Processing states can be either 0 or 1
		//0 - Indicates that the sub-array in the current range is not further divided 
		//for processing
		//1 - Indicates that all children of the current range is processed
		
		while(!stack.isEmpty())
		{
			final Integer[] top = stack.peekFirst();			
			left = top[1];
			right = top[2];
			if(left < right)
			{
				final int center = (right + left)/2; 
				switch(top[0])
				{
					case 0:
						Integer[] rightBranch = setState(0, center+1, right);					
						Integer[] leftBranch = setState(0, left, center);
						stack.getFirst()[0] = 1;
						stack.push(rightBranch);
						stack.push(leftBranch);					
						break;
					case 1:
						stack.pop();
						merge(array, left, center, right);					
						break;
				}
			}
			else
			{
				stack.pop();
			}
		}
	}
	
	/**
	 * Merges two sorted contiguous partitions of an array in to one sorted partition. 
	 * @param array - The array to be sorted
	 * @param left - Beginning index of the first partition of the array to be sorted
	 * @param center - Last index of the first partition of the array to be sorted
	 * @param right - Last index of the second partition of the array to be sorted
	 */
	private static<T extends Comparable<? super T>> void merge(T[] array, 
			int left, 
			int center, 
			int right)
	{
		Class type = array.getClass().getComponentType();
		T[] leftArray = (T[])Array.newInstance(type, center-left+1);
		T[] rightArray = (T[])Array.newInstance(type, right-center);
		System.arraycopy(array, left, leftArray, 0, center-left+1);
		System.arraycopy(array, center+1, rightArray, 0, right-center);

		int leftPointer = 0;
		int rightPointer = 0;
		int currentPointer = left;
		
		//Invariants: leftPointer - starting position of the array 1
		//rightPointer - starting position of the array 2
		//currentPointer - Current position in the original array where elements go in order

		while(leftPointer < leftArray.length && rightPointer < rightArray.length)
		{
			int compareto = leftArray[leftPointer].compareTo(rightArray[rightPointer]);
			if(compareto <= 0)
			{
				array[currentPointer] = leftArray[leftPointer];				
				leftPointer++;
			}
			else
			{
				array[currentPointer] = rightArray[rightPointer];
				rightPointer++;
			}
			currentPointer++;
		}

		while(leftPointer < leftArray.length)
		{
			array[currentPointer] = leftArray[leftPointer];
			leftPointer++;
			currentPointer++;
		}		
	}
	
	/**
	 * Prints the top ten elements from the given Iterator object.
	 * @param arrayToPrint - Array to be printed
	 */
	private static<T> void printTopTen(final T[] arrayToPrint)
	{
		int count = 0;
		while(count < 10 && count < arrayToPrint.length)
		{
			System.out.print(arrayToPrint[count]+" ");
			count++;
		}
		System.out.println();
	}
	

	public static void main(String[] args) {
		if(args == null || args.length ==0)
		{
			throw new IllegalArgumentException("Not enough number of arguments passed"
					+ "\nUsage: SimulateRecursion <Input size> \n"
					+ "Example: SimulateRecursion 1000000");		
		}
		if(Integer.parseInt(args[0]) < 0)
		{
			//If the max limit for the array element is negative
			throw new IllegalArgumentException("Input size can take only positive values");
		}
		
		Integer[] array = getRandomIntegerArray(Integer.parseInt(args[0]), 
												Integer.MAX_VALUE);
		System.out.println("Unsorted array:");
		printTopTen(array);
		long startTime = System.currentTimeMillis();
		mergeSort(array, 0, array.length-1);
		long endTime = System.currentTimeMillis();
		System.out.println("Sorted array:");
		printTopTen(array);
		System.out.println("Time taken for merge sort :"+(endTime - startTime));
	}

}
