import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by G19 on 2/25/16.
 */
public class K_Selection<T> {
    T ele;

    public K_Selection(T element){
        this.ele=element;
    }

    <T extends Comparable<? super T>> K_Selection<T>[] swap(K_Selection<T>[] arr, int from_ind, int to_ind) {
        K_Selection<T> temp=arr[from_ind];
        arr[from_ind]=arr[to_ind];
        arr[to_ind]=temp;
        return arr;
    }

    <T extends Comparable<? super T>> int Partition(K_Selection<T>[] arr, int start, int end){
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

    <T extends Comparable<? super T>> int Select(K_Selection<T>[] arr,int start,int end,int k){
        int q=Partition(arr,start,end);
        if((end-q)>=k)
            return Select(arr,q+1,end,k);
        else if ((end-q+1)==k)
            return q;
        else
            return Select(arr,start,q-1,(k-(end-q+1)));
    }

    <T extends Comparable<? super T>> int K_Largest(K_Selection<T>[] arr,int k){
        return Select(arr, 0, arr.length - 1, k);
    }

    void sortedOutput(K_Selection<T>[] arr, int q){
        System.out.println("Sorted K- Largest Elements are: ");
        for(int i=q;i<arr.length;i++)
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
        System.out.println("K-Largest elements: ");
        int k=in.nextInt();
        System.out.println("Enter the numbers: ");
        K_Selection gen=new K_Selection(1);
        K_Selection<Long>[] arr = new K_Selection[n];
        for(int i=0;i<n;i++)
            arr[i]=new K_Selection(in.nextInt());
        long start_time=System.currentTimeMillis();
        int q=gen.K_Largest(arr, k);
        System.out.println("Time taken to get K-largest elements are: "+(System.currentTimeMillis()-start_time) + "msec");
        gen.sortedOutput(arr,q);
    }
}
