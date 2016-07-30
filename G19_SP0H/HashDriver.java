import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException; 
import java.util.Scanner;


public class HashDriver {

	public static void main(String[] args) throws FileNotFoundException 
	{
                
		long strtTime, endTime, cuckooTimer=0, normalTimer=0;
		int totalValsToHash;
		System.out.print("Size of Values to Hash:  ");
		Scanner input = new Scanner(System.in);
		int size = input.nextInt();
		
		CuckooHashing CH = new CuckooHashing(size);
		CH.hash();
		
		NormalHash SH=new NormalHash(size);
		SH.hash();
		
		if (args.length > 0) {
		    File inputFile = new File(args[0]);
		    input = new Scanner(inputFile);
		} 
		else {
		    System.out.println("\n Input file not provided, enter "+size+" values: \n");	
		    input = new Scanner(System.in);
		}
		
		for(int i = 0; i < size; i++)
		{
			totalValsToHash=input.nextInt();
			strtTime = System.currentTimeMillis();
			CH.put(totalValsToHash);
			endTime = System.currentTimeMillis();
			cuckooTimer=cuckooTimer+(endTime - strtTime);
			
			strtTime = System.currentTimeMillis();
			SH.put(totalValsToHash);
			endTime = System.currentTimeMillis();
			normalTimer=normalTimer+(endTime - strtTime);
			
		}
				
		
		System.out.println("Time to hash "+size+" values"+"\n Cuckoo Hash: "+cuckooTimer+"milliseconds \n Normal Hash: "+normalTimer+" milliseconds ");
		System.out.println("Rehash # : "+CH.RehashCount);
	}
}
