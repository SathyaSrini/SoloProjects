import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RemoveDuplicates {
	
	public static void main(String[] args) {
		Student[] s  = new Student[7];
		 s[0] = new Student("c",3);
		 s[1] = new Student("a",1);
		 s[2] = new Student("c",3);
		 s[3] = new Student("b",2);
		 s[4] = new Student("b",2);
		 s[5] = new Student("a",1);
		 s[6] = new Student("c",3);
		System.out.println(findDistinct(s));
	}
 
	public static <T> int findDistinct(T[] arr){
		Set<T> m = new HashSet<T>();
		int j=0;            //pointer that moves only if the value arr[i] is not existing already in the array
		for(int i=0;i<arr.length;){
			if(!m.contains(arr[i])){
				m.add(arr[i]);
				if(i!=j){
					T temp = arr[j];
					arr[j] = arr[i];
					arr[i] = temp;
				}
				j++;
			}
			i++;
		}	
		return j;
	}
}
class Student{
	String name;
	int id;
	
	Student(String name,int id){
		this.name = name;
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}