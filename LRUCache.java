import java.util.concurrent.ConcurrentHashMap;

public class LRUCache {

    /*
    The LRUCache object uses a Concurrent HashMap and a Doubly-Linked List to implement a Least-Recently Used
    Caching strategy.
    The HashMap takes a <Key, Node> pair
    Nodes are the building blocks of the Linked List and contain references to Next, Previous, and a reference to the
    key and object they were created with.
    This allows fast HashMap lookup for Objects through the attached Key-Node pair, in addition to letting the Linked List
    reorganize based on that fast lookup.
    Max capacity is size of int
    Does not support resizing after initialization
    */

    ConcurrentHashMap<String, Node> hTbl;
    private DblLinkedList list;

    public LRUCache(int size) {

        if(size< 1){
            throw new IllegalArgumentException("Tried to create cache with no capacity");
        }
        hTbl = new ConcurrentHashMap<>(size);
        list = new DblLinkedList(size);

    }

    public Object get(String key) {
        /*returns null if there is no object for that key, otherwise returns the object
        If an object is found the list must update the location of the cached object to the front of the list.
        */
        if (hTbl.containsKey(key)) {
            Node item = hTbl.get(key);
            list.refresh(item);
            return item.obj;
        }
        return null;
    }

    public void put(String key, Object obj) throws KeyAlreadyUsedException {
        /*throws a KeyAlreadyUsedException if key already exists
        If the cache limit has been reached, it removes the last node from both the hashMap and the list.
        Otherwise, it adds the node to each structure.
        list.add is a syncronized method.
        */
        if (hTbl.containsKey(key)) {
            throw new KeyAlreadyUsedException();
        }
        if (obj == null) {
            throw new IllegalArgumentException("Attempted to put a null object into cache");
        }
        Node node = new Node(key, obj);
        if (list.size == list.capacity) {
            //Evict oldest node in the cache
            Node removed = list.remove(list.last);
            hTbl.remove(removed.key);
            removed.delete();
        }
        hTbl.put(key, node);
        list.add(node);
    }

    public Object replace(String key, Object obj) {
        /*inserts the object if one for that key does not exist, and returns null
        replaces the object if one exists,, and returns the old object

        */

        if (hTbl.containsKey(key)) {
            Node exists = hTbl.get(key);
            Object old = exists.obj;
            exists.obj = obj;
            list.refresh(exists);
            return old;
        } else {
            try {
                put(key, obj);
            } catch (KeyAlreadyUsedException ex) {
                replace(key, obj);
            }
            return null;
        }
    }

    public Object remove(String key) throws KeyNotFoundException {
        /*removes the object from the cache and returns it
        throws a KeyNotFoundException if key does not exist*/
        if (hTbl.containsKey(key)) {
            Node removeMe = hTbl.get(key);
            hTbl.remove(key);
            list.remove(removeMe);
            return removeMe.obj;
        } else {
            throw new KeyNotFoundException("Remove called on non-existent key");
        }
    }

    public String toString() {
        StringBuilder string1 = new StringBuilder("Contents of Hash: " + hTbl.toString());
        string1.append("\nContents of List: ").append(list.toString());
        return string1.toString();
    }

    public String listSize() {
        return Integer.toString(list.size);
    }

}

class KeyAlreadyUsedException extends Exception {

    public KeyAlreadyUsedException() {
    }

    public KeyAlreadyUsedException(String message) {
        super(message);
    }
}

class KeyNotFoundException extends Exception {

    public KeyNotFoundException() {
    }

    public KeyNotFoundException(String message) {
        super(message);
    }
}