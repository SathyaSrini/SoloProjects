import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by G19 on 2/25/16.
 */
public class MergeVsQuick<T> {
    T ele;
    public MergeVsQuick(T element){
        this.ele=element;
    }

    public static<T extends Comparable<? super T>> void Mergesort(MergeVsQuick<T>[] arr,int min,int mid,int max){
        MergeVsQuick<T>[] s1= Arrays.copyOfRange(arr,min,mid+1);
        MergeVsQuick<T>[] s2= Arrays.copyOfRange(arr, mid + 1, max + 1);

        int f1=0,f2=0,m=0;
        /*edge cases are checked as follows
            where the first sublist is over before the second one is and vice versa     */
        for(int i=min;i<max+1;i++){

            if(f2==s2.length && f1<s1.length)
                arr[i]=s1[f1++];
            else if(f1==s1.length && f2<s2.length)
                arr[i]=s2[f2++];
            else if(s1[f1].ele.compareTo(s2[f2].ele)<0)
                arr[i]=s1[f1++];
            else
                arr[i]=s2[f2++];
        }
    }

     static<T extends Comparable<? super T>> void Merge(MergeVsQuick<T>[] arr,int min,int max){
        if(min<max) {
            int mid=(max+min)/2;
            Merge(arr, min, mid);
            Merge(arr, mid + 1, max);
            Mergesort(arr,min,mid,max);}
     }
    
    <T extends Comparable<? super T>> MergeVsQuick<T>[] swap(MergeVsQuick<T>[] arr, int from_ind, int to_ind) {
        MergeVsQuick<T> temp=arr[from_ind];
        arr[from_ind]=arr[to_ind];
        arr[to_ind]=temp;
        return arr;
    }


    <T extends Comparable<? super T>> void MQuickSort(MergeVsQuick<T>[] arr, int begin, int end){
        if(end<=begin) return;
        int rnd,rnd1;

        rnd =begin;
        rnd1=end;
        if (arr[rnd].ele.compareTo(arr[rnd1].ele)>0)arr=swap(arr, rnd, rnd1);
        int first=begin,last=end,l=begin;
        T x1,x2;

        x1=arr[rnd].ele;x2=arr[rnd1].ele;

        first++;l++;last--;

        while(l<=last){
            if(arr[l].ele.compareTo(arr[first].ele)<0)
                swap(arr,first++,l++);
            else if(arr[last].ele.compareTo(arr[l].ele)<0)
                swap(arr,l,last--);
            else   l++;
        }
        MQuickSort(arr, begin,first-1);
        if(arr[first].ele.compareTo(arr[last].ele)<0) MQuickSort(arr,first+1,last-1);
        MQuickSort(arr,last+1,end);
    }


    void sortedOutput(MergeVsQuick<T>[] arr){
        System.out.println();
        for(int i=0;i<arr.length;i++)
            System.out.println(arr[i].ele.toString());
    }
    public static void main(String[] args) throws FileNotFoundException {
        int n;
        Scanner in;
        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            System.out.println("Since Input file is not mentioned, enter the total number of elements to be sorted:");
            in = new Scanner(System.in);
        }
        n=in.nextInt();
        System.out.println("Enter the numbers: " + n);
        MergeVsQuick gen=new MergeVsQuick(1);
        MergeVsQuick<Long>[] arr = new MergeVsQuick[n];
        for(int i=0;i<n;i++)
            arr[i]=new MergeVsQuick(in.nextInt());
        MergeVsQuick<Long>[] arr1 =arr;

        System.out.println("Quick Sort: ");
        long start_time=System.currentTimeMillis();
        gen.MQuickSort(arr, 0, arr.length-1);
        System.out.println("Time taken for Quick sort algorithm is: "+(System.currentTimeMillis()-start_time)+" milliseconds");
       // gen.sortedOutput(arr);

        System.out.println("Merge Sort: ");
        start_time=System.currentTimeMillis();
        gen.Merge(arr1,0,arr.length-1);
        System.out.println("Time taken for Merge sort algorithm is: "+(System.currentTimeMillis()-start_time)+" milliseconds");
        //gen.sortedOutput(arr1);
    }
}
