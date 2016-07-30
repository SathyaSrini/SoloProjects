import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by G19 on 2/25/16.
 */
public class KQuantiles<T> {
    T ele;
    public int index=0 ;
    public KQuantiles(T element){
        this.ele=element;
    }

    <T extends Comparable<? super T>> KQuantiles<T>[] swap(KQuantiles<T>[] arr, int from_ind, int to_ind) {
        KQuantiles<T> temp=arr[from_ind];
        arr[from_ind]=arr[to_ind];
        arr[to_ind]=temp;
        return arr;
    }

    <T extends Comparable<? super T>> KQuantiles<T>[] Partition(KQuantiles<T>[] arr, int start, int end){
       /* int rnd;
        if (end>0)
            rnd= new Random().nextInt(end);
        else rnd=0;*/
        T x=arr[0].ele;
        int begin=start,last=end;
        while (begin<end){
            if(arr[begin].ele.compareTo(x)<0)begin++;
            else if(arr[end].ele.compareTo(x)>0) end--;
            else arr=swap(arr,begin,end);
        }
        return arr;
    }

    <T extends Comparable<? super T>> int Partition1(KQuantiles<T>[] arr, int start, int end){
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

    <T extends Comparable<? super T>> int Select(KQuantiles<T>[] arr,int start,int end,int k){
        int q=Partition1(arr, start, end);
        if((end-q)>=k)
            return Select(arr,q+1,end,k);
        else if ((end-q+1)==k)
            return q;
        else
            return Select(arr,start,q-1,(k-(end-q+1)));
    }

    <T extends Comparable<? super T>> int Quantiles(KQuantiles<T>[] arr,int k,ArrayList<KQuantiles<T>> answer){
        if(k==1 || k==0) return arr.length-1;
        else {
            int n=arr.length,i=k/2;
             if(k%2==1) i=k/2+1;
            else i=k/2;
                    int x=Select(arr,0,arr.length-1,i*(n/k));
            arr=Partition(arr,0, x);
//            System.out.println();
//            for(KQuantiles m:arr)
  //              System.out.println(m.ele);
            answer.add(arr[Quantiles(Arrays.copyOfRange(arr, 0, (i * n / k)), (k / 2), answer)]);
            answer.add(arr[Quantiles(Arrays.copyOfRange(arr, (i * (n / k)) + 1, n), (int) Math.ceil(k / 2), answer)]);
            return x;
        }
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
        KQuantiles gen=new KQuantiles(1);
        KQuantiles<Long>[] arr = new KQuantiles[n];
        for(int i=0;i<n;i++)
            arr[i]=new KQuantiles(in.nextInt());
        ArrayList<KQuantiles<Long>> answer= new ArrayList<KQuantiles<Long>>();
        gen.Quantiles(arr, k, answer);
        System.out.println();
        ArrayList<KQuantiles<Long>> ans= new ArrayList<KQuantiles<Long>>();

        for(KQuantiles i:answer)
            if(!ans.contains(i))
                   ans.add(i);
        if(k%2==0) ans.remove(ans.size()-1);
        for(KQuantiles i:ans)
            System.out.println(i.ele);
    }
}
