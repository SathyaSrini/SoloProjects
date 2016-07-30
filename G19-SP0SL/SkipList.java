/**
 * Created by SathyaSrini on 3/29/2016.
 */

import java.lang.*;
import java.util.*;
import java.util.concurrent.*;


public class SkipList<T extends Comparable<? super T>>
{


    // =========================== Declarations ======================================== //
    private SkipListNode<T> head; // defines the head of the SkipList
    private static int total_size = 0; // defines the current size of SkipList
    private static int Level_Max = 25; // defines the max size of the next[]

    public SkipList() {
        head = new SkipListNode<>(null, Level_Max); // creating the head of skiplist with data = null and next[] size as "Level_Max"
        total_size = 0;                               // setting size of new skiplist as 0
    }
    // =========================== Declarations =========================================== //


    //==================================== Inline Classes ================================= //

    // SkipListNode class //

    private class SkipListNode<T extends Comparable<? super T>> {
        private T data; //Data in current node
        private SkipListNode<T>[] next; // Array of next node pointers

        SkipListNode(T x, int level)
        {
            data = x; // initializing data = x
            next = (SkipListNode<T>[]) new SkipListNode[level + 1]; //Initializing the next pointer array to size of Level_Max+1
        }


        private SkipListNode<T>[] getNext()
        {
            return next;
        }

        private int level()
        {
            return next.length - 1;
        }

        private T getData() {
            return data;
        }
    }
    // SkipListNode class //


    // SetFindResult class //
    private class SetFindResult<T extends Comparable<? super T>> //// Result of The Find Operation is set in this class  
    {
        private SkipListNode<T> node;
        private SkipListNode<T>[] prev; // array of prev[] pointers

        public SetFindResult(SkipListNode<T> node, SkipListNode<T>[] prev) {
            super();
            this.node = node;
            this.prev = prev;
        }

        public SkipListNode<T> getNode() {
            return node;
        }

        public SkipListNode<T>[] getPrev() {
            return prev;
        }


    }
    // SetFindResult class //

    // ==================================== Inline Classes ================================= //

    // ================================ Helper Methods ===================================== //

    public SetFindResult<T> find(T x){
        SkipListNode<T> temp = head;
        SkipListNode<T>[] prev = (SkipListNode<T>[]) new SkipListNode[Level_Max + 1];
        for (int level = head.level(); level >= 0; level--) {
            while (temp.getNext()[level] != null && x.compareTo(temp.getNext()[level].getData()) > 0) { //checks if x is greater than current nodes data
                temp = temp.getNext()[level];
            }
            prev[level] = temp;
        }
        if (temp.getNext()[0] != null && x.equals(temp.getNext()[0].getData())) {
            return new SetFindResult<>(temp.getNext()[0], prev); // if x is found in the skiplist
        } else {
            return new SetFindResult<>(null, prev); // if x is not found in the skiplist
        }
    }

    private int getRandomLevel(int Level_Max)
    {
        int level = 0;
        Random rand = new Random();
        while (level < Level_Max) {
            int b = rand.nextInt(2);
            if (b == 0) {
                break;
            } else {
                level++;
            }
        }
        return level;
    }

    public String toString()
    {

        StringBuilder rsltStringBuilder = new StringBuilder();

        SkipListNode<T> current = head.getNext()[0];

        while (current != null)
        {

            rsltStringBuilder.append(current.getData() + " ");

            current = current.getNext()[0];

        }

        return rsltStringBuilder.toString();
    }

    // =========================== Helper Methods ===================================== //


    //=================================== Implemented Methods ================================== //

    boolean add(T x) {  // Add an element x to the list.  Returns true if x was a new element.

        SetFindResult<T> result = find(x);

        boolean exists;

        if (result.getNode() != null)
        {
            exists = false;

        }
        else {

            exists = true;

            int Level_Max = head.level();
            int level = getRandomLevel(Level_Max);
            SkipListNode<T> n = new SkipListNode<T>(x, level);

            for (int i = 0; i <= level; i++)
            {
                n.getNext()[i] = result.getPrev()[i].getNext()[i];
                result.getPrev()[i].getNext()[i] = n;
            }
            total_size++;
        }

        return exists;

    }

    T ceiling(T x) { // Least element that is >= x, or null if no such element
        if (isEmpty()) {
            return null;
        } else {
            SetFindResult<T> result = find(x);
            if (result.getNode() == null) {
                if (result.getPrev()[0].getNext()[0] != null) {
                    return result.getPrev()[0].getNext()[0].getData();
                }
            } else {
                return result.getNode().getData();
            }
            return null;
        }
    }

    boolean contains(T x) {  // Is x in the list?
        SetFindResult<T> result = find(x);
        if (result.getNode() == null) {
            return false;
        } else {
            return true;
        }
    }

    T findIndex(int index) {  // Return the element at a given position (index) in the list
        Iterator<T> itr = iterator();
        T data = null;
        Integer inde=0;
        if (isEmpty()) {
            return null;
        } else {
            if (size() >= index) {
                while (itr.hasNext()){
                    if (itr.next().equals(index))
                        return (T) inde;
                    else
                        inde++;
                }
                /*for (int i = 0; i <= index; i++) {
                    if (itr.hasNext()) {
                        inde=i;
                        data = itr.next();
                    } else {
                        inde=null;
                        break;
                    }
                }*/
            }
        }
        return null;
    }

    T first() {  // Return the first element of the list
        if (isEmpty()) {
            return null;
        } else {
            SkipListNode<T> p = head;
            if (p.getNext() != null) {
                return p.getNext()[0].getData();
            }
            return null;
        }
    }

    T floor(T x) {  // Greatest element that is <= x, or null if no such element
        if (isEmpty()) {
            return null;
        } else {
            SetFindResult<T> result = find(x); // calls the find() method with x as parameter
            if (result.getNode() == null) {
                return result.getPrev()[0].getData();
            } else {
                if (result.getPrev() != null) {
                    return result.getNode().getData();
                }
                return null;
            }
        }
    }

    boolean isEmpty() {  // Is the list empty?
        if (total_size == 0) {
            return true;
        } else {
            return false;
        }
    }

    Iterator<T> iterator() {// Returns an iterator for the list

        class ListIterator<T extends Comparable<? super T>> implements Iterator<T> {
            private SkipListNode<T> iterNode;

            public ListIterator(SkipListNode<T> head) {
                this.iterNode = head;
            }

            @Override
            public boolean hasNext() {
                return iterNode.getNext()[0] != null;
            }

            @Override
            public T next() {
                iterNode = iterNode.getNext()[0];
                return iterNode.getData();
            }

        }
        ;

        ListIterator<T> iter = new ListIterator<T>(head);

        return iter;
    }

    T last() {  // Return the last element of the list
        if (isEmpty()) {
            return null;
        } else {
            SkipListNode<T> temp = head;
            for (int level = head.level(); level >= 0; level--) {
                while (temp.getNext()[level] != null && temp.getNext()[level].getNext()[0] != null) {
                    temp = temp.getNext()[level];
                }
            }
            return temp.getNext()[0].getData();
        }
    }

    private void rebuild() {  // Rebuild this list into a perfect skip list

    }

    boolean remove(T x) {  // Remove x from list; returns false if x was not in list
        if (isEmpty()) {
            return false;
        } else {
            SetFindResult<T> result = find(x);
            if (result.getNode() == null) {
                return false;
            } else {
                total_size--;
                for (int i = 0; i <= Level_Max; i++) {
                    if (result.getPrev()[i].getNext()[i] != null) {
                        if (result.getNode().getData().equals(result.getPrev()[i].getNext()[i].getData())) {
                            result.getPrev()[i].getNext()[i] = result.getNode().getNext()[i];
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }

    int size() {  // Number of elements in the list
        return total_size;
    }


    static Integer findIndex(Iterator<Integer> it,int index){
        int cnt=0;
        while (it.hasNext()){
            if (it.next().equals(index))
                return cnt;
            else
                cnt++;
        }
        return null;
    }

    //=================================== Implemented Methods ================================== //


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Thread threadSkip = null;
        Thread threadTree = null;

        final SkipList<Integer> skipList = new SkipList<>();

        final TreeSet<Integer> treeSet=new TreeSet<>();

        final int NO_OF_METHODS = 10;

        int idx;
        Timer timerSkip = new Timer();
        Timer timerTree = new Timer();

        List<Integer> methodIndices = new ArrayList<>();


        for (int i = 1; i <= NO_OF_METHODS; i++) {
            methodIndices.add(i); //Randomly selecting functionality for each of the data structure
        }

        Random randomGenerator = new Random();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        timerSkip.start();
        timerTree.start();

        for (idx = 1; idx <= 100000; ++idx) {

            final int randomInt = randomGenerator.nextInt(10000);

            Collections.shuffle(methodIndices);

            for (final Integer index : methodIndices) {

                threadSkip = new Thread() {
                    public void run() {
                        switch (index)
                        {
                            default:
                                System.out.println("Thread Skip with index = " + index +" Int =" + randomInt);
                                break;
                            case 1:
                                System.out.println("=== Trying to add " + randomInt);
                                System.out.println("Add Result : " + skipList.add(randomInt));
                                break;
                            case 2:
                                System.out.println("=== Trying to remove " + randomInt);
                                System.out.println("Remove Result : " + skipList.remove(randomInt));
                                break;
                            case 3:
                                System.out.println("==== Trying to check contains " + randomInt);
                                System.out.println("Contains Result : " + skipList.contains(randomInt));
                                break;
                            case 4:
                                System.out.println("==== Trying to check ceiling " + randomInt);
                                System.out.println("Ceiling Result : " + skipList.ceiling(randomInt));
                                break;
                            case 5:
                                System.out.println("==== Trying to Find Index " + randomInt);
                                System.out.println("FindIndex Result : " + skipList.findIndex(randomInt));
                                break;
                            case 6:
                                System.out.println("==== Trying to Find First Element");
                                System.out.println("First Element Result : " + skipList.first());
                                break;
                            case 7:
                                System.out.println("==== Trying to Find Floor" + randomInt);
                                System.out.println("Floor Element Result : " + skipList.floor(randomInt));
                                break;
                            case 8:
                                System.out.println("==== Trying to check IsEmpty ");
                                System.out.println("IsEmpty Result : " + skipList.isEmpty());
                                break;
                            case 9:
                                System.out.println("==== Trying to Find Last Element ");
                                System.out.println("Last Element Result : " + skipList.last());
                                break;
                            case 10:
                                System.out.println("==== Trying to print Size ");
                                System.out.println("Size Result : " + skipList.size());
                                break;
                        }
                        /*skipList.rebuild();*/
                        timerSkip.end();
                    }
                };

                threadTree = new Thread() {
                    public void run() {
                        switch (index)
                        {
                            case 1:
                                System.out.println("=== Tree Trying to add " + randomInt);
                                System.out.println("Add Result : " + treeSet.add(randomInt));
                                break;
                            case 2:
                                System.out.println("=== Tree Trying to remove " + randomInt);
                                System.out.println("Remove Result : " + treeSet.remove(randomInt));
                                break;
                            case 3:
                                System.out.println("==== Tree Trying to check contains " + randomInt);
                                System.out.println("Contains Result : " + treeSet.contains(randomInt));
                                break;
                            case 4:
                                System.out.println("==== Tree Trying to check ceiling " + randomInt);
                                System.out.println("Ceiling Result : " + treeSet.ceiling(randomInt));
                                break;
                            case 5:
                                System.out.println("==== Tree Trying to Find Index " + randomInt);
                                System.out.println("FindIndex Result : " + findIndex(treeSet.iterator(), randomInt));
                                break;
                            case 6:
                                System.out.println("==== Tree Trying to Find First Element");
                                System.out.println("First Element Result : " + treeSet.first());
                                break;
                            case 7:
                                System.out.println("==== Tree Trying to Find Floor" + randomInt);
                                System.out.println("Floor Element Result : " + treeSet.floor(randomInt));
                                break;
                            case 8:
                                System.out.println("==== Tree Trying to check IsEmpty ");
                                System.out.println("IsEmpty Result : " + treeSet.isEmpty());
                                break;
                            case 9:
                                System.out.println("==== Tree Trying to Find Last Element ");
                                System.out.println("Last Element Result : " + treeSet.last());
                                break;
                            case 10:
                                System.out.println("==== Tree Trying to print Size ");
                                System.out.println("Size Result : " + treeSet.size());
                                break;
                            default:
                                System.out.println("Thread Tree with index = " + index +" Int =" + randomInt);
                                break;
                        }
                        /*skipList.rebuild();*/
                        timerTree.end();
                    }
                };
//                Replace the multithreaded functions with http://crunchify.com/how-to-run-multiple-threads-concurrently-in-java-executorservice-approach/

                threadSkip.start();

                threadTree.start();

                threadSkip.join();
                threadTree.join();
            }
        }

      /*  if (executorService.isTerminated()) {
            executorService.shutdownNow();
        }*/

        System.out.println("====== SkipList Time ====:");
        System.out.println(timerSkip.toString());
        System.out.println("====== Tree Set Time ====:");
        System.out.println(timerTree.toString());

    }

}