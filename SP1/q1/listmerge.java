
import java.io.*;
import java.lang.System;
import java.util.*;
import java.util.LinkedList;

public class listmerge<T>{
    private T ele;

    public listmerge(T add){
        this.ele=add;
    }

    public static<T extends Comparable<? super T>> listmerge<T> next(ListIterator<listmerge<T>> it){
       if(it.hasNext())
        return it.next();
       else
        return null;
    }
    /*Printing of the output list after every operation,st- String to be printed such as "Union/Intersection/Difference"*/
    public static<T extends Comparable<? super T>> void print(List<listmerge<T>> arr, String st) throws IOException {
        File outFile = new File ("output.txt");
        //if file doesnt exists, then create it
        if(!outFile.exists()){
            outFile.createNewFile();
        }
        FileWriter fWriter = new FileWriter (outFile,true);
        PrintWriter pWriter = new PrintWriter (fWriter);
        ListIterator<listmerge<T>> it=arr.listIterator();
        listmerge<T> x=next(it);
        pWriter.println(st);
        while(x!=null){
            pWriter.print(x.ele.toString() + " ");
            x=next(it);}
        pWriter.println();
        pWriter.close();
    }
    /*a= first list, b= second list, out- final output list
    * it, it1, it2 - iterators of a,b and out
    * x1 - value of next item in linkedlist a, x2- value of next item in linkedlist b
    * */
    public static<T extends Comparable<? super T>> void intersection(List<listmerge<T>> a, List<listmerge<T>> b, List<listmerge<T>> out) throws IOException {
        int i=0,j1=0,j2=b.size()-1;
        ListIterator<listmerge<T>> it=a.listIterator();
        ListIterator<listmerge<T>> it1=b.listIterator();
        ListIterator<listmerge<T>> it2=out.listIterator();

        listmerge<T> x1=next(it);listmerge<T> x2=next(it1);
        while(x1!=null && x2!=null){
            if(x1.ele.compareTo(x2.ele)==0)
            { it2.add(x1);
             x1=next(it);x2=next(it1);}
            else if(x1.ele.compareTo(x2.ele)<0)
                x1=next(it);
            else  if(x1.ele.compareTo(x2.ele)>0)
                x2=next(it1);
        }

        print(out,"Intersection of two lists is:");
    }

    /*a= first list, b= second list, out- final output list
    * it, it1, it2 - iterators of a,b and out
    * x1 - value of next item in linkedlist a, x2- value of next item in linkedlist b
    * */
    public static<T extends Comparable<? super T> > void union(List<listmerge<T>> a,List<listmerge<T>> b,List<listmerge<T>> out) throws IOException {
        int i=0,j=0;
        ListIterator<listmerge<T>> it=a.listIterator();
        ListIterator<listmerge<T>> it1=b.listIterator();
        ListIterator<listmerge<T>> it2=out.listIterator();

        listmerge<T> x1=next(it);listmerge<T> x2=next(it1);
        while(x1!=null || x2!=null){
            if(x1==null && x2!=null){
                it2.add(x2);x2=next(it1);}
            else if(x2==null && x1!=null){
                it2.add(x1);x1=next(it);}
            else if(x1.ele.compareTo(x2.ele)<0){
                it2.add(x1);x1=next(it);}
            else if(x1.ele.compareTo(x2.ele) ==0){
                it2.add(x2);x2=next(it1);x1=next(it);}
            else {it2.add(x2);x2=next(it1);}
        }
        print(out,"Union of two lists is:");
    }

    /*a= first list, b= second list, out- final output list
    * it, it1, it2 - iterators of a,b and out
    * x1 - value of next item in linkedlist a, x2- value of next item in linkedlist b
    * */
    public static<T extends Comparable<? super T> > void difference(List<listmerge<T>> a,List<listmerge<T>> b,List<listmerge<T>> out) throws IOException {
        int i=0,j=0;
        ListIterator<listmerge<T>> it=a.listIterator();
        ListIterator<listmerge<T>> it1=b.listIterator();
        ListIterator<listmerge<T>> it2=out.listIterator();

        listmerge<T> x1=next(it);listmerge<T> x2=next(it1);
        while(x1!=null){
            if(x2!=null && x1.ele.compareTo(x2.ele)<0)
            {  it2.add(x1);x1=next(it);}
            else if(x2!=null && x1.ele.compareTo(x2.ele)>0) x2=next(it1);
            else if(x2==null) {it2.add(x1);x1=next(it1);}
            else {x2=next(it1);x1=next(it);}
        }
        print(out,"Difference of two lists is:");
    }
    /*in1- Scanner of the entire input file, in- Scanner of every line, both lists are given in a single file.
    * l1- list1,l2-list2, out- output file, arr- buffer list to copy values to l1 and l2.
    * fg- flag variable used to check only same type of input is given for l1 and l2 and also within l1/l2.
    *  ie; if input like 1 2 3 hello is given, it ignores hello.
    * */
    public static<T extends Comparable<T>> void main(String[] args) throws IOException {
        LinkedList<listmerge<T>> l1=new LinkedList<listmerge<T>>();
        LinkedList<listmerge<T>> l2=new LinkedList<listmerge<T>>();
        LinkedList<listmerge<T>> out= new LinkedList<listmerge<T>>();
        LinkedList<listmerge<T>> out1= new LinkedList<listmerge<T>>();
        LinkedList<listmerge<T>> out2= new LinkedList<listmerge<T>>();
        long start_time=0,end_time=0;
        if(new File("output.txt").exists())
            new File("output.txt").delete();

        Scanner in1;
        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in1 = new Scanner(inputFile);
        } else {
            System.out.println("Since no input file is mentioned, Give the input as : a <space> b <space> and so on for list 1 and press enter and repeat the same for list 2");
            in1 = new Scanner(System.in);
        }
        int flag=0,fg=0,i=0;
        LinkedList<listmerge<T>> arr= l1;
        while(i<=1 && in1.hasNextLine() ){
            String nextLine= in1.nextLine();
            Scanner in = new Scanner(nextLine);
            while(in.hasNextInt()&&(fg==0 || fg==1)){
                arr.add(new listmerge(in.nextInt()));fg=1;}
            while(in.hasNextBigDecimal() &&(fg==0 || fg==2)){
                arr.add(new listmerge(in.nextBigDecimal()));fg=2;}
            while(in.hasNextBigInteger() &&(fg==0 || fg==3)){
                arr.add(new listmerge(in.nextBigInteger()));fg=3;}
            while(in.hasNextDouble() &&(fg==0 || fg==4)){
                arr.add(new listmerge(in.nextDouble()));fg=4;}
            while (in.hasNextFloat() &&(fg==0 || fg==5)){
                arr.add(new listmerge(in.nextFloat()));fg=5;}
            while (in.hasNextLong() &&(fg==0 || fg==6)){
                arr.add(new listmerge(in.nextLong()));fg=6;}
            while (in.hasNextShort()&&(fg==0 || fg==7)){
                arr.add(new listmerge(in.nextShort()));fg=7;}
            while(in.hasNextByte()&&(fg==0 || fg==8)){
                arr.add(new listmerge(in.nextByte()));fg=8;}
            while(in.hasNext() &&(fg==0 || fg==9)){
                arr.add(new listmerge(in.next()));fg=9;}
            if(flag==0) {l1=arr;flag=1;arr=l2;}
            i++;
        }
        l2=arr;

        if(l1.size()>=1 && l2.size()>=1){
            print(l1,"List A");print(l2,"List B");
            start_time=System.currentTimeMillis();
            if (l1.size()<l2.size()) intersection(l1,l2,out);
            else intersection(l2,l1,out);
            union(l1,l2,out1);
            difference(l1,l2,out2);
            end_time=System.currentTimeMillis();
        }
        else
            System.out.println("One of the list seems to be empty");
        System.out.print("The total amount of time taken is:"+(end_time-start_time));

        System.out.println("Check the output file you mentioned to see the results");

    }
}

