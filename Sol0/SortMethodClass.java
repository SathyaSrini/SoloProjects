import java.lang.reflect.Array;
import java.util.List;
import java.util.PriorityQueue;

public class SortMethodClass {
	
	// Heap Sort
	public static<T extends Comparable<? super T>> void heapSort(List<T> randomList)
	{
		PriorityQueue<T> heap = new PriorityQueue<T>(randomList);
		randomList.clear();
		while(!heap.isEmpty())
		{
			randomList.add(heap.poll());			
		}
	}
	
	// Merge Sort
	
		public static<T extends Comparable<? super T>> void mergeSort(T[] array, int leftPtr, int rightPtr)
	{
		if(leftPtr < rightPtr)
		{			
			int centerPtr = (rightPtr + leftPtr)/2;
			mergeSort(array, leftPtr, centerPtr);
			mergeSort(array, centerPtr+1, rightPtr);			
			merge(array, leftPtr, centerPtr, rightPtr);
		}
	}

	private static<T extends Comparable<? super T>> void merge(T[] array, int leftPtr, int centerPtr, int rightPtr)
	{
		Class type = array.getClass().getComponentType();
		T[] leftArr = (T[])Array.newInstance(type, centerPtr-leftPtr+1);
		T[] rightArr = (T[])Array.newInstance(type, rightPtr-centerPtr);
		System.arraycopy(array, leftPtr, leftArr, 0, centerPtr-leftPtr+1);
		System.arraycopy(array, centerPtr+1, rightArr, 0, rightPtr-centerPtr);

		int leftPointer = 0;
		int rightPointer = 0;
		int currentPtr = leftPtr;

		while(leftPointer < leftArr.length && rightPointer < rightArr.length)
		{
			int compareto = leftArr[leftPointer].compareTo(rightArr[rightPointer]);
			if(compareto <= 0)
			{
				array[currentPtr] = leftArr[leftPointer];				
				leftPointer++;
			}
			else
			{
				array[currentPtr] = rightArr[rightPointer];
				rightPointer++;
			}
			currentPtr++;
		}

		while(leftPointer < leftArr.length)
		{
			array[currentPtr] = leftArr[leftPointer];
			leftPointer++;
			currentPtr++;
		}		
	}

	public static void main(String[] args) {
		

	}

}
