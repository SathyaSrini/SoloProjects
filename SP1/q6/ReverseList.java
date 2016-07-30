public class ReverseList<T> {
	
	private final SinglyLinkedList<T> linkedList;
	
	public ReverseList(SinglyLinkedList<T> linkedList) {
		if(linkedList == null)
		{
			this.linkedList = new SinglyLinkedList<>();
		}
		else
		{
			this.linkedList = linkedList;
		}
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
	 * Reverse the given linked list without recursion.
	 * @param print - Whether to print the original order of the list
	 * @param output - output string
	 */
	private void reverseListNonRecur(final boolean print, final StringBuffer output)
	{
		SinglyLinkedList<T>.Entry<T> prev = next(linkedList.header);		
		SinglyLinkedList<T>.Entry<T> cur = null; 
		
		//Invariant: prev points to the last reversed node in the list
		//cur points to the node that need to be reversed
		
		if(prev != null)
		{
			if(print)//If print enabled, print the first element
			{
				output.append(prev.element+" ");
			}
			cur = next(prev);
			prev.next = null;
		}				
		linkedList.tail = cur;
		while(cur != null)
		{
			if(print)//If print enabled, print the current element
			{
				output.append(cur.element+" ");
			}
			final SinglyLinkedList<T>.Entry<T> temp = next(cur);			
			cur.next = prev;
			prev = cur;
			cur = temp;
		}
		linkedList.header.next = prev;
	}
	
	/**
	 * Wrapper for the actual reverse linked list non recursively function.
	 */
	public void reverseListNonRecur()
	{
		reverseListNonRecur(false, null);
	}
	
	/**
	 * Reverse the given linked list from the given current position
	 * @param currentNode - Node of the linked list from which the list should be reversed
	 * @param nextNode - Next node of the current node in the list 
	 */
	private void reverseList(final SinglyLinkedList<T>.Entry<T> currentNode,
									final SinglyLinkedList<T>.Entry<T> nextNode)
	{
		if(currentNode != null && 
				nextNode != null)
		{			
			reverseList(nextNode, nextNode.next);
			if(currentNode.element != null)
				nextNode.next = currentNode;
			else
				nextNode.next = null;
		}
	}
	
	/**
	 * Reverse the given linked list recursively
	 * @param linkedList - Linked list to be reversed
	 */
	public void reverseListRecur()
	{
		if(linkedList != null && 
			linkedList.header != null && 
			linkedList.header.next != null)
		{
			reverseList(linkedList.header, linkedList.header.next);
			final SinglyLinkedList<T>.Entry<T> temp = linkedList.tail;
			linkedList.tail = linkedList.header.next;
			linkedList.header.next= temp;
		}
	}
	
	/**
	 * Print the nodes in the reverse order from the given node recursively.
	 * @param node - node in the linked list
	 * @param output - output string
	 */
	private void reversePrint(final SinglyLinkedList<T>.Entry<T> node,
										final StringBuffer output)
	{
		if(node != null)
		{
			reversePrint(node.next, output);
			output.append(node.element+ " ");
		}
	}		
	
	/**
	 * Print the elements in the given linked list in reverse order recursively
	 * @param linkedList - Linked list to be printed in reverse order
	 */
	public void printReverseRecur()
	{
		if(linkedList != null)
		{
			final StringBuffer output = new StringBuffer();
			reversePrint(linkedList.header.next, output);
			if(linkedList.size > 20)
			{
				Util.printOutput("output.txt", output.toString());
			}
			else
			{
				System.out.println(output);
			}
		}
	}
	
	/**
	 * Print the elements in the given linked list in reverse order non recursively
	 * @param linkedList - Linked list to be printed in reverse order
	 */
	public void printReverseNonRecur()
	{
		if(linkedList != null)
		{
			final StringBuffer output = new StringBuffer();
			reverseListNonRecur(); // Reverse the linked list
			reverseListNonRecur(true, output); // Reverse it again with printing the current structure
			if(linkedList.size > 20)
			{
				Util.printOutput("output.txt", output.toString());
			}
			else
			{
				System.out.println(output);
			}
		}
	}
	
	/**
	 * Get the Singly Linked List
	 * @return - Singly linked list
	 */
	public SinglyLinkedList<T> getLinkedList() {
		return linkedList;
	}
	

	public static void main(String[] args) {
		if(args == null || args.length < 2)
		{
			throw new IllegalArgumentException("Not enough number of arguments passed"
					+ "\nUsage: ReverseList <option number> <Input size> \n"
					+ "Example: ReverseList 1 1000000 \n"
					+ "Option Number:\n"
					+ "1. Reverse List Recursively\n"
					+ "2. Reverse List Non-Recursively\n"
					+ "3. Print List in Reverse Order Recursively\n"
					+ "4. Print List in Reverse Order Non-Recursively\n");		
		}
		if(Integer.parseInt(args[0]) < 1 || 
				Integer.parseInt(args[0]) > 4)
		{
			//If the option number is not valid
			throw new IllegalArgumentException("Option number can be 1 or 2 or 3 or 4");
		}
		if(Integer.parseInt(args[1]) < 0)
		{
			//If the max limit for the array element is negative
			throw new IllegalArgumentException("Input size can take only positive values");
		}
		final SinglyLinkedList<Integer> linkedList = new SinglyLinkedList<>();
		final int inputSize = Integer.parseInt(args[1]);
		for(int i =1;i <= inputSize; i++)
		{
			linkedList.add(i);
		}
		ReverseList<Integer> reverseList = new ReverseList<>(linkedList); 
		switch(Integer.parseInt(args[0]))
		{
			case 1:
				System.out.println("First 10 elements before:"+reverseList.getLinkedList().toString());
				reverseList.reverseListRecur();
				System.out.println("First 10 elements after:"+reverseList.getLinkedList().toString());
				break;
			case 2:
				System.out.println("First 10 elements before:"+reverseList.getLinkedList().toString());
				reverseList.reverseListNonRecur();
				System.out.println("First 10 elements after:"+reverseList.getLinkedList().toString());
				break;
			case 3:
				reverseList.printReverseRecur();
				break;
			case 4:
				reverseList.printReverseNonRecur();
				break;			
		}
	}

}
