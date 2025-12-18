import java.util.*;
public class Treemap {
    public static void main(String[] args) {
        TreeMap<Integer, String> map = new TreeMap<>();

        map.put(3, "Three");
        map.put(1, "One");
        map.put(2, "Two");  // stored in sorted key order automatically

        System.out.println("TreeMap: " + map);

        map.remove(1);
        System.out.println("After remove: " + map);

        System.out.println("Value for key 3: " + map.get(3));
    }
}


