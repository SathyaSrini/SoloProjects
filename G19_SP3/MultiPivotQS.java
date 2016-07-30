/**
 * Created by G19 on 2/23/16.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class MultiPivotQS<T> {
    T ele;
    public MultiPivotQS(T element){
        this.ele=element;
    }

    <T extends Comparable<? super T>> MultiPivotQS<T>[] swap(MultiPivotQS<T>[] arr, int from_ind, int to_ind) {
        MultiPivotQS<T> temp=arr[from_ind];
        arr[from_ind]=arr[to_ind];
        arr[to_ind]=temp;
        return arr;
    }
    <T extends Comparable<? super T>> int Partition(MultiPivotQS<T>[] arr, int start, int end){
        int rnd;
        if (end>0)
                rnd= new Random().nextInt(end);
        else rnd=0;
        T x=arr[rnd].ele;
        int begin=start,last=end;
        while (begin<end){
            if(arr[begin].ele.compareTo(x)<0)begin++;
            else if(arr[end].ele.compareTo(x)>0) end--;
            else arr=swap(arr,begin,end);
        }
        return end;
    }

    <T extends Comparable<? super T>> void QuickSort(MultiPivotQS<T>[] arr, int begin, int end){
        if(begin<(end-1)){
            int middle=Partition(arr,begin,end-1);
            QuickSort(arr,begin,middle);
            QuickSort(arr,middle+1,end);
        }
    }

    <T extends Comparable<? super T>> void MQuickSort(MultiPivotQS<T>[] arr, int begin, int end){
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
    void sortedOutput(MultiPivotQS<T>[] arr){
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
        System.out.println("Enter the numbers: "+n);
        MultiPivotQS gen=new MultiPivotQS(1);
        MultiPivotQS<Long>[] arr = new MultiPivotQS[n];
        for(int i=0;i<n;i++)
            arr[i]=new MultiPivotQS(in.nextInt());
        MultiPivotQS<Long>[] arr1 = arr;

        System.out.println("Quick Sort: ");
        long start_time=System.currentTimeMillis();
        gen.QuickSort(arr, 0, arr.length);
        System.out.println("The time taken for single pivot quick sort is: " + (System.currentTimeMillis() - start_time) +"msec");
       // gen.sortedOutput(arr);

        start_time=System.currentTimeMillis();
        gen.MQuickSort(arr1, 0, arr.length-1);
        System.out.println("The time taken for dual pivot quick sort is: "+(System.currentTimeMillis()-start_time) + "msec");
//        gen.sortedOutput(arr1);
    }
}
