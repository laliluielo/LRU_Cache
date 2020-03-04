class DblLinkedList {
    /* Uses the Nodes class to maintain a doubly-linked list
    Determines when to evict the least recently used object based on capacity, which cannot be resized after
    creation
    */

    Node first;
    Node last;
    int size = 0;
    final int capacity;

    public DblLinkedList(int maxSize) {
        first = null;
        last = null;
        capacity = maxSize;
    }

    public void add(String key, Object obj) {
        //Create a Node based off the <Key, Object> pair used in the HashMap
        Node node = new Node(key, obj);
        add(node);
    }

    public synchronized void add(Node node) {
        //Add a node to the front of the list

        if (size == capacity) {//Full list --duplicated check in LRUCache
            remove(last);
        }//Empty list
        else if (size == 0) {
            first = node;
            last = node;
        } else {
            first.prev = node;
            node.next = first;
            first = node;
        }
        size++;
    }

    public synchronized Node remove(Node node) {

        if (size == 0) {
            //Should not happen due to KeyNotFoundException in LRUCache's remove.
            throw new IllegalArgumentException("Trying to remove from empty list");
        } else if (size == 1) {
            first = null;
            last = null;
        } else if (last.equals(node)) {
            //End of list
            last.prev.next = null;
            last = last.prev;
        } else if (first.equals(node)) {
            //Start of list
            first.next.prev = null;
            first = first.next;
        } else { //Node in middle of list
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        size--;

        node.next = null;
        node.prev = null;
        return node;
    }

    public void refresh(Node node) {
        //Cycle node to the front of the list
        remove(node);
        add(node);
    }

    public String toString() {
        if (size == 0) {
            return "Empty List";
        }
        Node current = first;
        StringBuilder string = new StringBuilder();
        while (true) {
            string.append("(" + current.key + "," + current.obj.toString()).append(") ");
            if (current.next != null) {
                current = current.next;
            } else break;
        }
        return string.toString();
    }

    /*

    public static void main(String[] args){
        //Debug use
        DblLinkedList list = new DblLinkedList(10);
        list.add("10", new Object());
        list.add("9",new Object());
        list.add("8",new Object());
        list.add("7",new Object());
        list.add("6",new Object());
        System.out.println(list);
        list.remove(list.first);
        System.out.println(list);
        list.remove(list.last);
        System.out.println(list);
    }

    */
}

class Node {
    /*
    Holds both the object and its key to allow for fast removal of any element in the Linked List
     */

    Node prev = null;
    Node next = null;
    Object obj;
    String key;

    public Node(String key, Object obj) {
        this.obj = obj;
        this.key = key;
    }

    public void delete() {
        //May not be strictly required
        prev = null;
        next = null;
        obj = null;
        key = null;
    }
}
