
import java.util.*;

public class Arraylist {
    public static void main(String[] args) {
        ArrayList<Integer> nums = new ArrayList<>();

        nums.add(10);
        nums.add(20);
        nums.add(30);

        System.out.println("ArrayList: " + nums);

        nums.set(1, 25);      // change value
        System.out.println("After update: " + nums);

        nums.remove(0);       // remove by index
        System.out.println("After remove: " + nums);
    }
}
