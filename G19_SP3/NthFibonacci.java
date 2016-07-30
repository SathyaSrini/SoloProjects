/**
 * Created by G19 on 2/22/16.
 */
import java.util.*;
public class NthFibonacci {
    static Scanner in=new Scanner(System.in);

    static long matMultiply(long[][] temp,int i,int j,long[][] orig){
        return (temp[i][0]*orig[0][j])+(temp[i][1]*orig[1][j]);
    }
    static long[][] matMultiplication(long[][] arr,long[][] orig){
        long[][] temp=new long[2][2];
        temp[0][0]=matMultiply(arr,0,0,orig);
        temp[0][1]=matMultiply(arr,0,1,orig);
        temp[1][0]=matMultiply(arr,1,0,orig);
        temp[1][1]=matMultiply(arr,1,1,orig);
        return temp;
    }
    static long[][] logNFibonacci(long[][] arr,long n,long[][] orig){
        if(n==0)
            return arr;
        else if(n==1)
            return arr;
        else{
            arr=logNFibonacci(arr,n/2,orig);
            arr=matMultiplication(arr,arr);
            if(n%2!=0)
               return matMultiplication(arr,orig);
        }
        return arr;
    }

    static long nFibonacci(long n){
        long x1=1,x2=1,x3=0;
        for(long i=2;i<n;i++)
        {
            x3=x2+x1;
            x1=x2;
            x2=x3;
        }
/*        if(n==1 || n==0)
            return n;
        else
            return nFibonacci(n-1)+nFibonacci(n-2);*/
        return x3;
    }

    public static void main(String[] args){
        long n;
        System.out.println("Enter the nth number for finding Fibonacci: ");
        n=in.nextLong();
        long[][] arr=new long[2][2];
        arr[0][0]=1;arr[0][1]=1;
        arr[1][0]=1;arr[1][1]=0;
        long start_time=System.currentTimeMillis();
        System.out.println("Nth Fibonacci is using logN: "+logNFibonacci(arr,n-1,arr)[0][0]%999953+" and time taken is: "+(System.currentTimeMillis()-start_time)+"msec");
        start_time=System.currentTimeMillis();
        System.out.println("Nth Fibonacci using n algorithm: "+nFibonacci(n)%999953+" and time taken is: "+(System.currentTimeMillis()-start_time)+"msec");
    }
}
