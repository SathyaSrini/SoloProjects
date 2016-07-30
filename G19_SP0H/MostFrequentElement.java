import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

public class MostFrequentElement {

	public static void main(String[] args) {
		
		System.out.println("input size:");
		Scanner s = new Scanner(System.in);
		int size = s.nextInt();
		int[] arr = new int[size];
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			int num = random.nextInt(1000);
			arr[i] = num;		
		}

		long start1 = System.currentTimeMillis();
		System.out.println("Most Frequent Element found by O(n) algorithm"+mostFrequent(arr));
		long end1 = System.currentTimeMillis();
		
		//O(nlogn) algorithm
		long start2 = System.currentTimeMillis();
		Arrays.sort(arr);
		int count = 1,temp=1,element=arr[0];
		for(int i=0;i<arr.length-1;i++){
			if(arr[i]==arr[i+1])
				temp++;
			else
				temp=1;
			if(temp>count){
				count = temp;
				 element = arr[i];
			}
		}
		System.out.println("Most Frequent Element found by O(nlogn) algorithm"+element);
		long end2 = System.currentTimeMillis();
		System.out.println("Time taken by O(n) algorithm:"+(end1-start1));
		System.out.println("Time taken by O(nlogn) algorithm:"+(end2-start2));
	}

	public static int mostFrequent(int[] a) {
		
		HashMap<Integer,Integer> hfreq = new HashMap<Integer,Integer>();
		for(int i=0;i<a.length;i++){
			
			if(!hfreq.containsKey(Integer.valueOf(a[i]))){
				hfreq.put(Integer.valueOf(a[i]), 1);
			}
			else
				hfreq.put(Integer.valueOf(a[i]),hfreq.get(a[i])+1);			
		}
		
		int temp = Integer.valueOf(1);
		int element = Integer.valueOf(a[0]);
		Iterator<Entry<Integer, Integer>> it = hfreq.entrySet().iterator();
		while(it.hasNext()){
			int key = it.next().getKey();
			if(hfreq.get(key)>temp)
			{
				temp = hfreq.get(key);
				element = key;
			}
		}
		return element;
	}
	
	
}
