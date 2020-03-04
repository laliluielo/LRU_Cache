import java.util.ArrayList;
import java.util.Random;

public class TestRunner {

    /*
    A collection of tests for the LRUCache class
    */

    public static void main(String[] args) {


        testCreation(10);
        testCreation(500, false);
        testCreation(100000, false);

        testAcceptsObjects();

        LRUCache cache = testCache();
        testGets(cache);

        testPuts();

        cache = testCache();
        testReplace(cache);

        cache = testCache();
        testRemove(cache);

    }

    public static void testCreation(int size) {
        System.out.println("///Testing cache creation///");
        LRUCache cache = new LRUCache(size);
        Random rand = new java.util.Random();

        try {
            for (int i = 0; i < size; i++) {
                cache.put(Integer.toString(rand.nextInt()), Integer.toString(i));
            }
            System.out.print(cache.toString() + "\n\n");
        } catch (KeyAlreadyUsedException ex) {
            System.out.println("Attempted to overwrite an existing key.");
        }

    }

    public static void testCreation(int size, boolean print) {
        //Testing larger capacities
        LRUCache cache = new LRUCache(size);
        Random rand = new java.util.Random();


        for (int i = 0; i < size; i++) {
            try {
                cache.put(Integer.toString(rand.nextInt()), Integer.toString(i));
            } catch (KeyAlreadyUsedException ex) {
                System.out.println("Attempted to overwrite an existing key.");
                i--; //try again
            }
        }
        System.out.println("Finished populating cache of: " + cache.hTbl.mappingCount());
        System.out.println("List has size: " + cache.listSize() + "\n");
    }

    public static void testAcceptsObjects() {
        System.out.println("///Testing that cache accepts various objects///");
        LRUCache cache = new LRUCache(10);
        try {
            cache.put("0", new ArrayList(5));
            cache.put("1", new Object());
            cache.put("2", new LRUCache(1)); //Size must be > 0 or else an error is thrown
            cache.put("3", new Thread());
            cache.put("4", Integer.valueOf("54"));
            cache.put("5", new StringBuilder("StringBuilder"));
            cache.put("6", new Random());
        } catch (KeyAlreadyUsedException ex) {
            System.out.println("Shouldn't be possible");
        }
        System.out.print(cache.toString() + "\n\n");

    }

    public static void testGets(LRUCache cache) {
        System.out.println("///Testing that the get function works properly///");
        System.out.println(cache.toString());
        Object obj = cache.get("0");
        System.out.println("0 maps to " + obj.toString());
        obj = cache.get("4");
        System.out.println("4 maps to " + obj.toString());
        System.out.println("(4,E) is now first in list");
        System.out.println(cache.toString() + "\n");
        obj = cache.get("3");
        System.out.println("Getting (3,D) from middle of list updates properly.");
        System.out.println(cache.toString() + "\n");

        obj = cache.get("12");
        if (obj == null) {
            System.out.println("Getting a key that doesn't exist returns null\n");
        } else {
            System.out.println("A key that doesn't exist didn't return null in the gets function\n");
        }

    }

    public static void testPuts() {
        System.out.println("///Testing that the put function works properly///");
        LRUCache cache = new LRUCache(3);
        System.out.println("Cache size is 3");
        System.out.println(cache);
        System.out.println("Putting 3 objects in cache.");
        try {
            cache.put("0", "A");
            cache.put("1", "B");
            cache.put("2", "C");
        } catch (KeyAlreadyUsedException ex) {
            System.out.println("Shouldn't happen");
        }
        System.out.println(cache);
        System.out.println("Putting two more objects in cache.");
        try {
            cache.put("3", "D");
            cache.put("4", "E");
        } catch (KeyAlreadyUsedException ex) {
            System.out.println("Shouldn't happen");
        }
        System.out.println("Size of hashmap is " + cache.hTbl.mappingCount() + " and size of list is " + cache.listSize());
        System.out.println(cache + "\n");

    }

    public static void testReplace(LRUCache cache) {
        System.out.println("///Testing that the replace function works properly///");
        System.out.println(cache);
        Object obj = cache.replace("5", "F");
        if (obj == null) {
            System.out.println("Replace on new key 5: returned object is null and oldest object is evicted");
            System.out.println(cache.toString() + "\n");
        }
        obj = cache.replace("2", "Apple");
        System.out.println("Replace on key 2: returned object is " + obj.toString() + " and new object for key 2 is " + cache.get("2"));
        System.out.println(cache.toString() + "\n");
    }

    public static void testRemove(LRUCache cache) {
        System.out.println("///Testing that the remove function works properly///");
        System.out.println(cache);
        Object obj = new Object();
        try {
            cache.remove("ABCD");
        } catch (KeyNotFoundException ex) {
            System.out.println("No key ABCD found, KeyNotFoundException exception caught.");
        }
        try {
            obj = cache.remove("4");
        } catch (KeyNotFoundException ex) {
            System.out.println("Shouldn't happen.");
        }
        System.out.println("Removed first element " + obj.toString());
        System.out.println(cache.toString());
        try {
            obj = cache.remove("0");
        } catch (KeyNotFoundException ex) {
            System.out.println("Shouldn't happen.");
        }
        System.out.println("Removed last element " + obj.toString());
        System.out.println(cache.toString());
        try {
            obj = cache.remove("2");
        } catch (KeyNotFoundException ex) {
            System.out.println("Shouldn't happen.");
        }
        System.out.println("Removed middle element " + obj.toString());
        System.out.println(cache.toString());
        try {
            cache.remove("1");
            cache.remove("3");
            cache.remove("");
        } catch (KeyNotFoundException ex) {
            System.out.println("Remove called on empty cache and KeyNotFound exception caught.");
        }
        System.out.println(cache.toString());

    }

    public static LRUCache testCache() {
        //Small test cache for manual inspection
        LRUCache cache = new LRUCache(5);
        try {
            cache.put("0", "A");
            cache.put("1", "B");
            cache.put("2", "C");
            cache.put("3", "D");
            cache.put("4", "E");
        } catch (KeyAlreadyUsedException ex) {
            System.out.println("Shouldn't be possible");
        }
        return cache;
    }

}
