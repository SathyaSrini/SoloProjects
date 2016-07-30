import java.lang.reflect.Array;

public class Zipper<T> {
	private final SinglyLinkedList<T> linkedList;		
	
	public Zipper(SinglyLinkedList<T> linkedList) {
		if(linkedList != null)
		{
			this.linkedList = linkedList;
		}
		else
		{
			this.linkedList = new SinglyLinkedList<>();
		}
	}	

	public SinglyLinkedList<T> getLinkedList() {
		return linkedList;
	}
	
	/**
	 * Get the next node from the provided linked list.
	 * @param node - node whose next node need to be found
	 * @return - the next node of the given node
	 */
	public SinglyLinkedList<T>.Entry<T> next(SinglyLinkedList<T>.Entry<T> node)
    {
    	if(node != null && node.next != null)
    	{
    		return node.next;
    	}
    	else
    	{
    		return null;
    	}
    }

	/**
	 * Unzip the linked list by combining elements at k distances apart.
	 * @param k - Current distance between elements to join together. 
	 */
	public void multiUnzip(int k)
	{
		if(k >= this.linkedList.size || k <= 0)
		{
			return;
		}
		Class type = this.linkedList.getClass();
		SinglyLinkedList<T>[] states = (SinglyLinkedList<T>[]) Array.newInstance(type, k);
		
		//Invariant: each element in states contains the list that need to be chained 
		//together in the final list. 
		
		for(int i=0;i<k;i++)
		{
			states[i] = new SinglyLinkedList<>();			
		}
		
		SinglyLinkedList<T>.Entry<T> node = this.linkedList.header.next;
		int stateIndex = 0;
		while(node != null)
		{
			states[stateIndex].append(node);
			node = next(node);
			stateIndex = (stateIndex+1)%k;
		}
	
		//Connect all individual lists in to a single complete list by connecting 
		//each tail to the first valid node of the list in the next state.
		for(int i=0;i<k-1;i++)
		{
			states[i].tail.next = states[i+1].header.next;
		}		
		
		this.linkedList.header.next = states[0].header.next;
		this.linkedList.tail = states[k-1].tail;
		this.linkedList.tail.next = null;
	}


	public static void main(String[] args) {
		
		if(args == null || args.length < 2)
		{
			throw new IllegalArgumentException("Not enough number of arguments passed"
					+ "\nUsage: Zipper <k value> <Input size> \n"
					+ "Example: Zipper 3 1000000 \n");		
		}		
		if(Integer.parseInt(args[1]) < 0)
		{
			//If the max limit for the array element is negative
			throw new IllegalArgumentException("Input size can take only positive values");
		}
		if(Integer.parseInt(args[0]) < 0)
		{
			//If the max limit for the array element is negative
			throw new IllegalArgumentException("k value cannot be negative");
		}
		
		
		final SinglyLinkedList<Integer> linkedList = new SinglyLinkedList<>();
		final int inputSize = Integer.parseInt(args[1]);
		for(int i =1;i <= inputSize; i++)
		{
			linkedList.add(i);
		}
		
		Zipper<Integer> zipper = new Zipper<>(linkedList);
		System.out.println("First 10 elements before:"+zipper.getLinkedList().toString());
		zipper.multiUnzip(Integer.parseInt(args[0]));
		System.out.println("First 10 elements after:"+zipper.getLinkedList().toString());
	}

}
